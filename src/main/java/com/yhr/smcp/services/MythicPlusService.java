package com.yhr.smcp.services;


import com.yhr.smcp.entities.character.MythicPlusProfile;
import com.yhr.smcp.parsers.mythicplus.MythicPlusProfileParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MythicPlusService {
    private final BlizzardApiService blizzardApiService;
    private final MythicPlusProfileParser mythicPlusProfileParser;
    private final ObjectMapper objectMapper;

    public MythicPlusProfile syncProfile(String mythicProfileHref) {
        try {

            String rawJson = blizzardApiService.getDataByHref(mythicProfileHref).block();
            JsonNode profileRootNode = objectMapper.readTree(rawJson);

            // pegar as URLś das seasons
            List<String> seasonURLs = new ArrayList<>();
            profileRootNode.path("seasons").forEach(season -> {
                seasonURLs.add(season.path("key").path("href").asString());
            });

            // fetch nas seasons em parelelo
            List<String> rawSeasonJson = blizzardApiService.getDataInParallel(seasonURLs).block();

            // cada season da lista de json numa lista de node
            List<JsonNode> seasonRootNodes = Optional.ofNullable(rawSeasonJson)
                    .orElse(List.of())
                    .stream()
                    .map(raw -> {
                        try {
                            return objectMapper.readTree(raw);
                        } catch (Exception e) {
                            return null; // se vier um nodo nulo da blizzard vai retornar nada e ser filtrado depois
                            // e a annotation do lombok deve pegar
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();


            // seasonRoot -> "members" -> "specialization" -> "key" -> "href" -> spec root data -> "playable_class"
            Map<String, String> specClassMap = buildSpecClassMap(seasonRootNodes);
            List<JsonNode> seasonDataMap = buildSeasonMap(seasonRootNodes);
            // retornar o profile completo
            return mythicPlusProfileParser.buildProfile(profileRootNode, seasonDataMap, specClassMap);
        } catch (Exception e) {
            throw new RuntimeException("MythicPlusService: failed to sync mythic plus profile: " + e.getMessage(), e);
        }

    }

    // BUG: isso aqui é populado de acordo com as minhas runs, tem que chamar isso de outro lugar da blizzard ai sim
    //  Usar a game data API e tirar esse method daqui


    private Map<String, String> buildSpecClassMap(List<JsonNode> seasonsNode) {
        Set<String> specsURL = new HashSet<>();
        for (JsonNode season : seasonsNode) {
            for (JsonNode run : season.path("best_runs")) {
                for (JsonNode member : run.path("members")) {
                    String specHref = member.path("specialization").path("key").path("href").asString();
                    if (!specHref.isEmpty()) {
                        specsURL.add(specHref);
                    }
                }
            }
        }

        List<String> uniqueSpecList = new ArrayList<>(specsURL);
        List<String> rawSpecJson = blizzardApiService.getDataInParallel(uniqueSpecList).block();
        Map<String, String> specClassMap = new HashMap<>();
        for (int i = 0; i < specsURL.size(); i++) {
            try {
                String json = rawSpecJson.get(i);
                if (json.isEmpty()) {
                    continue;
                }
                JsonNode specRoot = objectMapper.readTree(rawSpecJson.get(i));
                String className = specRoot.path("playable_class").path("name").path("en_US").asString();
                specClassMap.put(uniqueSpecList.get(i), className);

            } catch (Exception e) {
                throw new RuntimeException("MythicPlusService: failed to map the spec class map: " + e.getMessage(), e);
            }
        }
        return specClassMap;
    }

    // BUG: Pegar essas infos da Game Data API, coletar apenas os ID's e puxar do DB
    //  Tirar esse method daqui
    private List<JsonNode> buildSeasonMap(List<JsonNode> seasonsNode) {
        List<String> seasonHrefs = new ArrayList<>();
        for (JsonNode season : seasonsNode) {
            String seasonHref = season.path("season").path("key").path("href").asString();
            if (!seasonHref.isEmpty()) {
                seasonHrefs.add(seasonHref);
            }
        }

        List<String> rawSeasonDataJson = blizzardApiService.getDataInParallel(seasonHrefs).block();
        List<JsonNode> seasonDataNodes = new ArrayList<>();
        for (int i = 0; i < seasonHrefs.size(); i++) {
            try {
                JsonNode season = objectMapper.readTree(rawSeasonDataJson.get(i));
                seasonDataNodes.add(season);
            } catch (Exception e) {
                throw new RuntimeException("MythicPlusService: failed to map the season node map: " + e.getMessage(), e);
            }
        }
        return seasonDataNodes;
    }
}
