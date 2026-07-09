package com.yhr.smcp.parsers.gamedata;

import com.yhr.smcp.entities.gamedata.character.PlayableClass;
import com.yhr.smcp.entities.gamedata.character.PlayableSpecialization;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayableSpecializationsParser {
    public List<PlayableSpecialization> parse(JsonNode classNode, PlayableClass playableClass) {
        List<PlayableSpecialization> playableSpecializations = new ArrayList<>();
        try {
            classNode.path("specializations").forEach(specNode -> {
                String name = specNode.path("name").asString();
                Integer id = specNode.path("id").asInt();
                playableSpecializations.add(PlayableSpecialization.builder()
                        .name(name)
                        .id(id)
                        .playableClass(playableClass)
                        .build());
            });
            return playableSpecializations;
        } catch (Exception e) {
            throw new BlizzardParsingException("PlayableSpecialization", "name=" + classNode.path("specializations")
                    .path("name").asString(), e);
        }

    }
}
