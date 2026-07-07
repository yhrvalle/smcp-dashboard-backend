package com.yhr.smcp.parsers.guild;

import com.yhr.smcp.entities.guild.GuildMember;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import tools.jackson.databind.JsonNode;

public class GuildMemberParser {
    public GuildMember parse(JsonNode memberRoot) {
        try {
            JsonNode characterNode = memberRoot.path("character");

            Long id = characterNode.path("id").asLong();
            String name = characterNode.path("name").asString();
            String realm = characterNode.path("realm").path("slug").asString();
            Integer level = characterNode.path("level").asInt();
            Integer classId = characterNode.path("playable_class").path("id").asInt();
            Integer raceId = characterNode.path("playable_race").path("id").asInt();
            Integer guildRank = characterNode.path("guild_rank").asInt();

            return GuildMember.builder()
                    .id(id)
                    .name(name)
                    .realm(realm)
                    .level(level)
                    .classId(classId)
                    .raceId(raceId)
                    .guildRank(guildRank)
                    .build();
        } catch (Exception e) {
            throw new BlizzardParsingException("GuildMember", "member=" + memberRoot.path("id"), e);
        }
    }
}
