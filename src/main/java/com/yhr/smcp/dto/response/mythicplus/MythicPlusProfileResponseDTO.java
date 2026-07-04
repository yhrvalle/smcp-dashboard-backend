package com.yhr.smcp.dto.response.mythicplus;

public record MythicPlusProfileResponseDTO(
        Long id,
        Double currentMythicRating,
        String ratingColorHex
) {
}
