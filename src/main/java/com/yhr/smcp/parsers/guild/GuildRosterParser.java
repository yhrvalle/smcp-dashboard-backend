package com.yhr.smcp.parsers.guild;

import com.yhr.smcp.entities.guild.GuildMember;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class GuildRosterParser {
    private final GuildMemberParser memberParser;

    public List<GuildMember> parse(JsonNode rosterRoot, Integer maxRank) {
        List<GuildMember> members = new ArrayList<>();
        rosterRoot.path("members").forEach(member -> {
            int guildRank = member.path("rank").asInt();
            if (guildRank > maxRank) {
                return;
            }
            members.add(memberParser.parse(member));
        });
        return members;
    }
}
