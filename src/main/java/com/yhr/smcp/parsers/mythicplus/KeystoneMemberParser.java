package com.yhr.smcp.parsers.mythicplus;

import com.yhr.smcp.entities.character.mythicplus.KeystoneMember;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class KeystoneMemberParser {
    public KeystoneMember parse(JsonNode member) {
        try {
            String name = member.path("character").path("name").asString();
            String realm = member.path("character").path("realm").path("slug").asString();
            String race = member.path("race").path("name").asString();
            Double itemLevel = member.path("equipped_item_level").asDouble();
            Integer specId = member.path("specialization").path("id").asInt();

            return KeystoneMember.builder()
                    .characterName(name)
                    .realm(realm)
                    .specializationId(specId)
                    .race(race)
                    .itemLevel(itemLevel)
                    .build();
        } catch (Exception e) {
            throw new BlizzardParsingException("KeystoneMember", "member=" + member.path("character").path("name").asString(), e);
        }
    }
}
