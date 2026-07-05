package com.yhr.smcp.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BlizzardApiService {
    private final WebClient blizzardWebClient;

    public Mono<String> getGuildRoster(String realm, String guildName) {
        return blizzardWebClient.get()
                .uri("/data/wow/guild/{realmSlug}/{guildSlug}/roster?namespace=profile-us&locale=en_US", realm, guildName)
                .retrieve()
                .bodyToMono(String.class);

    }

    public Mono<String> getCharacter(String realm, String characterName) {
        return blizzardWebClient.get()
                .uri("/profile/wow/character/{realmSlug}/{characterName}?namespace=profile-us&locale=en_US", realm, characterName)
                .retrieve()
                .bodyToMono(String.class);
    }

    // -- MYTHIC PLUS --
    public Mono<String> getMythicCharacterProfile(String realm, String characterName) {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/profile/wow/character/{realm}/{characterName}/mythic-keystone-profile?namespace=profile-us",
                        realm.toLowerCase(),
                        characterName.toLowerCase())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getCharacterSeasonProfile(String realm, String characterName, Integer seasonId) {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/profile/wow/character/{realm}/{name}/mythic-keystone-profile/season/{id}?namespace=profile-us&locale=en_US",
                        realm.toLowerCase(),
                        characterName.toLowerCase(),
                        seasonId)
                .retrieve()
                .bodyToMono(String.class);
    }

    //TODO: esses gets são mto parecidos para pegar o index -> details tentar procurar uma forma para fazer um mais generico
    // ao inves de ficar quase repetindo
    // TODO: refactor para ser paralelo n sequencial

    // -- MYTHIC PLUS PROFILE --
    public Mono<List<String>> getCharacterSeasonsProfiles(String realm, String characterName, List<Integer> seasonsIds) {
        return Flux.fromIterable(seasonsIds)
                .flatMap(seasonId -> getCharacterSeasonProfile(realm, characterName, seasonId)
                        .onErrorResume(error -> {
                            log.error("BlizzardApiService - getCharacterSeasonProfiles failed to fetch season {}. value={}", seasonId, error.getMessage());
                            return Mono.empty();
                        }))
                .collectList();
    }

    // -- GAME DATA CLASSES AND SPECS --
    public Mono<String> getPlayableClassesIndex() {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/playable-class/index?namespace=static-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getPlayableClass(Integer classId) {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/playable-class/" + classId + "?namespace=static-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class);
    }


    // -- M+ AFFIX --
    public Mono<String> getAffixIndex() {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/keystone-affix/index?namespace=static-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getAffixDetails(Integer affixId) {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/keystone-affix/" + affixId + "?namespace=static-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class);
    }

    // -- M+ SEASON

    public Mono<String> getSeasonIndex() {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/mythic-keystone/season/index?namespace=dynamic-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getSeasonDetails(Integer seasonId) {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/mythic-keystone/season/" + seasonId + "?namespace=dynamic-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class);
    }
}

