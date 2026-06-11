package com.yhr.smcp.parsers.mythicplus;

import com.yhr.smcp.entities.character.MythicPlusProfile;
import com.yhr.smcp.entities.character.mythicplus.MythicSeason;
import com.yhr.smcp.exceptions.BlizzardParsingException;
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

    public MythicPlusProfile buildProfile(JsonNode profileRoot, List<JsonNode> seasonNodes, Map<String, String> specClassMap) {
        try {
            List<MythicSeason> mythicSeasons = new ArrayList<>();
            mythicSeasons = seasonNodes.stream()
                    .map(season -> mythicSeasonParser.parse(season, specClassMap))
                    .toList();

            Double currentMythicRating = profileRoot.path("current_mythic_rating")
                    .path("rating").asDouble();
            TreeMap<String, Double> ratingColor = new TreeMap<>();
            ratingColor = RatingColors.ratingColorParserUtil(profileRoot.path("current_mythic_rating")
                    .path("color"));

            return MythicPlusProfile.builder()
                    .currentMythicRating(currentMythicRating)
                    .ratingColor(ratingColor)
                    .seasons(mythicSeasons)
                    .build();

        } catch (BlizzardParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new BlizzardParsingException("MythicPlusProfileParser", "profile=" + profileRoot.path("character")
                    .path("name"), e);
        }
    }

}
