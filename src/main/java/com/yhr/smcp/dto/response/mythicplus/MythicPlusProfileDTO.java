package com.yhr.smcp.dto.response.mythicplus;

public record MythicPlusProfileDTO(
        Long id,
        Double currentMythicRating,
        String ratingColorHex
) {
}
