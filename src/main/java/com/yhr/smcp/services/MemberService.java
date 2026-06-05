package com.yhr.smcp.services;

import com.yhr.smcp.entities.GuildMember;
import com.yhr.smcp.entities.character.MythicPlusProfile;
import com.yhr.smcp.entities.character.mythic.KeystoneMember;
import com.yhr.smcp.entities.character.mythic.KeystoneRun;
import com.yhr.smcp.entities.character.mythic.MythicSeason;
import com.yhr.smcp.repositories.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MemberService {
    private static final Duration CACHE_TTL = Duration.ofHours(1);

    private final MemberRepository memberRepository;
    private final BlizzardApiService blizzardApiService;
    private final ObjectMapper objectMapper;


    public GuildMember syncMember(String realm, String characterName) {
        String rawJson = blizzardApiService.getCharacter(realm, characterName)
                .block();
        JsonNode root = objectMapper.readTree(rawJson);
        // como pegar o guildRank sem sofrer
        String title = root.get("active_title").get("name").asString();
        String gender = root.get("gender").get("name").asString();
        String faction = root.get("faction").get("name").asString();
        String race = root.get("race").get("name").asString();
        String characterClass = root.get("character_class").get("name").asString();
        String activeSpecialization = root.get("active_spec").get("name").asString();
        Integer level = root.get("level").asInt();

        String mythicHref = root.get("mythic_keystone_profile").get("href").asString();
        MythicPlusProfile mythicPlusProfile = syncMythicPlusProfile(realm, characterName, mythicHref);
        // PVPSummary = syncPVPSummary
        // Achievments = syncAchievments
        // Encounters = syncEncounters
        // etc etc


        return GuildMember.builder()
                .name(characterName)
                .realm(realm)
                .activeTitle(title)
                .gender(gender)
                .faction(faction)
                .race(race)
                .characterClass(characterClass)
                .activeSpecialization(activeSpecialization)
                .level(level)
                .build();
    }

    private MythicPlusProfile syncMythicPlusProfile(String realm, String characterName, String href) {
        try {

            String rawJson = blizzardApiService.getDataByHref(href)
                    .block();
            JsonNode root = objectMapper.readTree(rawJson);
            Double currentMythicRating = root.get("current_mythic_rating").get("rating").asDouble();

            TreeMap<String, Double> ratingColor = new TreeMap<>();
            JsonNode colors = root.get("current_mythic_rating").get("color");
            if (colors != null) {
                for (JsonNode color : colors) {
                    ratingColor.put(color.get(0).asString(), color.get(1).asDouble()); // ACHO que vai funfar
                }
            }

            List<String> seasonsURL = new ArrayList<>();
            JsonNode seasons = root.get("seasons");
            if (seasons != null) {
                for (JsonNode season : seasons) {
                    seasonsURL.add(season.get("key").get("href").asString());

                }
            }
            List<MythicSeason> seasonList = fetchMythicSeasons(seasonsURL);
            return MythicPlusProfile.builder()
                    .currentMythicRating(currentMythicRating)
                    .ratingColor(ratingColor)
                    .seasonsURL(seasonsURL)
                    .seasons(new ArrayList<>()) // vazia pq nem sei como vou fazer isso
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("faiou synca o mythic plus profile " + e.getMessage());
        }

    }

    private List<MythicSeason> fetchMythicSeasons(List<String> seasonsURL) {
        List<String> rawJsonList = blizzardApiService.getAllSeasons(seasonsURL).block();
        return rawJsonList.stream()
                .map(rawJson ->
                {
                    try {
                        JsonNode root = objectMapper.readTree(rawJson);
                        return parseSeason(root);
                    } catch (Exception e) {
                        throw new RuntimeException("faiou grabbar as season padrinho " + e.getMessage());
                    }
                })
                .collect(Collectors.toList());

    }

    private MythicSeason parseSeason(JsonNode season) {
        try {
            String seasonURL = season.get("season_url").get("key").get("href").asString();
            String rawSeasonDataJson = blizzardApiService.getDataByHref(seasonURL).block();

            JsonNode rootSeasonData = objectMapper.readTree(rawSeasonDataJson);
            String seasonName = rootSeasonData.get("season_name").get("en_US").asString();
            Double startTime = rootSeasonData.get("start_timestamp").asDouble();
            Double endTime = rootSeasonData.get("end_timestamp").asDouble();

            Double seasonRating = season.get("mythic_rating").get("rating").asDouble();

            TreeMap<String, Double> ratingColor = new TreeMap<>();
            JsonNode colors = season.get("mythic_rating").get("color");
            if (colors != null) {
                for (JsonNode color : colors) {
                    ratingColor.put(color.get(0).asString(), color.get(1).asDouble()); // ACHO que vai funfar
                }
            }

            JsonNode bestRuns = season.path("best_runs");
            List<KeystoneRun> keystoneRuns = new ArrayList<>();
            if (bestRuns.isArray()) {
                for (JsonNode run : bestRuns) {
                    keystoneRuns.add(parseRun(run));
                }
            }

            return MythicSeason.builder()
                    .seasonName(seasonName)
                    .seasonDataURL(seasonURL)
                    .startTime(startTime)
                    .endTime(endTime)
                    .bestRuns(keystoneRuns)
                    .seasonRating(seasonRating)
                    .ratingColor(ratingColor)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("faiou grabbar as season url " + e.getMessage());
        }
    }

    private KeystoneRun parseRun(JsonNode run) {
        try {
            Double completedTimeStamp = run.get("completed_timestamp").asDouble();
            Double runDuration = run.get("duration").asDouble();
            Integer keystoneLevel = run.get("keystone_level").asInt();

            JsonNode affixRoot = run.path("keystone_affixes");
            List<String> affixesName = new ArrayList<>();
            if (affixRoot.isArray()) {
                for (JsonNode affix : affixRoot) {
                    affixesName.add(affix.get("name").get("en_US").asString());
                }
            }

            List<KeystoneMember> keystoneMembers = new ArrayList<>();
            JsonNode members = run.path("members");
            if (members.isArray()) {
                for (JsonNode member : members) {
                    keystoneMembers.add(parseRunMembers(member));
                }
            }

            String dungeonName = run.get("dungeon").get("name").get("en_US").asString();
            Boolean isTimed = run.get("is_completed_within_timer").asBoolean();
            Double runRating = run.get("mythic_rating").get("rating").asDouble();

            TreeMap<String, Double> ratingColor = new TreeMap<>();
            JsonNode colors = run.get("mythic_rating").get("color");
            if (colors != null) {
                for (JsonNode color : colors) {
                    ratingColor.put(color.get(0).asString(), color.get(1).asDouble()); // ACHO que vai funfar
                }
            }

            return KeystoneRun.builder()
                    .completedTimestamp(completedTimeStamp)
                    .duration(runDuration)
                    .level(keystoneLevel)
                    .affixesName(affixesName)
                    .members(keystoneMembers)
                    .dungeonName(dungeonName)
                    .isTimed(isTimed)
                    .ratingColor(ratingColor)
                    .dungeonMythicRating(runRating)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("faiou popula as keystone run " + e.getMessage());
        }

    }

    private KeystoneMember parseRunMembers(JsonNode member) {
        try {
            return KeystoneMember.builder()


                    .build();
        } catch (Exception e) {
            throw new RuntimeException("faiou popula os membro " + e.getMessage());
        }
    }
}