package com.yhr.smcp.parsers.gamedata.character;

import com.yhr.smcp.entities.gamedata.character.PlayableRace;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class PlayableRaceParser {
    public PlayableRace parse(JsonNode raceRoot) {
        try {
            Integer id = raceRoot.path("id").asInt();
            String name = raceRoot.path("name").asString();
            return PlayableRace.builder()
                    .id(id)
                    .name(name)
                    .build();
        } catch (Exception e) {
            throw new BlizzardParsingException("PlayableRaceParser", "name=" + raceRoot.path("name").asString(), e);
        }
    }
}
