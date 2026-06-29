package com.yhr.smcp.util.mythic;

import tools.jackson.databind.JsonNode;

import java.util.TreeMap;

public class RatingColors {
    public static String ratingColorParserUtil(JsonNode node) {
        return "#%02X%02X%02X%02X".formatted(node.path("r").asInt(),
                node.path("g").asInt(),
                node.path("b").asInt(),
                Math.round(node.path("a").asDouble() * 255)
        );
    }
}

