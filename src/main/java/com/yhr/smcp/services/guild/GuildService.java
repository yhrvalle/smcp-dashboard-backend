package com.yhr.smcp.services.guild;

import com.yhr.smcp.entities.guild.Guild;
import com.yhr.smcp.entities.guild.GuildMember;
import com.yhr.smcp.parsers.guild.GuildParser;
import com.yhr.smcp.parsers.guild.GuildRosterParser;
import com.yhr.smcp.repositories.guild.GuildMemberRepository;
import com.yhr.smcp.repositories.guild.GuildRepository;
import com.yhr.smcp.services.BlizzardApiService;
import com.yhr.smcp.services.character.CharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuildService {
    private final GuildRepository guildRepository;
    private final GuildMemberRepository guildMemberRepository;
    private final CharacterService characterService;
    private final BlizzardApiService blizzardApiService;
    private final ObjectMapper objectMapper;
    private final GuildParser guildParser;
    private final GuildRosterParser guildRosterParser;

    @Value("${guild.roster.max_rank}")
    private Integer maxRank;

    public Guild syncGuild(String realm, String guildSlug) {
        String rawJson = blizzardApiService.getGuild(realm, guildSlug).block();
        JsonNode guildRoot = objectMapper.readTree(rawJson);
        Guild guild = guildParser.parse(guildRoot);
        guildRepository.save(guild);
        syncGuildRoster(realm, guildSlug, guild);
        return guild;
    }

    private void syncGuildRoster(String realm, String guildSlug, Guild guild) {
        String rawJson = blizzardApiService.getGuildRoster(realm, guildSlug).block();
        JsonNode rosterRoot = objectMapper.readTree(rawJson);
        List<GuildMember> guildMembers = guildRosterParser.parse(rosterRoot, maxRank);
        for (GuildMember guildMember : guildMembers) {
            if (guildMemberRepository.existsById(guildMember.getId())) {
                continue;
            }
            guildMember.setGuild(guild);
            guildMemberRepository.save(guildMember);
            characterService.syncCharacter(guildMember);
        }

    }

}
