package com.yhr.smcp.parsers.mythicplus;

import com.yhr.smcp.entities.character.mythicplus.MythicPlusProfile;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import com.yhr.smcp.util.mythic.RatingColors;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class MythicPlusProfileParser {

    public MythicPlusProfile parse(JsonNode profileRoot) {
        try {
            Double currentMythicRating = profileRoot.path("current_mythic_rating")
                    .path("rating").asDouble();

            String ratingColor = RatingColors.ratingColorParserUtil(profileRoot.path("current_mythic_rating")
                    .path("color"));

            return MythicPlusProfile.builder()
                    .currentMythicRating(currentMythicRating)
                    .ratingColor(ratingColor)
                    .build();
        } catch (BlizzardParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new BlizzardParsingException("MythicPlusProfileParser", "profile=" + profileRoot.path("character")
                    .path("name").asString(), e);
        }
    }
}
