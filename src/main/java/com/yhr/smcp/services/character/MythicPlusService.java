package com.yhr.smcp.services.character;

import com.yhr.smcp.client.BlizzardProfileApiClient;
import com.yhr.smcp.entities.character.mythicplus.KeystoneRun;
import com.yhr.smcp.entities.character.mythicplus.MythicPlusProfile;
import com.yhr.smcp.entities.character.mythicplus.MythicSeason;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import com.yhr.smcp.exceptions.BlizzardSyncException;
import com.yhr.smcp.parsers.mythicplus.MythicPlusProfileParser;
import com.yhr.smcp.parsers.mythicplus.MythicSeasonParser;
import com.yhr.smcp.parsers.mythicplus.MythicSeasonParser.SeasonParserResult;
import com.yhr.smcp.repositories.character.mythicplus.KeystoneRunRepository;
import com.yhr.smcp.repositories.character.mythicplus.MythicPlusProfileRepository;
import com.yhr.smcp.repositories.character.mythicplus.MythicSeasonRepository;
import com.yhr.smcp.services.gamedata.mythicplus.KeystoneSeasonDataService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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
    private final BlizzardProfileApiClient blizzardProfileApiClient;
    private final ObjectMapper objectMapper;

    private final MythicPlusProfileParser mythicPlusProfileParser;
    private final MythicSeasonParser mythicSeasonParser;

    private final MythicPlusProfileRepository mythicPlusProfileRepository;

    private final KeystoneSeasonDataService keystoneSeasonDataService;
    private final MythicSeasonRepository mythicSeasonRepository;
    private final KeystoneRunRepository keystoneRunRepository;

    @Transactional
    public MythicPlusProfile syncProfile(String realm, String name, Long existingProfileId) {
        try {
            String mythicProfileRawJson = fetchMythicProfileRoot(realm, name);
            JsonNode mythicProfileRoot = objectMapper.readTree(mythicProfileRawJson);
            List<Integer> seasonIds = extractSeasonsIds(mythicProfileRoot);
            List<String> seasonsRawJson = fetchSeasonsRawJson(realm, name, seasonIds);

            MythicPlusProfile parsed = mythicPlusProfileParser.parse(mythicProfileRoot);
            MythicPlusProfile profile = existingProfileId != null
                    ? mythicPlusProfileRepository.getReferenceById(existingProfileId)
                    : new MythicPlusProfile();

            profile.setCurrentMythicRating(parsed.getCurrentMythicRating());
            profile.setRatingColor(parsed.getRatingColor());
            mythicPlusProfileRepository.save(profile);
            saveProfileWithSeasons(seasonsRawJson, profile);
            return profile;

        } catch (BlizzardSyncException | BlizzardParsingException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new BlizzardSyncException("failed to save mythic plus profile for %s at %s".formatted(name, realm), e);
        }

    }

    private String fetchMythicProfileRoot(String realm, String name) {
        try {
            return blizzardProfileApiClient.getMythicCharacterProfile(realm, name).block();
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

    private List<String> fetchSeasonsRawJson(String realm, String characterName, List<Integer> seasonIds) {
        try {
            return blizzardProfileApiClient.getCharacterSeasonsProfiles(realm, characterName, seasonIds)
                    .blockOptional()
                    .orElse(Collections.emptyList());
        } catch (Exception e) {
            throw new BlizzardSyncException("failed to sync season profiles for name=%s at %s".formatted(characterName, realm), e);
        }
    }

    private void saveProfileWithSeasons(List<String> seasonsRawJson, MythicPlusProfile profile) {
        for (String rawJson : seasonsRawJson) {
            try {
                JsonNode seasonRoot = objectMapper.readTree(rawJson);
                SeasonParserResult result = mythicSeasonParser.parse(seasonRoot);


                MythicSeason season = mythicSeasonRepository.findByProfileIdAndKeystoneSeasonId(profile.getId(), result.seasonId())
                        .orElseGet(MythicSeason::new);

                season.setProfile(profile);
                season.setKeystoneSeason(keystoneSeasonDataService.getReferenceById(result.seasonId()));
                season.setSeasonRating(result.mythicSeason().getSeasonRating());
                season.setRatingColor(result.mythicSeason().getRatingColor());
                season = mythicSeasonRepository.save(season);
                for (KeystoneRun parsedRun : result.keystoneRuns()) {
                    KeystoneRun run = keystoneRunRepository.findByMythicSeasonIdAndDungeonName(season.getId(), parsedRun.getDungeonName())
                            .orElseGet(KeystoneRun::new);
                    run.setMythicSeason(season);
                    run.setDungeonName(parsedRun.getDungeonName());
                    run.setLevel(parsedRun.getLevel());
                    run.setIsTimed(parsedRun.getIsTimed());
                    run.setDungeonMythicRating(parsedRun.getDungeonMythicRating());
                    run.setRatingColor(parsedRun.getRatingColor());
                    run.setCompletedTimestamp(parsedRun.getCompletedTimestamp());
                    run.setDuration(parsedRun.getDuration());
                    run.setAffixIds(parsedRun.getAffixIds());
                    run.setMembers(parsedRun.getMembers());
                    keystoneRunRepository.save(run);
                }
            } catch (BlizzardParsingException e) {
                log.error("failed to parse season for profile id={}", profile.getId(), e);
            } catch (DataAccessException e) {
                log.error("failed to save season for profile id={}", profile.getId(), e);
            } catch (Exception e) {
                log.error("failed to process season for profile id={}", profile.getId(), e);
            }


        }
    }

}
