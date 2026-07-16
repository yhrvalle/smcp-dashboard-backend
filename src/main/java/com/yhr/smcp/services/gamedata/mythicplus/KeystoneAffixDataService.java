package com.yhr.smcp.services.gamedata.mythicplus;

import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneAffix;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import com.yhr.smcp.exceptions.BlizzardSyncException;
import com.yhr.smcp.parsers.gamedata.mythicplus.KeystoneAffixParser;
import com.yhr.smcp.repositories.gamedata.mythicplus.KeystoneAffixRepository;
import com.yhr.smcp.services.BlizzardApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeystoneAffixDataService {
    private final KeystoneAffixRepository keystoneAffixRepository;
    private final KeystoneAffixParser keystoneAffixParser;
    private final BlizzardApiService blizzardApiService;
    private final ObjectMapper objectMapper;

    public void syncKeystoneAffixes() {

        String rawIndexJson = fetchAffixesIndex();
        JsonNode indexRoot = objectMapper.readTree(rawIndexJson);
        indexRoot.path("affixes").forEach(affix -> {
            Integer affixId = affix.path("id").asInt();
            if (keystoneAffixRepository.existsById(affixId)) {
                return;
            }
            try {
                String affixDetailsJson = blizzardApiService.getAffixDetails(affixId).block();
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
        });
    }

    public KeystoneAffix findKeystoneAffixById(Integer affixId) {
        return keystoneAffixRepository.findById(affixId).orElse(null);
    }

    public List<KeystoneAffix> findAllKeystoneAffixByIds(List<Integer> affixIds) {
        return keystoneAffixRepository.findAllById(affixIds);
    }

    private String fetchAffixesIndex() {
        try {
            return blizzardApiService.getAffixIndex().block();
        } catch (Exception e) {
            throw new BlizzardSyncException("failed to fetch affixes indexes", e);
        }
    }


}
