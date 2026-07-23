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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hibernate.Hibernate.map;

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
        List<Long> ids = new ArrayList<>();
        indexRoot.path("achievements").forEach(achieveNode -> {
            Long achieveId = achieveNode.path("id").asLong();
            if (!achievementRepository.existsById(achieveId)) {
                ids.add(achieveId);
            }
        });

        Flux.fromIterable(ids)
                .flatMap(id ->
                        blizzardStaticApiClient.getAchievementDetails(id)
                                .map(json -> Map.entry(id, json))
                                .onErrorResume(e -> {
                                    log.error("failed to fetch achievement details for achievement id={}", id, e);
                                    return Mono.empty();
                                }), 20)
                .doOnNext(entry -> saveAchievement(entry.getKey(), entry.getValue()))
                .blockLast();
    }

    private String fetchAchievementsIndex() {
        try {
            return blizzardStaticApiClient.getAchievementIndex().block();
        } catch (Exception e) {
            throw new BlizzardSyncException("failed to fetch achievements indexes", e);
        }
    }
    
    private void saveAchievement(Long id, String rawJson) {
        try {
            JsonNode indexRoot = objectMapper.readTree(rawJson);
            Achievements achievements = achievementsParser.parse(indexRoot);
            achievementRepository.save(achievements);
        } catch (BlizzardParsingException e) {
            log.error("failed to parse achievement id={}", id, e);
        } catch (DataAccessException e) {
            log.error("failed to save achievement id={}", id, e);
        } catch (Exception e) {
            log.error("failed to sync achievement id={}", id, e);
        }
    }
}
