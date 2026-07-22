package com.yhr.smcp.services.gamedata.achievement;

import com.yhr.smcp.client.BlizzardStaticApiClient;
import com.yhr.smcp.entities.gamedata.achievement.Achievements;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import com.yhr.smcp.exceptions.BlizzardSyncException;
import com.yhr.smcp.parsers.gamedata.achievement.AchievementsParser;
import com.yhr.smcp.repositories.gamedata.achievement.AchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementsDataService {
    private final AchievementRepository achievementRepository;
    private final AchievementsParser achievementsParser;
    private final BlizzardStaticApiClient blizzardStaticApiClient;
    private final ObjectMapper objectMapper;

    public void syncAchievements() {
        String rawJson = fetchAchievementsIndex();
        JsonNode indexRoot = objectMapper.readTree(rawJson);
        indexRoot.path("achievements").forEach(achieveNode -> {
            Long achieveId = achieveNode.path("id").asLong();
            if (achievementRepository.existsById(achieveId)) {
                return;
            }
            try {
                String achieveDetailJson = blizzardStaticApiClient.getAchievementDetails(achieveId).block();
                JsonNode detailsRoot = objectMapper.readTree(achieveDetailJson);
                Achievements achievement = achievementsParser.parse(detailsRoot);
                achievementRepository.save(achievement);
            } catch (BlizzardParsingException e) {
                log.error("failed to parse achievement id={}", achieveId, e);
            } catch (DataAccessException e) {
                log.error("failed to save achievement id={}", achieveId, e);
            } catch (Exception e) {
                log.error("failed to sync achievement id={}", achieveId, e);
            }
        });
    }

    private String fetchAchievementsIndex() {
        try {
            return blizzardStaticApiClient.getAchievementIndex().block();
        } catch (Exception e) {
            throw new BlizzardSyncException("failed to fetch achievements indexes", e);
        }
    }

}
