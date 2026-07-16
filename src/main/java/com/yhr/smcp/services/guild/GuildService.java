package com.yhr.smcp.services.guild;

import com.yhr.smcp.entities.guild.Guild;
import com.yhr.smcp.entities.guild.GuildMember;
import com.yhr.smcp.exceptions.BlizzardSyncException;
import com.yhr.smcp.parsers.guild.GuildMemberParser;
import com.yhr.smcp.parsers.guild.GuildParser;
import com.yhr.smcp.repositories.guild.GuildMemberRepository;
import com.yhr.smcp.repositories.guild.GuildRepository;
import com.yhr.smcp.client.BlizzardApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuildService {
    private final GuildRepository guildRepository;
    private final GuildMemberRepository guildMemberRepository;
    private final BlizzardApiService blizzardApiService;
    private final ObjectMapper objectMapper;
    private final GuildParser guildParser;
    private final GuildMemberParser guildMemberParser;

    @Value("${guild.roster.max_rank}")
    private Integer maxRank;

    public Guild syncGuild(String realm, String guildSlug) {
        Guild guild = fetchGuildAndSave(realm, guildSlug);
        syncGuildRoster(realm, guildSlug, guild);
        return guild;
    }

    private Guild fetchGuildAndSave(String realm, String guildSlug) {
        try {
            String rawJson = blizzardApiService.getGuild(realm, guildSlug).block(); // checar se esse metodo da blizzard aceita guildId
            JsonNode guildRoot = objectMapper.readTree(rawJson);
            Guild guild = guildParser.parse(guildRoot);
            return guildRepository.save(guild);
        } catch (Exception e) {
            throw new BlizzardSyncException("Failed to sync guild=%s, at realm=%s".formatted(guildSlug, realm), e);
        }
    }

    private void syncGuildRoster(String realm, String guildSlug, Guild guild) {
        try {
            String rawJson = blizzardApiService.getGuildRoster(realm, guildSlug).block();
            JsonNode rosterRoot = objectMapper.readTree(rawJson);
            rosterRoot.path("members").forEach(member -> {
                try {
                    int guildRank = member.path("rank").asInt();
                    if (guildRank > maxRank) {
                        return;
                    }
                    GuildMember guildMember = guildMemberParser.parse(member);
                    if (guildMemberRepository.existsById(guildMember.getId())) {
                        return;
                    }
                    guildMember.setGuild(guild);
                    guildMemberRepository.save(guildMember);

                } catch (Exception e) {
                    log.error("failed to sync member id={}", member.path("character")
                            .path("id").asLong(), e);
                }
            });
        } catch (Exception e) {
            throw new BlizzardSyncException("Failed to sync guild roster, guild=%s, at realm=%s".formatted(guildSlug, realm), e);
        }

    }

}
