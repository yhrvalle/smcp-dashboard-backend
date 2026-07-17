package com.yhr.smcp.dto.response.mythicplus;

import java.time.Instant;

public record MythicSeasonDTO(
        Long id,
        Double seasonRating,
        String ratingColorHex,
        String seasonName,
        Instant startTimestamp,
        Instant endTimestamp
) {
}
