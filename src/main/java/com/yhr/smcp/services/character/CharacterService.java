package com.yhr.smcp.services.character;

import com.yhr.smcp.entities.character.CharacterProfile;
import com.yhr.smcp.entities.character.mythicplus.MythicPlusProfile;
import com.yhr.smcp.entities.guild.GuildMember;
import com.yhr.smcp.parsers.character.CharacterParser;
import com.yhr.smcp.repositories.character.CharacterRepository;
import com.yhr.smcp.services.BlizzardApiService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
public class CharacterService {
    private final CharacterParser characterParser;
    private final CharacterRepository characterRepository;
    private final BlizzardApiService blizzardApiService;
    private final MythicPlusService mythicPlusService;
    private final ObjectMapper objectMapper;

    public void syncCharacter(GuildMember guildMember) {
        String realm = guildMember.getRealm();
        String characterName = guildMember.getName();
        String rawJson = blizzardApiService.getCharacter(realm, characterName).block();
        JsonNode characterRoot = objectMapper.readTree(rawJson);
        CharacterProfile characterProfile = characterParser.parse(characterRoot);
        MythicPlusProfile mythicPlusProfile = mythicPlusService.syncProfile(realm, characterName);
        characterProfile.setMythicPlusProfile(mythicPlusProfile);
        characterProfile.setGuildMember(guildMember);
        characterRepository.save(characterProfile);
    }

}