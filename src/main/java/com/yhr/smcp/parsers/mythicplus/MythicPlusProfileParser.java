package com.yhr.smcp.parsers.mythicplus;

import com.yhr.smcp.entities.character.MythicPlusProfile;
import com.yhr.smcp.entities.character.mythic.MythicSeason;
import com.yhr.smcp.util.mythic.RatingColors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
@AllArgsConstructor
public class MythicPlusProfileParser {
    private final MythicSeasonParser mythicSeasonParser;

    public MythicPlusProfile parse(JsonNode profileRoot, List<JsonNode> seasonNodes, Map<String, String> specClassMap) {
        List<MythicSeason> mythicSeasons = new ArrayList<>();
        mythicSeasons = seasonNodes.stream()
                .map(season -> mythicSeasonParser.parse(season, specClassMap))
                .toList();

        Double currentMythicRating = profileRoot.path("current_mythic_rating").path("rating").asDouble();
        TreeMap<String, Double> ratingColor = new TreeMap<>();
        ratingColor = RatingColors.ratingColorParserUtil(profileRoot.path("current_mythic_rating").path("color"));

        return MythicPlusProfile.builder()
                .currentMythicRating(currentMythicRating)
                .ratingColor(ratingColor)
                .seasons(mythicSeasons)
                .build();
    }

}
