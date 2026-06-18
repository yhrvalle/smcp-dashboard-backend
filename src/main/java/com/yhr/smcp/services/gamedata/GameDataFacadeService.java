package com.yhr.smcp.services.gamedata;

import com.yhr.smcp.entities.gamedata.PlayableSpecialization;
import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneSeason;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameDataFacadeService {
    private final KeystoneSeasonService keystoneSeasonService;
    private final PlayableClassDataService playableClassDataService;

    public Map<Integer, KeystoneSeason> buildSeasonMap(List<Integer> seasonIds) {
        Map<Integer, KeystoneSeason> seasonMap = new HashMap<>();
        for (Integer seasonId : seasonIds) {
            KeystoneSeason keystoneSeason = keystoneSeasonService.findKeystoneSeasonById(seasonId);
            if (keystoneSeason != null) {
                seasonMap.put(seasonId, keystoneSeason);
            } else {
                log.warn("Season {} not found in database.", seasonId);
            }

        }
        return seasonMap;
    }

    public Map<Integer, PlayableSpecialization> buildSpecializationMap(List<JsonNode> seasonRoot) {
        Set<Integer> uniqueIds = new HashSet<>();
        for (JsonNode season : seasonRoot) {
            for (JsonNode run : season.path("best_runs")) {
                for (JsonNode member : run.path("members")) {
                    JsonNode specNode = member.path("specialization").path("id");
                    if (!specNode.isMissingNode()) {
                        uniqueIds.add(specNode.asInt());
                    }

                }
            }

        }

        Map<Integer, PlayableSpecialization> specializationMap = new HashMap<>();
        for (Integer specId : uniqueIds) {
            PlayableSpecialization specialization = playableClassDataService.findPlayableSpecializationById(specId);
            if (specialization == null) {
                log.warn("Specialization {} not found in database.", specId);
            }
            specializationMap.put(specId, specialization);
        }
        return specializationMap;
    }

}
