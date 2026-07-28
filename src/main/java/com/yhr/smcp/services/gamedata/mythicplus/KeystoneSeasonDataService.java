package com.yhr.smcp.services.gamedata.mythicplus;

import com.yhr.smcp.client.BlizzardDynamicApiClient;
import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneSeason;
import com.yhr.smcp.exceptions.BlizzardSyncException;
import com.yhr.smcp.parsers.gamedata.mythicplus.KeystoneSeasonParser;
import com.yhr.smcp.repositories.gamedata.mythicplus.KeystoneSeasonsRepository;
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
public class KeystoneSeasonDataService {
    private final KeystoneSeasonsRepository keystoneSeasonsRepository;
    private final BlizzardDynamicApiClient blizzardDynamicApiClient;
    private final KeystoneSeasonParser keystoneSeasonParser;
    private final ObjectMapper objectMapper;

    public void syncMythicSeasons() {
        String rawIndexJson = fetchSeasonIndex();
        JsonNode indexRoot = objectMapper.readTree(rawIndexJson);
        Set<Long> existingIds = new HashSet<>(keystoneSeasonsRepository.findAllIds());
        List<Long> ids = new ArrayList<>();
        indexRoot.path("seasons").forEach(season -> {
            Long seasonId = season.path("id").asLong();
            if (!existingIds.contains(seasonId)) {
                ids.add(seasonId);
            }
        });

        Flux.fromIterable(ids)
                .flatMap(id -> blizzardDynamicApiClient.getSeasonDetails(id)
                        .map(json -> Map.entry(id, json))
                        .onErrorResume(e -> {
                            log.error("failed to fetch season details id={}", id, e);
                            return Mono.empty();
                        }), 20)
                .doOnNext(entry -> saveMythicSeason(entry.getKey(), entry.getValue()))
                .blockLast();
    }

    public Optional<KeystoneSeason> getReferenceById(Long seasonId) {
        return keystoneSeasonsRepository.findById(seasonId);
    }

    private String fetchSeasonIndex() {
        try {
            return blizzardDynamicApiClient.getSeasonIndex().block();
        } catch (Exception e) {
            throw new BlizzardSyncException("failed to fetch season indexes", e);
        }
    }

    private void saveMythicSeason(Long seasonId, String seasonDetailsJson) {
        try {
            JsonNode seasonRoot = objectMapper.readTree(seasonDetailsJson);
            KeystoneSeason keystoneSeason = keystoneSeasonParser.parse(seasonRoot);
            keystoneSeasonsRepository.save(keystoneSeason);
        } catch (BlizzardSyncException e) {
            log.error("failed to parse season id={}", seasonId, e);
        } catch (DataAccessException e) {
            log.error("failed to save season id={}", seasonId, e);
        } catch (Exception e) {
            log.error("failed to sync season id={}", seasonId, e);
        }
    }

}
