package com.yhr.smcp.services.character;

import com.yhr.smcp.entities.character.mythicplus.KeystoneRun;
import com.yhr.smcp.entities.character.mythicplus.MythicPlusProfile;
import com.yhr.smcp.entities.character.mythicplus.MythicSeason;
import com.yhr.smcp.exceptions.BlizzardSyncException;
import com.yhr.smcp.parsers.mythicplus.MythicPlusProfileParser;
import com.yhr.smcp.parsers.mythicplus.MythicSeasonParser;
import com.yhr.smcp.parsers.mythicplus.MythicSeasonParser.SeasonParserResult;
import com.yhr.smcp.repositories.character.mythicplus.KeystoneRunRepository;
import com.yhr.smcp.repositories.character.mythicplus.MythicPlusProfileRepository;
import com.yhr.smcp.repositories.character.mythicplus.MythicSeasonRepository;
import com.yhr.smcp.services.BlizzardApiService;
import com.yhr.smcp.services.gamedata.mythicplus.KeystoneSeasonDataService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
    private final ObjectMapper objectMapper;

    private final MythicPlusProfileParser mythicPlusProfileParser;
    private final MythicSeasonParser mythicSeasonParser;

    private final MythicPlusProfileRepository mythicPlusProfileRepository;

    private final KeystoneSeasonDataService keystoneSeasonDataService;
    private final MythicSeasonRepository mythicSeasonRepository;
    private final KeystoneRunRepository keystoneRunRepository;

    @Transactional
    public MythicPlusProfile syncProfile(String realm, String name) {
        try {
            String mythicProfileRawJson = fetchMythicProfileRoot(realm, name);
            JsonNode mythicProfileRoot = objectMapper.readTree(mythicProfileRawJson);
            List<Integer> seasonIds = extractSeasonsIds(mythicProfileRoot);
            List<JsonNode> seasonRootNodes = fetchSeasonNodes(realm, name, seasonIds);

            MythicPlusProfile profile = mythicPlusProfileParser.parse(mythicProfileRoot);
            mythicPlusProfileRepository.save(profile);
            saveProfileWithSeasons(seasonRootNodes, profile);
            return profile;

        } catch (Exception e) {
            throw new BlizzardSyncException("failed to sync mythic plus profile for %s at %s".formatted(name, realm), e);
        }
    }

    private String fetchMythicProfileRoot(String realm, String name) {
        try {
            return blizzardApiService.getMythicCharacterProfile(realm, name).block();
        } catch (Exception e) {
            throw new BlizzardSyncException("failed to sync character mythic profile name=%s at %s ".formatted(name, realm), e);
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
        try {
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

        } catch (Exception e) {
            throw new BlizzardSyncException("failed to sync season profiles for name=%s at %s".formatted(characterName, realm), e);
        }
    }

    private void saveProfileWithSeasons(List<JsonNode> seasonsRootNodes, MythicPlusProfile profile) {
        for (JsonNode seasonNode : seasonsRootNodes) {
            SeasonParserResult result = mythicSeasonParser.parse(seasonNode);

            MythicSeason season = result.mythicSeason();
            season.setProfile(profile);
            season.setKeystoneSeason(keystoneSeasonDataService.getReferenceById(result.seasonId()));
            season = mythicSeasonRepository.save(season);

            for (KeystoneRun run : result.keystoneRuns()) {
                run.setMythicSeason(season);
                keystoneRunRepository.save(run);
            }
        }
    }

}
