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

        GuildMember guildMember = GuildMember.builder()
                .name(characterName)
                .realm(realm)
                .activeTitle(title)
                .gender(gender)
                .faction(faction)
                .race(race)
                .characterClass(characterClass)
                .activeSpecialization(activeSpecialization)
                .level(level)
                .mythicPlusProfile(mythicPlusProfile)
                .build();
        // return memberRepository.save(guildMember);
        return guildMember;
    }

    private MythicPlusProfile syncMythicPlusProfile(String realm, String characterName, String href) {
        try {
            String rawJson = blizzardApiService.getDataByHref(href)
                    .block();
            JsonNode root = objectMapper.readTree(rawJson);
            Double currentMythicRating = root.path("current_mythic_rating").path("rating").asDouble();

            TreeMap<String, Double> ratingColor = new TreeMap<>();
            JsonNode colors = root.path("current_mythic_rating").path("color"); //TODO: fazer um metodo pra isso, repito 3x aqui
            if (!colors.isMissingNode()) {
                ratingColor.put("r", colors.path("r").asDouble());
                ratingColor.put("g", colors.path("g").asDouble());
                ratingColor.put("b", colors.path("b").asDouble());
                ratingColor.put("a", colors.path("a").asDouble());
            }

            List<String> seasonsURL = new ArrayList<>();
            JsonNode seasons = root.path("seasons");
            if (!seasons.isMissingNode()) {
                for (JsonNode season : seasons) {
                    String seasonHref = season.path("key").path("href").asString();
                    if (seasonHref != null && !seasonHref.isEmpty()) {
                        seasonsURL.add(seasonHref);
                    }

                }
            }
            List<MythicSeason> seasonList = fetchMythicSeasons(seasonsURL);
            return MythicPlusProfile.builder()
                    .currentMythicRating(currentMythicRating)
                    .ratingColor(ratingColor)
                    .seasonsURL(seasonsURL)
                    .seasons(seasonList)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("faiou synca o mythic plus profile " + e.getMessage(), e);
        }

    }

    private List<MythicSeason> fetchMythicSeasons(List<String> seasonsURL) {
        List<String> rawJsonList = blizzardApiService.getAllSeasons(seasonsURL).block();
        return rawJsonList.stream() //TODO: checar null
                .map(rawJson -> {
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
            String seasonURL = season.path("season").path("key").path("href").asString();
            String rawSeasonDataJson = blizzardApiService.getDataByHref(seasonURL).block();

            JsonNode rootSeasonData = objectMapper.readTree(rawSeasonDataJson);
            String seasonName = rootSeasonData.path("season_name").path("en_US").asString();
            Double startTime = rootSeasonData.path("start_timestamp").asDouble();
            Double endTime = rootSeasonData.path("end_timestamp").asDouble();

            Double seasonRating = season.path("mythic_rating").path("rating").asDouble();

            TreeMap<String, Double> ratingColor = new TreeMap<>();
            JsonNode colors = season.path("mythic_rating").path("color");
            if (colors != null) {
                ratingColor.put("r", colors.path("r").asDouble());
                ratingColor.put("g", colors.path("g").asDouble());
                ratingColor.put("b", colors.path("b").asDouble());
                ratingColor.put("a", colors.path("a").asDouble());
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
            Double completedTimeStamp = run.path("completed_timestamp").asDouble();
            Double runDuration = run.path("duration").asDouble();
            Integer keystoneLevel = run.path("keystone_level").asInt();

            JsonNode affixRoot = run.path("keystone_affixes");
            List<String> affixesName = new ArrayList<>(); //TODO: metodo pra isso
            if (affixRoot.isArray()) {
                for (JsonNode affix : affixRoot) {
                    affixesName.add(affix.path("name").path("en_US").asString());
                }
            }

            List<KeystoneMember> keystoneMembers = new ArrayList<>();
            JsonNode members = run.path("members");
            if (members.isArray()) {
                for (JsonNode member : members) {
                    keystoneMembers.add(parseRunMembers(member));
                }
            }

            String dungeonName = run.path("dungeon").path("name").path("en_US").asString();
            Boolean isTimed = run.path("is_completed_within_timer").asBoolean();
            Double runRating = run.path("mythic_rating").get("rating").asDouble();

            TreeMap<String, Double> ratingColor = new TreeMap<>();
            JsonNode colors = run.path("mythic_rating").get("color");
            if (colors != null) {
                ratingColor.put("r", colors.path("r").asDouble());
                ratingColor.put("g", colors.path("g").asDouble());
                ratingColor.put("b", colors.path("b").asDouble());
                ratingColor.put("a", colors.path("a").asDouble());
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
            String name = member.path("character").path("name").asString();
            String realm = member.path("character").path("realm").path("slug").asString();
            String spec = member.path("specialization").path("name").path("en_US").asString();
            String race = member.path("race").path("name").path("en_US").asString();

            String playableClassURL = member.path("specialization").path("key").path("href").asString();
            JsonNode specializationDataRoot = objectMapper.readTree(blizzardApiService.getDataByHref(playableClassURL).block());
            String playableClass = specializationDataRoot.path("playable_class").path("name").path("en_US").asString();

            Double itemLevel = member.path("equipped_item_level").asDouble();

            return KeystoneMember.builder()
                    .characterName(name)
                    .realm(realm)
                    .specializationName(spec)
                    .playableClass(playableClass)
                    .race(race)
                    .itemLevel(itemLevel)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("faiou popula os membro " + e.getMessage());
        }
    }

    public GuildMember getGuildMember(String realm, String characterName) {
        return memberRepository.findByNameAndRealm(realm, characterName).orElse(null);
    }


}