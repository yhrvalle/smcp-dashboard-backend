package com.yhr.smcp.parsers.guild;

import com.yhr.smcp.entities.guild.Guild;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.time.Instant;

@Component
public class GuildParser {
    public Guild parse(JsonNode guildRoot) {
        try {
            Long id = guildRoot.path("id").asLong();
            String name = guildRoot.path("name").asString();
            String faction = guildRoot.path("faction").path("name").asString();
            String realm = guildRoot.path("realm").path("name").asString();
            Instant createdTimestamp = Instant.ofEpochMilli(guildRoot.path("created_timestamp").asLong());
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

        } catch (Exception e) {
            throw new BlizzardParsingException("GuildParser", "id=" + guildRoot.path("id").asLong(), e);
        }


    }

}
