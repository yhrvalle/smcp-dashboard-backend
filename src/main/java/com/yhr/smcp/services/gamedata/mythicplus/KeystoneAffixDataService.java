package com.yhr.smcp.services.gamedata.mythicplus;

import com.yhr.smcp.client.BlizzardStaticApiClient;
import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneAffix;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import com.yhr.smcp.exceptions.BlizzardSyncException;
import com.yhr.smcp.parsers.gamedata.mythicplus.KeystoneAffixParser;
import com.yhr.smcp.repositories.gamedata.mythicplus.KeystoneAffixRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeystoneAffixDataService {
    private final KeystoneAffixRepository keystoneAffixRepository;
    private final KeystoneAffixParser keystoneAffixParser;
    private final BlizzardStaticApiClient blizzardStaticApiClient;
    private final ObjectMapper objectMapper;

    public void syncKeystoneAffixes() {

        String rawIndexJson = fetchAffixesIndex();
        JsonNode indexRoot = objectMapper.readTree(rawIndexJson);
        Set<Integer> exisingIds = new HashSet<>(keystoneAffixRepository.findAllIds());
        List<Integer> ids = new ArrayList<>();
        indexRoot.path("affixes").forEach(affix -> {
            Integer affixId = affix.path("id").asInt();
            if (!exisingIds.contains(affixId)) {
                ids.add(affixId);
            }
        });

        Flux.fromIterable(ids)
                .flatMap(id -> blizzardStaticApiClient.getAffixDetails(id)
                        .map(json -> Map.entry(id, json))
                        .onErrorResume(e -> {
                            log.error("failed to fetch affix details id={}", id, e);
                            return Mono.empty();
                        }), 20)
                .doOnNext(entry -> saveAffix(entry.getKey(), entry.getValue()))
                .blockLast();
    }

    public KeystoneAffix findKeystoneAffixById(Integer affixId) {
        return keystoneAffixRepository.findById(affixId).orElse(null);
    }

    public List<KeystoneAffix> findAllKeystoneAffixByIds(List<Integer> affixIds) {
        return keystoneAffixRepository.findAllById(affixIds);
    }

    private String fetchAffixesIndex() {
        try {
            return blizzardStaticApiClient.getAffixIndex().block();
        } catch (Exception e) {
            throw new BlizzardSyncException("failed to fetch affixes indexes", e);
        }
    }

    private void saveAffix(Integer affixId, String affixDetailsJson) {
        try {
            JsonNode affixRoot = objectMapper.readTree(affixDetailsJson);
            KeystoneAffix keystoneAffix = keystoneAffixParser.parse(affixRoot);
            keystoneAffixRepository.save(keystoneAffix);
        } catch (BlizzardParsingException e) {
            log.error("failed to parse affix id={}", affixId, e);
        } catch (DataAccessException e) {
            log.error("failed to save affix id={}", affixId, e);
        } catch (Exception e) {
            log.error("failed to sync affix id={}", affixId, e);
        }
    }

}
