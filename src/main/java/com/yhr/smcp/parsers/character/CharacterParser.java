package com.yhr.smcp.parsers.character;

import com.yhr.smcp.entities.character.CharacterProfile;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class CharacterParser {
    public CharacterProfile parse(JsonNode character) {
        try {
            String title = character.path("active_title").path("name").asString(null);
            String gender = character.path("gender").path("name").asString();
            String faction = character.path("faction").path("name").asString();
            Integer specId = character.path("active_spec").path("id").asInt();
            Long equippedItemLevel = character.path("equipped_item_level").asLong();
            return CharacterProfile.builder()
                    .activeTitle(title)
                    .gender(gender)
                    .faction(faction)
                    .activeSpecializationId(specId)
                    .itemLevel(equippedItemLevel)
                    .build();
        } catch (Exception e) {
            throw new BlizzardParsingException("CharacterProfile", "character=" + character.path("name"), e);
        }
    }
}
