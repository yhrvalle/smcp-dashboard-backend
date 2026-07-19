package com.yhr.smcp.dto.response.mythicplus;

import java.time.Instant;
import java.util.List;

public record KeystoneRunDetailDTO(
        KeystoneRunDTO run,
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
