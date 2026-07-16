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
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

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
        indexRoot.path("seasons").forEach(season -> {
            Integer seasonId = season.path("id").asInt();
            if (keystoneSeasonsRepository.existsById(seasonId)) {
                return;
            }
            try {
                String seasonDetailsJson = blizzardDynamicApiClient.getSeasonDetails(seasonId).block();
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
        });

    }

    public KeystoneSeason getReferenceById(Integer seasonId) {
        return keystoneSeasonsRepository.findById(seasonId).orElse(null);
    }

    private String fetchSeasonIndex() {
        try {
            return blizzardDynamicApiClient.getSeasonIndex().block();
        } catch (Exception e) {
            throw new BlizzardSyncException("failed to fetch season indexes", e);
        }
    }


}
