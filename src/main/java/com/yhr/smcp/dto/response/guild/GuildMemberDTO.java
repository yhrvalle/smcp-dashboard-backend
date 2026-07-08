package com.yhr.smcp.dto.response.guild;

public record GuildMemberDTO(
        String name,
        String realm,
        Integer level,
        String className,
        String specName,
        String race
) {
}
