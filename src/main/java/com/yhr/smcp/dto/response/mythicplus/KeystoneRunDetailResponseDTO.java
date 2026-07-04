package com.yhr.smcp.dto.response.mythicplus;

import java.time.Instant;
import java.util.List;

public record KeystoneRunDetailResponseDTO(
        Long id,
        String dungeonName,
        Integer level,
        Boolean isTimed,
        String ratingColorHex,
        Instant completedTimestamp,
        Instant duration,
        List<AffixDTO> affixes,
        List<KeystoneMembersDTO> members

) {
    public record KeystoneMembersDTO(
            String characterName,
            String realm,
            String specializationName,
            String className,
            String race,
            Double itemLevel
    ) {
    }

}
