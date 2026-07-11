package com.yhr.smcp.services.gamedata.character;

import com.yhr.smcp.entities.gamedata.character.PlayableClass;
import com.yhr.smcp.entities.gamedata.character.PlayableSpecialization;
import com.yhr.smcp.exceptions.BlizzardSyncException;
import com.yhr.smcp.parsers.gamedata.character.PlayableClassesParser;
import com.yhr.smcp.parsers.gamedata.character.PlayableSpecializationsParser;
import com.yhr.smcp.repositories.gamedata.character.PlayableClassRepository;
import com.yhr.smcp.repositories.gamedata.character.PlayableSpecializationRepository;
import com.yhr.smcp.services.BlizzardApiService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayableClassDataService {
    private final PlayableClassRepository playableClassRepository;
    private final PlayableSpecializationRepository playableSpecializationRepository;

    private final PlayableClassesParser playableClassesParser;
    private final PlayableSpecializationsParser playableSpecializationsParser;

    private final BlizzardApiService blizzardApiService;
    private final ObjectMapper objectMapper;

    @Transactional
    public void syncPlayableClasses() {
        String rawIndexJson = fetchClassIndex();
        JsonNode indexRoot = objectMapper.readTree(rawIndexJson);

        indexRoot.path("classes").forEach(classNode -> {
            Integer classId = classNode.path("id").asInt();
            if (playableClassRepository.existsById(classId)) {
                return;
            }
            try {
                String classDetailsJson = blizzardApiService.getPlayableClass(classId).block();
                JsonNode detailsRoot = objectMapper.readTree(classDetailsJson);

                PlayableClass playableClass = playableClassesParser.parse(detailsRoot);
                playableClassRepository.save(playableClass);

                List<PlayableSpecialization> specs = playableSpecializationsParser.parse(detailsRoot, playableClass);
                playableSpecializationRepository.saveAll(specs);

            } catch (Exception e) {
                log.error("failed to sync class id={}", classId, e);
            }

        });
    }

    public PlayableClass findPlayableClassById(Integer id) {
        return playableClassRepository.findById(id).orElse(null);
    }

    public PlayableSpecialization findPlayableSpecializationById(Integer id) {
        return playableSpecializationRepository.findById(id).orElse(null);
    }

    private String fetchClassIndex() {
        try {
            return blizzardApiService.getPlayableClassesIndex().block();
        } catch (Exception e) {
            throw new BlizzardSyncException("failed to fetch playable classes indexes", e);
        }
    }
}
