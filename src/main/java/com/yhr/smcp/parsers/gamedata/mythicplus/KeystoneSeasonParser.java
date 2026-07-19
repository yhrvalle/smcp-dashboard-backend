package com.yhr.smcp.parsers.gamedata.mythicplus;

import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneSeason;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.time.Instant;

@Component
public class KeystoneSeasonParser {
    public KeystoneSeason parse(JsonNode seasonNode) {
        try {
            Long id = seasonNode.path("id").asLong();

            long startMilliSeconds = seasonNode.path("start_timestamp").asLong();
            Instant startTimestamp = Instant.ofEpochMilli(startMilliSeconds);

            Instant endTimestamp = null;
            if (!seasonNode.path("end_timestamp").isMissingNode()) {
                long endMillisSeconds = seasonNode.path("end_timestamp").asLong();
                endTimestamp = Instant.ofEpochMilli(endMillisSeconds);
            }

            String name = seasonNode.path("season_name").asString("blizzard api sucks and this field was null");
            return KeystoneSeason.builder()
                    .id(id)
                    .startTimestamp(startTimestamp)
                    .endTimestamp(endTimestamp)
                    .name(name)
                    .build();
        } catch (Exception e) {
            throw new BlizzardParsingException("KeystoneSeasonParser", "id=" + seasonNode.path("id").asString(), e);
        }
    }
}