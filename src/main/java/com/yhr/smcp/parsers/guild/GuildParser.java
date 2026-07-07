package com.yhr.smcp.parsers.guild;

import com.yhr.smcp.entities.guild.Guild;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.time.Instant;

@Component
public class GuildParser {
    public Guild parse(JsonNode guildRoot) {
        Long id = guildRoot.path("guildId").asLong();
        String name = guildRoot.path("name").asString();
        String faction = guildRoot.path("faction").path("name").asString();
        String realm = guildRoot.path("realm").path("name").asString();
        Instant createdTimestamp = Instant.ofEpochMilli(guildRoot.path("created_Timestamp").asLong());
        Integer achievementPoints = guildRoot.path("achievement_points").asInt();
        Integer memberCount = guildRoot.path("member_count").asInt();

        return Guild.builder()
                .id(id)
                .name(name)
                .faction(faction)
                .realm(realm)
                .createdTimestamp(createdTimestamp)
                .achievementPoints(achievementPoints)
                .memberCount(memberCount)
                .build();

    }

}
