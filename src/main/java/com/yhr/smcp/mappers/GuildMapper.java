package com.yhr.smcp.mappers;

import com.yhr.smcp.dto.response.guild.GuildDTO;
import com.yhr.smcp.dto.response.guild.GuildMemberDTO;
import com.yhr.smcp.entities.guild.Guild;
import com.yhr.smcp.entities.guild.GuildMember;

public class GuildMapper {
    public static GuildMemberDTO toGuildMemberDTO(GuildMember guildMember, String className, String raceName) {
        return new GuildMemberDTO(
                guildMember.getId(),
                guildMember.getName(),
                guildMember.getRealm(),
                guildMember.getLevel(),
                className,
                raceName
        );
    }

    public static GuildDTO toGuildDTO(Guild guild) {
        return new GuildDTO(
                guild.getId(),
                guild.getName(),
                guild.getFaction(),
                guild.getRealm(),
                guild.getCreatedTimestamp(),
                guild.getAchievementPoints(),
                guild.getMemberCount()
        );
    }
}
