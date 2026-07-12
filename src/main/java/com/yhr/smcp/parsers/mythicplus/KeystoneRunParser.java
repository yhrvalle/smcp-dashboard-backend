package com.yhr.smcp.parsers.mythicplus;

import com.yhr.smcp.entities.character.mythicplus.KeystoneMember;
import com.yhr.smcp.entities.character.mythicplus.KeystoneRun;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import com.yhr.smcp.util.mythic.RatingColors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class KeystoneRunParser {
    private final KeystoneMemberParser keystoneMemberParser;

    public KeystoneRun parse(JsonNode run) {
        try {
            List<KeystoneMember> members = new ArrayList<>();
            run.path("members").forEach(member -> {
                members.add(keystoneMemberParser.parse(member));
            });

            List<Integer> affixesId = new ArrayList<>();
            run.path("keystone_affixes").forEach(affix -> {
                JsonNode affixId = affix.path("id");
                if (affixId.isMissingNode()) {
                    return;
                }
                affixesId.add(affixId.asInt());
            });

            long completedTimestampMilli = run.path("completed_timestamp").asLong();
            long runDurationMilli = run.path("duration").asLong();
            Instant completedTimestamp = Instant.ofEpochMilli(completedTimestampMilli);
            Instant runDuration = Instant.ofEpochMilli(runDurationMilli); //BUG: esta errado n é uma data é duracao

            Integer keystoneLevel = run.path("keystone_level").asInt();
            String dungeonName = run.path("dungeon").path("name").asString();
            Boolean isTimed = run.path("is_completed_within_time").asBoolean();
            Double runRating = run.path("mythic_rating").path("rating").asDouble();
            JsonNode colors = run.path("mythic_rating").path("color");

            String ratingColors = RatingColors.ratingColorParserUtil(colors);

            return KeystoneRun.builder()
                    .completedTimestamp(completedTimestamp)
                    .duration(runDuration)
                    .level(keystoneLevel)
                    .affixIds(affixesId)
                    .members(members)
                    .dungeonName(dungeonName)
                    .isTimed(isTimed)
                    .ratingColor(ratingColors)
                    .dungeonMythicRating(runRating)
                    .build();

        } catch (BlizzardParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new BlizzardParsingException("KeystoneRun", "dungeon= " +
                    run.path("dungeon").path("name").asString(), e);
        }
    }

}
