package com.yhr.smcp.parsers.gamedata.character;

import com.yhr.smcp.entities.gamedata.character.PlayableRace;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class PlayableRaceParser {
    public PlayableRace parse(JsonNode raceRoot) {
        Integer id = raceRoot.get("id").asInt();
        String name = raceRoot.get("name").asString();
        return PlayableRace.builder()
                .id(id)
                .name(name)
                .build();
    }
}
