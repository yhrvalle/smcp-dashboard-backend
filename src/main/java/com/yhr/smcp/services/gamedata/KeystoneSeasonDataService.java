package com.yhr.smcp.services.gamedata;

import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneSeason;
import com.yhr.smcp.parsers.gamedata.mythicplus.KeystoneSeasonParser;
import com.yhr.smcp.repositories.gamedata.mythicplus.KeystoneSeasonsRepository;
import com.yhr.smcp.services.BlizzardApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeystoneSeasonDataService {
    private final KeystoneSeasonsRepository keystoneSeasonsRepository;
    private final BlizzardApiService blizzardApiService;
    private final KeystoneSeasonParser keystoneSeasonParser;
    private final ObjectMapper objectMapper;

    public void syncMythicSeasons() {
        try {
            String rawIndexJson = blizzardApiService.getSeasonIndex().block();
            JsonNode indexRoot = objectMapper.readTree(rawIndexJson);
            indexRoot.path("seasons").forEach(season -> {
                Integer seasonId = season.path("id").asInt();
                if (keystoneSeasonsRepository.existsById(seasonId)) {
                    return;
                }
                String seasonDetailsJson = blizzardApiService.getSeasonDetails(seasonId).block();
                JsonNode seasonRoot = objectMapper.readTree(seasonDetailsJson);
                KeystoneSeason keystoneSeason = keystoneSeasonParser.parse(seasonRoot);
                keystoneSeasonsRepository.save(keystoneSeason);

            });
        } catch (Exception e) {
            throw new RuntimeException("KeystoneSeasonDataService syncMythicSeasons error " + e.getMessage(), e);
        }
    }

    public KeystoneSeason findKeystoneSeasonById(Integer seasonId) {
        return keystoneSeasonsRepository.findById(seasonId).orElse(null);
    }

    public void saveKeystoneSeason(KeystoneSeason keystoneSeason) {
        keystoneSeasonsRepository.save(keystoneSeason);
    }

}
