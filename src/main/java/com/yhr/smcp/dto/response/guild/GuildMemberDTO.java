package com.yhr.smcp.dto.response.guild;

public record GuildMemberDTO(
        Long id,
        String name,
        String realm,
        Integer level,
        String className
        // String race TODO: fazer a tabela de gamedata races
) {
}
