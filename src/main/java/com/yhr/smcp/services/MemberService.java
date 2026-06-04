package com.yhr.smcp.services;

import com.yhr.smcp.entities.GuildMember;
import com.yhr.smcp.entities.character.MythicPlusProfile;
import com.yhr.smcp.repositories.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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



        return MythicPlusProfile.builder()
                .currentMythicRating(currentMythicRating)
                .ratingColor(ratingColor)
                .seasonsURL(seasonsURL)
                .
                .build();
    }


//    private MythicPlusProfile parseMythicProfile(String rawJson) {
//        try {
//            JsonNode root = objectMapper.readTree(rawJson);
//            Double currentMythicRating = root.get("current_mythic_rating").get("rating").asDouble();
//            TreeMap<String, Double> ratingRGB = new TreeMap<>();
//            JsonNode RatingColorNode = root.get("current_mythic_rating").get("colors");
//            ratingRGB.put("r", RatingColorNode.get("r").asDouble());
//            ratingRGB.put("g", RatingColorNode.get("g").asDouble());
//            ratingRGB.put("b", RatingColorNode.get("b").asDouble());
//            ratingRGB.put("a", RatingColorNode.get("a").asDouble());
//
//            List<String> seasonsURL = new ArrayList<>();
//            List<MythicSeason> seasons = new ArrayList<>();
//
//            int currentSeasonId = -1;
//            String currentSeasonURL = null;
//            JsonNode seasonsNode = root.get("seasons");
//            if (seasonsNode != null) {
//                for (JsonNode season : seasonsNode) {
//                    String seasonHref = season.get("key").get("href").asString(); // URL das seasons
//                    int seasonId = season.get("id").asInt();
//                    seasonsURL.add(seasonHref);
//
//                    if (seasonId > currentSeasonId) {
//                        currentSeasonId = seasonId;
//                        currentSeasonURL = seasonHref;
//                    }
//                }
//            }
//            MythicSeason currentMythicSeason = fetchCurrentSeason(currentSeasonURL);
//
//            return MythicPlusProfile.builder()
//                    .currentMythicRating(currentMythicRating)
//                    .ratingColor(ratingRGB)
//                    .seasonsURL(seasonsURL)
//
//                    .build();
//        } catch (Exception e) {
//            throw new RuntimeException("faiou de pegar o mythic profile");
//        }
//    }
//
//    private MythicSeason fetchCurrentSeason(String href) {
//        String rawJson = blizzardApiService.getSeasonByHref(href)
//                .block();
//        try {
//            JsonNode root = objectMapper.readTree(rawJson);
//
//        }
//
//    }
}
