package com.yhr.smcp.parsers.gamedata.achievement;

import com.yhr.smcp.entities.gamedata.achievement.Achievements;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Component
public class AchievementsParser {

    public Achievements parse(JsonNode achieveRoot) {
        Long id = achieveRoot.path("id").asLong();
        String category = achieveRoot.path("category").asString();
        String name = achieveRoot.path("name").asString();
        String description = achieveRoot.path("description").asString();
        Integer points = achieveRoot.path("points").asInt();
        return Achievements.builder()
                .id(id)
                .name(name)
                .description(description)
                .points(points)
                .category(category)
                .build();
    }

}
