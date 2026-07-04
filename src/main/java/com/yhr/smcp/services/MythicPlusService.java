package com.yhr.smcp.services;

import com.yhr.smcp.entities.character.MythicPlusProfile;
import com.yhr.smcp.entities.character.mythicplus.KeystoneRun;
import com.yhr.smcp.entities.character.mythicplus.MythicSeason;
import com.yhr.smcp.parsers.mythicplus.MythicPlusProfileParser;
import com.yhr.smcp.parsers.mythicplus.MythicSeasonParser;
import com.yhr.smcp.repositories.character.mythicplus.KeystoneRunRepository;
import com.yhr.smcp.repositories.character.mythicplus.MythicPlusProfileRepository;
import com.yhr.smcp.repositories.character.mythicplus.MythicSeasonRepository;
import com.yhr.smcp.services.gamedata.GameDataFacadeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MythicPlusService {
    private final BlizzardApiService blizzardApiService;
    private final GameDataFacadeService gameDataService;
    private final ObjectMapper objectMapper;

    private final MythicPlusProfileParser mythicPlusProfileParser;
    private final MythicSeasonParser mythicSeasonParser;

    private final MythicPlusProfileRepository mythicPlusProfileRepository;
    private final MythicSeasonRepository mythicSeasonRepository;
    private final KeystoneRunRepository keystoneRunRepository;

    @Transactional
    public MythicPlusProfile syncProfile(String realm, String name) {
        try {
            JsonNode mythicProfileRoot = fetchMythicProfileRoot(realm, name);
            List<Integer> seasonIds = extractSeasonsIds(mythicProfileRoot);
            List<JsonNode> seasonRootNodes = fetchSeasonNodes(realm, name, seasonIds);

            GameDataFacadeService.GameDataLookup gameDataLookups = gameDataService.buildLookUps(seasonIds, seasonRootNodes);
            MythicPlusProfile profile = mythicPlusProfileParser.buildProfile(mythicProfileRoot);
            mythicPlusProfileRepository.save(profile);
            saveProfileWithSeasons(seasonRootNodes, profile, gameDataLookups);

            return profile;


        } catch (Exception e) {
            log.error("Error syncing mythic profile name={}, value={}", name, e.getMessage());
            throw new RuntimeException("MythicPlusService: failed to sync mythic plus profile: " + e.getMessage(), e);
        }

    }

    private JsonNode fetchMythicProfileRoot(String realm, String name) {
        try {
            String rawMythicProfileJson = blizzardApiService.getMythicCharacterProfile(realm, name).block();
            return objectMapper.readTree(rawMythicProfileJson);

        } catch (Exception e) {
            throw new RuntimeException("fetchMythicProfileRoot: failed to fetch MythicCharacterProfile: " + e.getMessage(), e);
        }
    }

    private List<Integer> extractSeasonsIds(JsonNode mythicProfileRoot) {
        List<Integer> seasonIds = new ArrayList<>();
        mythicProfileRoot.path("seasons").forEach((season) -> {
            seasonIds.add(season.path("id").asInt());
        });
        return seasonIds;
    }

    private List<JsonNode> fetchSeasonNodes(String realm, String characterName, List<Integer> seasonIds) {
        List<String> rawSeasonJsons = blizzardApiService.getCharacterSeasonsProfiles(realm, characterName, seasonIds)
                .blockOptional()
                .orElse(Collections.emptyList());
        return rawSeasonJsons.stream()
                .map(raw -> {
                    try {
                        return objectMapper.readTree(raw);
                    } catch (Exception e) {
                        throw new RuntimeException("MythicPlusService - fetchSeasonNodes: failed to fetch MythicCharacterProfile: " + e.getMessage(), e);
                    }
                })
                .toList();
    }

    private void saveProfileWithSeasons(List<JsonNode> seasonsRootNodes, MythicPlusProfile profile, GameDataFacadeService.GameDataLookup lookups) {
        for (JsonNode seasonNode : seasonsRootNodes) {
            Integer seasonId = seasonNode.path("season").path("id").asInt();
            if (!lookups.seasonMap().containsKey(seasonId)) {
                log.warn("Skipping season {} for profile {}: not found in database", seasonId, profile.getId());
                continue;
            }
            MythicSeasonParser.SeasonParserResult result = mythicSeasonParser.parse(seasonNode, lookups.specializationMap(),
                    lookups.seasonMap(), lookups.affixMap());
            MythicSeason season = result.mythicSeason();
            season.setProfile(profile);
            season = mythicSeasonRepository.save(season);

            for (KeystoneRun run : result.keystoneRuns()) {
                run.setMythicSeason(season);
                keystoneRunRepository.save(run);
            }
        }
    }

}
