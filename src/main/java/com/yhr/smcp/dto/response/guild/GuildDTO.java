package com.yhr.smcp.dto.response.guild;

import java.time.Instant;

public record GuildDTO(
        Long id,
        String name,
        String faction,
        String realm,
        Instant createdTimestamp,
        Integer achievementPoints,
        Integer memberCount

) {
}
