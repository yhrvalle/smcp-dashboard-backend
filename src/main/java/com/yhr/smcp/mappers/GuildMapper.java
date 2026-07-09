package com.yhr.smcp.mappers;

import com.yhr.smcp.dto.response.guild.GuildMemberDTO;
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
}
