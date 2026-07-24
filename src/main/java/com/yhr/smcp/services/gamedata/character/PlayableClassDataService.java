package com.yhr.smcp.services.gamedata.character;

import com.yhr.smcp.client.BlizzardStaticApiClient;
import com.yhr.smcp.entities.gamedata.character.PlayableClass;
import com.yhr.smcp.entities.gamedata.character.PlayableSpecialization;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import com.yhr.smcp.exceptions.BlizzardSyncException;
import com.yhr.smcp.parsers.gamedata.character.PlayableClassesParser;
import com.yhr.smcp.parsers.gamedata.character.PlayableSpecializationsParser;
import com.yhr.smcp.repositories.gamedata.character.PlayableClassRepository;
import com.yhr.smcp.repositories.gamedata.character.PlayableSpecializationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayableClassDataService {
    private final PlayableClassRepository playableClassRepository;
    private final PlayableSpecializationRepository playableSpecializationRepository;

    private final PlayableClassesParser playableClassesParser;
    private final PlayableSpecializationsParser playableSpecializationsParser;

    private final BlizzardStaticApiClient blizzardStaticApiClient;
    private final ObjectMapper objectMapper;

    public void syncPlayableClasses() {
        String rawIndexJson = fetchClassIndex();
        JsonNode indexRoot = objectMapper.readTree(rawIndexJson);
        Set<Integer> existingIds = new HashSet<>(playableClassRepository.findAllIds());
        List<Integer> ids = new ArrayList<>();
        indexRoot.path("classes").forEach(classNode -> {
            Integer classId = classNode.path("id").asInt();
            if (!existingIds.contains(classId)) {
                ids.add(classId);
            }
        });
        Flux.fromIterable(ids)
                .flatMap(id -> blizzardStaticApiClient.getPlayableClass(id)
                        .map(json -> Map.entry(id, json))
                        .onErrorResume(e -> {
                            log.error("failed to fetch playable class for class id={}", id, e);
                            return Mono.empty();
                        }), 20)
                .doOnNext(classMap -> {
                    savePlayableClass(classMap.getKey(), classMap.getValue());
                })
                .blockLast();

    }

    public PlayableClass findPlayableClassById(Integer id) {
        return playableClassRepository.findById(id).orElse(null);
    }

    public PlayableSpecialization findPlayableSpecializationById(Integer id) {
        return playableSpecializationRepository.findById(id).orElse(null);
    }

    public List<PlayableSpecialization> findAllPlayableSpecializationsByIds(List<Integer> ids) {
        return playableSpecializationRepository.findAllById(ids);
    }

    private String fetchClassIndex() {
        try {
            return blizzardStaticApiClient.getPlayableClassesIndex().block();
        } catch (Exception e) {
            throw new BlizzardSyncException("failed to fetch playable classes indexes", e);
        }
    }

    private void savePlayableClass(Integer classId, String classDetailsJson) {
        try {
            JsonNode detailsRoot = objectMapper.readTree(classDetailsJson);

            PlayableClass playableClass = playableClassesParser.parse(detailsRoot);
            playableClassRepository.save(playableClass);

            List<PlayableSpecialization> specs = playableSpecializationsParser.parse(detailsRoot, playableClass);
            playableSpecializationRepository.saveAll(specs);

        } catch (BlizzardParsingException e) {
            log.error("failed to parse class id={}", classId, e);
        } catch (DataAccessException e) {
            log.error("failed to save class id={}", classId, e);
        } catch (Exception e) {
            log.error("failed to sync class id={}", classId, e);
        }
    }
}
