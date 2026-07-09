package com.yhr.smcp.services.gamedata;

import com.yhr.smcp.entities.gamedata.character.PlayableSpecialization;
import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneAffix;
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
    private final KeystoneSeasonDataService keystoneSeasonDataService;
    private final PlayableClassDataService playableClassDataService;
    private final KeystoneAffixDataService keystoneAffixDataService;

    private Map<Integer, KeystoneAffix> buildKeystoneAffixMap(List<JsonNode> seasonRoot) {
        Set<Integer> affixSet = new HashSet<>();
        for (JsonNode season : seasonRoot) {
            for (JsonNode run : season.path("best_runs")) {
                for (JsonNode affix : run.path("keystone_affixes")) {
                    JsonNode affixIdNode = affix.path("id");
                    if (affixIdNode.isMissingNode()) {
                        continue;
                    }
                    affixSet.add(affixIdNode.asInt());
                }
            }
        }

        Map<Integer, KeystoneAffix> map = new HashMap<>();
        for (Integer affixId : affixSet) {
            KeystoneAffix keystoneAffix = keystoneAffixDataService.findKeystoneAffixById(affixId);
            if (keystoneAffix == null) {
                log.warn("Affix {} not found in database", affixId);
            } else {
                map.put(affixId, keystoneAffix);
            }
        }
        return map;
    }

    private Map<Integer, KeystoneSeason> buildSeasonMap(List<Integer> seasonIds) {
        Map<Integer, KeystoneSeason> seasonMap = new HashMap<>();
        for (Integer seasonId : seasonIds) {
            KeystoneSeason keystoneSeason = keystoneSeasonDataService.findKeystoneSeasonById(seasonId);
            if (keystoneSeason == null) {
                log.warn("Season {} not found in database.", seasonId);
            } else {
                seasonMap.put(seasonId, keystoneSeason);
            }

        }
        return seasonMap;
    }

    private Map<Integer, PlayableSpecialization> buildSpecializationMap(List<JsonNode> seasonRoot) {
        Set<Integer> uniqueIds = new HashSet<>();
        for (JsonNode season : seasonRoot) {
            for (JsonNode run : season.path("best_runs")) {
                for (JsonNode member : run.path("members")) {
                    JsonNode specNode = member.path("specialization").path("id");
                    if (specNode.isMissingNode()) {
                        continue;
                    }
                    uniqueIds.add(specNode.asInt());
                }
            }
        }

        Map<Integer, PlayableSpecialization> specializationMap = new HashMap<>();
        for (Integer specId : uniqueIds) {
            PlayableSpecialization specialization = playableClassDataService.findPlayableSpecializationById(specId);
            if (specialization == null) {
                log.warn("Specialization {} not found in database.", specId);
            } else {
                specializationMap.put(specId, specialization);
            }
        }
        return specializationMap;
    }

    public GameDataLookup buildLookUps(List<Integer> seasonIds, List<JsonNode> seasonRoot) {
        Map<Integer, KeystoneAffix> keystoneAffixMap = buildKeystoneAffixMap(seasonRoot);
        Map<Integer, PlayableSpecialization> specializationMap = buildSpecializationMap(seasonRoot);
        Map<Integer, KeystoneSeason> seasonMap = buildSeasonMap(seasonIds);
        return new GameDataLookup(keystoneAffixMap, specializationMap, seasonMap);
    }

    public record GameDataLookup(Map<Integer, KeystoneAffix> affixMap, Map<Integer,
            PlayableSpecialization> specializationMap, Map<Integer, KeystoneSeason> seasonMap) {
    }

}
