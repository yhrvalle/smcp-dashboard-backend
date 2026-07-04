package com.yhr.smcp.dto.response.mythicplus;

import java.time.Instant;
import java.util.List;

public record KeystoneRunResponseDTO(
        Long id,
        String dungeonName,
        Integer level,
        Boolean isTimed,
        Double dungeonMythicRating,
        String ratingColorHex,
        Instant completedTimestamp,
        Instant duration,
        List<AffixDTO> affixes
) {
}
