package com.yhr.smcp.parsers.mythicplus;

import com.yhr.smcp.entities.character.MythicPlusProfile;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import com.yhr.smcp.util.mythic.RatingColors;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.TreeMap;

@Component
public class MythicPlusProfileParser {

    public MythicPlusProfile buildProfile(JsonNode profileRoot) {
        try {
            Double currentMythicRating = profileRoot.path("current_mythic_rating")
                    .path("rating").asDouble();
            TreeMap<String, Double> ratingColor = new TreeMap<>();
            ratingColor = RatingColors.ratingColorParserUtil(profileRoot.path("current_mythic_rating")
                    .path("color"));

            return MythicPlusProfile.builder()
                    .currentMythicRating(currentMythicRating)
                    .ratingColor(ratingColor)
                    .build();
        } catch (BlizzardParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new BlizzardParsingException("MythicPlusProfileParser", "profile=" + profileRoot.path("character")
                    .path("name"), e);
        }
    }
}
