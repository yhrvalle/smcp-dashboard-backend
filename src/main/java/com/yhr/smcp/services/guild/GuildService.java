package com.yhr.smcp.services.guild;

import com.yhr.smcp.entities.guild.Guild;
import com.yhr.smcp.parsers.guild.GuildParser;
import com.yhr.smcp.repositories.guild.GuildRepository;
import com.yhr.smcp.services.BlizzardApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
@Slf4j
public class GuildService {
    private final GuildRepository guildRepository;
    private final BlizzardApiService blizzardApiService;
    private final ObjectMapper objectMapper;
    private final GuildParser guildParser;

    public Guild syncGuild(String realm, String guildSlug) {
        String rawJson = blizzardApiService.getGuild(realm, guildSlug).block();
        JsonNode guildRoot = objectMapper.readTree(rawJson);
        Guild guild = guildParser.parse(guildRoot);
        guildRepository.save(guild);
        return guild;
    }

}
