package com.yhr.smcp.util.mythic;

import tools.jackson.databind.JsonNode;

import java.util.TreeMap;

public class RatingColors {
    public static TreeMap<String, Double> ratingColorParserUtil(JsonNode node) {
        TreeMap<String, Double> ratingColor = new TreeMap<>();
        ratingColor.put("r", node.path("r").asDouble());
        ratingColor.put("g", node.path("g").asDouble());
        ratingColor.put("b", node.path("b").asDouble());
        ratingColor.put("a", node.path("a").asDouble());
        return ratingColor;
    }
}
