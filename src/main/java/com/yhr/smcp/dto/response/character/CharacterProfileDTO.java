package com.yhr.smcp.dto.response.character;

public record CharacterProfileDTO(
        Long id,
        String name,
        String realm,
        String activeTitle,
        Integer level,
        Long equippedItemLevel,
        String faction,
        String gender,
        String raceName,
        String className,
        String specializationName,
        Double currentMythicRating,
        String ratingColorHex

) {
}
