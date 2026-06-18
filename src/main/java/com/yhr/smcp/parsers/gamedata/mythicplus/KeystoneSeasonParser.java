package com.yhr.smcp.parsers.gamedata.mythicplus;

import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneSeason;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class KeystoneSeasonParser {
    public KeystoneSeason parse(JsonNode seasonNode) {
        try {
            Integer id = seasonNode.path("id").asInt();
            Double startTimestamp = seasonNode.path("start_timestamp").asDouble();
            Double endTimestamp = seasonNode.path("end_timestamp").asDouble();
            String name = seasonNode.path("season_name").asString("blizzard api sucks and this field was null");
            return KeystoneSeason.builder()
                    .id(id)
                    .startTimestamp(startTimestamp)
                    .endTimestamp(endTimestamp)
                    .name(name)
                    .build();
        } catch (Exception e) {
            throw new BlizzardParsingException("KeystoneSeasonParser", "id=" + seasonNode.path("id") + e.getMessage(), e);
        }
    }
}