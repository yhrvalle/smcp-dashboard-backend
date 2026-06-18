package com.yhr.smcp.services;

import com.yhr.smcp.entities.character.MythicPlusProfile;
import com.yhr.smcp.entities.gamedata.PlayableSpecialization;
import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneSeason;
import com.yhr.smcp.parsers.mythicplus.MythicPlusProfileParser;
import com.yhr.smcp.services.gamedata.GameDataFacadeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MythicPlusService {
    private final BlizzardApiService blizzardApiService;

    private final MythicPlusProfileParser mythicPlusProfileParser;
    private final ObjectMapper objectMapper;

    private final GameDataFacadeService gameDataService;


    // TODO: resolver essa chain of responsabilities dos parsers com as infos no database
    public MythicPlusProfile syncProfile(String realm, String name) {
        try {
            String rawMythicProfileJson = blizzardApiService.getMythicCharacterProfile(realm, name).block();
            JsonNode mythicProfileRoot = objectMapper.readTree(rawMythicProfileJson);

            List<Integer> seasonIds = new ArrayList<>();
            mythicProfileRoot.path("seasons").forEach((season) -> {
                seasonIds.add(season.path("id").asInt());
            });

            // pegar os seasonIds e transformar em nodes dando fetch em cada season com esses ids
            List<JsonNode> seasonRootNodes = Flux.fromIterable(seasonIds)
                    .flatMap(id -> blizzardApiService.getCharacterSeasonProfile(realm, name, id)
                            .flatMap(raw -> {
                                try {
                                    return Mono.just(objectMapper.readTree(raw));
                                } catch (Exception e) {
                                    return Mono.error(new RuntimeException("MythicPlusService Failed Json Mapping: " + e.getMessage()));
                                }
                            })
                            .onErrorResume(error -> {
                                log.error("MythicPlusService failed to sync season {}, value={}", id, error.getMessage());
                                return Mono.empty();
                            })
                    )
                    .collectList()
                    .blockOptional()
                    .orElse(Collections.emptyList());


            // pegar os seasonsids e criar um map com as details de cada season que esta na lista
            Map<Integer, KeystoneSeason> keystoneSeasonMap = gameDataService.buildSeasonMap(seasonIds);

            Map<Integer, PlayableSpecialization> specializationMap = gameDataService.buildSpecializationMap(seasonRootNodes);


            return mythicPlusProfileParser.buildProfile(mythicProfileRoot, seasonRootNodes, specializationMap, keystoneSeasonMap);

        } catch (Exception e) {
            log.error("Error syncing mythic profile name={}, value={}", name, e.getMessage());
            throw new RuntimeException("MythicPlusService: failed to sync mythic plus profile: " + e.getMessage(), e);
        }

    }

}
