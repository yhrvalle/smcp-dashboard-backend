package com.yhr.smcp.parsers.mythicplus;

import com.yhr.smcp.entities.character.mythicplus.KeystoneMember;
import com.yhr.smcp.entities.character.mythicplus.KeystoneRun;
import com.yhr.smcp.entities.gamedata.PlayableSpecialization;
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
public class KeystoneRunParser {
    private final KeystoneMemberParser keystoneMemberParser;

    public KeystoneRun parse(JsonNode run, Map<Integer, PlayableSpecialization> specClassMap) {
        try {
            List<KeystoneMember> members = new ArrayList<>();
            run.path("members").forEach(member -> {
                members.add(keystoneMemberParser.parse(member, specClassMap));
            });

            List<String> affixesNames = new ArrayList<>();
            run.path("affixes").forEach(affixes -> {
                affixesNames.add(affixes.path("name").path("en_US").asString());
            });

            Double completedTimestamp = run.path("completed_timestamp").asDouble();
            Double runDuration = run.path("duration").asDouble();
            Integer keystoneLevel = run.path("keystone_level").asInt();

            String dungeonName = run.path("dungeon_name").path("name").path("en_US").asString();
            Boolean isTimed = run.path("is_completed_within_timer").asBoolean();
            Double runRating = run.path("mythic_rating").asDouble();

            JsonNode colors = run.path("mythic_rating").path("colors");
            TreeMap<String, Double> colorsMap = new TreeMap<>();
            colorsMap = RatingColors.ratingColorParserUtil(colors);

            return KeystoneRun.builder()
                    .completedTimestamp(completedTimestamp)
                    .duration(runDuration)
                    .level(keystoneLevel)
                    //.affixesName(affixesNames)
                    .members(members)
                    .dungeonName(dungeonName)
                    .isTimed(isTimed)
                    .ratingColor(colorsMap)
                    .dungeonMythicRating(runRating)
                    .build();

        } catch (BlizzardParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new BlizzardParsingException("KeystoneRun", "dungeon= " + run.path("dungeon_name").path("name").path("en_US").asString(), e);
        }
    }

}
