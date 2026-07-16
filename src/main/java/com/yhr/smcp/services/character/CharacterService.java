package com.yhr.smcp.services.character;

import com.yhr.smcp.entities.character.CharacterProfile;
import com.yhr.smcp.entities.character.mythicplus.MythicPlusProfile;
import com.yhr.smcp.entities.guild.GuildMember;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import com.yhr.smcp.exceptions.BlizzardSyncException;
import com.yhr.smcp.parsers.character.CharacterParser;
import com.yhr.smcp.repositories.character.CharacterRepository;
import com.yhr.smcp.repositories.guild.GuildMemberRepository;
import com.yhr.smcp.client.BlizzardApiService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class CharacterService {
    private final BlizzardApiService blizzardApiService;
    private final ObjectMapper objectMapper;

    private final GuildMemberRepository guildMemberRepository;
    private final CharacterRepository characterRepository;
    private final CharacterParser characterParser;

    private final MythicPlusService mythicPlusService;

    @Transactional
    public CharacterProfile syncCharacter(String realm, String characterName) {
        try {
            GuildMember guildMember = guildMemberRepository.findByRealmAndNameIgnoreCase(realm, characterName)
                    .orElseThrow(() -> new BlizzardSyncException("guild member not found in guild, name=%s at %s"
                            .formatted(characterName, realm), null));

            String rawJson = fetchCharacterProfile(realm, characterName);
            JsonNode characterRoot = objectMapper.readTree(rawJson);
            CharacterProfile parsedProfile = characterParser.parse(characterRoot);

            Long existingProfileId = characterRepository.findMythicPlusProfileIdById(guildMember.getId()).orElse(null);
            MythicPlusProfile mythicPlusProfile = mythicPlusService.syncProfile(realm, characterName, existingProfileId);

            return saveCharacterProfile(guildMember, parsedProfile, mythicPlusProfile);

        } catch (BlizzardSyncException e) {
            log.error("failed to sync character profile name={} at {}", characterName, realm, e);
            throw e;
        } catch (BlizzardParsingException e) {
            log.error("failed to parse character profile name={} at {}", characterName, realm, e);
            throw e;
        } catch (DataAccessException e) {
            log.error("failed to save character profile name={} at {}", characterName, realm, e);
            throw e;
        } catch (Exception e) {
            throw new BlizzardSyncException("failed to sync character profile name=%s at %s".formatted(characterName, realm), e);
        }

    }

    private CharacterProfile saveCharacterProfile(GuildMember guildMember, CharacterProfile parsed, MythicPlusProfile mythicPlusProfile) {
        CharacterProfile characterProfile = characterRepository.findById(guildMember.getId())
                .orElseGet(CharacterProfile::new);

        characterProfile.setActiveTitle(parsed.getActiveTitle());
        characterProfile.setGender(parsed.getGender());
        characterProfile.setFaction(parsed.getFaction());
        characterProfile.setActiveSpecializationId(parsed.getActiveSpecializationId());
        characterProfile.setItemLevel(parsed.getItemLevel());

        characterProfile.setMythicPlusProfile(mythicPlusProfile);
        characterProfile.setGuildMember(guildMember);

        return characterRepository.save(characterProfile);
    }

    private String fetchCharacterProfile(String realm, String characterName) {
        try {
            return blizzardApiService.getCharacter(realm, characterName).block();
        } catch (Exception e) {
            throw new BlizzardSyncException("failed to sync character profile name=%s at %s".formatted(characterName, realm), e);
        }

    }
}