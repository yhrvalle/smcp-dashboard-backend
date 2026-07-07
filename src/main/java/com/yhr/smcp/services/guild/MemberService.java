package com.yhr.smcp.services.guild;

import com.yhr.smcp.entities.guild.GuildMember;
import com.yhr.smcp.entities.character.MythicPlusProfile;
import com.yhr.smcp.repositories.MemberRepository;
import com.yhr.smcp.services.BlizzardApiService;
import com.yhr.smcp.services.MythicPlusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
public class MemberService {
    // private static final Duration CACHE_TTL = Duration.ofHours(1);

    private final MemberRepository memberRepository;
    private final BlizzardApiService blizzardApiService;
    private final MythicPlusService mythicPlusService;
    private final ObjectMapper objectMapper;

    public GuildMember syncMember(String realm, String characterName) {
        String rawJson = blizzardApiService.getCharacter(realm, characterName)
                .block();
        JsonNode root = objectMapper.readTree(rawJson);
        // como pegar o guildRank sem sofrer
        // tacar isso num parser
        // TODO: trocar para .path e refactor
        String title = root.get("active_title").get("name").asString();
        String gender = root.get("gender").get("name").asString();
        String faction = root.get("faction").get("name").asString();
        String race = root.get("race").get("name").asString();
        String characterClass = root.get("character_class").get("name").asString();
        String activeSpecialization = root.get("active_spec").get("name").asString();
        Integer level = root.get("level").asInt();

        MythicPlusProfile mythicPlusProfile = mythicPlusService.syncProfile(characterName, realm);
        // PVPSummary = syncPVPSummary
        // Achievments = syncAchievments
        // Encounters = syncEncounters
        // etc etc

        GuildMember guildMember = GuildMember.builder()
                .name(characterName)
                .realm(realm)
                .activeTitle(title)
                .gender(gender)
                .faction(faction)
                .race(race)
                .characterClass(characterClass)
                .activeSpecialization(activeSpecialization)
                .level(level)
                .mythicPlusProfile(mythicPlusProfile)
                .build();
        // return memberRepository.save(guildMember);
        return guildMember;
    }

}