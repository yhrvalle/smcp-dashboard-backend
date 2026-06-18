package com.yhr.smcp.parsers.mythicplus;

import com.yhr.smcp.entities.character.mythicplus.KeystoneMember;
import com.yhr.smcp.entities.gamedata.PlayableSpecialization;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.Map;

@Component
public class KeystoneMemberParser {
    public KeystoneMember parse(JsonNode member, Map<Integer, PlayableSpecialization> specClassMap) {
        try {

            String name = member.path("character").path("name").asString();
            String realm = member.path("character").path("realm").path("slug").asString();
            String spec = member.path("specialization").path("name").path("en_US").asString();
            String race = member.path("race").path("name").path("en_US").asString();
            Double itemLevel = member.path("equipped_item_level").asDouble();
            String specURL = member.path("specialization").path("key").path("href").asString();
            //String playableClass = specClassMap.getOrDefault(specURL, "Unknown");
            return KeystoneMember.builder()
                    .characterName(name)
                    .realm(realm)
                    //.specializationName(spec)
                    //.playableClass(playableClass)
                    .race(race)
                    .itemLevel(itemLevel)
                    .build();
        } catch (Exception e) {
            throw new BlizzardParsingException("KeystoneMember", "member=" + member.path("character").path("name").asString(), e);
        }
    }
}
