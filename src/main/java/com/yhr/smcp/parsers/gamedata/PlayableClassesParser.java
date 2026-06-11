package com.yhr.smcp.parsers.gamedata;

import com.yhr.smcp.entities.gamedata.PlayableClass;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class PlayableClassesParser {
    public PlayableClass parse(JsonNode classNode) {
        try {
            String name = classNode.path("name").asString();
            Integer classId = classNode.path("id").asInt();

            return PlayableClass.builder()
                    .id(classId)
                    .name(name)
                    .build();
        } catch (Exception e) {
            throw new BlizzardParsingException("PlayableClass", "name=" + classNode.path("name").asString(), e);
        }


    }

}
