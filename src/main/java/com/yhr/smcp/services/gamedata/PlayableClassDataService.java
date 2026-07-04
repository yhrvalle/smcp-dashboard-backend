package com.yhr.smcp.services.gamedata;

import com.yhr.smcp.entities.gamedata.PlayableClass;
import com.yhr.smcp.entities.gamedata.PlayableSpecialization;
import com.yhr.smcp.parsers.gamedata.PlayableClassesParser;
import com.yhr.smcp.parsers.gamedata.PlayableSpecializationsParser;
import com.yhr.smcp.repositories.gamedata.PlayableClassRepository;
import com.yhr.smcp.repositories.gamedata.PlayableSpecializationRepository;
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
        try {
            String rawIndexJson = blizzardApiService.getPlayableClassesIndex().block();
            JsonNode indexRoot = objectMapper.readTree(rawIndexJson);
            indexRoot.path("classes").forEach(classNode -> {
                Integer classId = classNode.path("id").asInt();
                if (playableClassRepository.existsById(classId)) {
                    return;
                }
                String classDetailsJson = blizzardApiService.getPlayableClass(classId).block();
                JsonNode detailsRoot = objectMapper.readTree(classDetailsJson);

                PlayableClass playableClass = playableClassesParser.parse(detailsRoot);
                playableClassRepository.save(playableClass);

                List<PlayableSpecialization> specs = playableSpecializationsParser.parse(detailsRoot, playableClass);
                playableSpecializationRepository.saveAll(specs);

            });
        } catch (Exception e) {
            throw new RuntimeException("GameDataService syncPlayableClasses failed:" + e.getMessage(), e);
        }

    }

    public PlayableClass findPlayableClassById(Integer id) {
        return playableClassRepository.findById(id).orElse(null);
    }

    public PlayableSpecialization findPlayableSpecializationById(Integer id) {
        return playableSpecializationRepository.findById(id).orElse(null);
    }

    public void saveClass(PlayableClass playableClass) {
        playableClassRepository.save(playableClass);
    }

    public void saveSpecialization(PlayableSpecialization playableSpecialization) {
        playableSpecializationRepository.save(playableSpecialization);
    }


}
