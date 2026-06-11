package com.yhr.smcp.parsers.gamedata;

import com.yhr.smcp.entities.gamedata.KeystoneAffix;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class KeystoneAffixParser {
    public KeystoneAffix parse(JsonNode affixNode) {
        try {
            Integer affixId = affixNode.path("id").asInt();
            String affixName = affixNode.path("name").asString();
            String affixDescription = affixNode.path("description").asString();
            return KeystoneAffix.builder()
                    .affixId(affixId)
                    .affixName(affixName)
                    .affixDescription(affixDescription)
                    .build();
        } catch (Exception e) {
            throw new BlizzardParsingException("KeystoneAffix", "name=" + affixNode.path("name").asString(), e);
        }

    }
}
