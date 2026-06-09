package com.yhr.smcp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    public Mono<String> getMythicCharacterProfile(String realm, String characterName) {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/profile/wow/character/{realm}/{characterName}/mythic-keystone-profile?namespace=profile-us",
                        realm, characterName)
                .retrieve()
                .bodyToMono(String.class);
    }

    // -- DATA --
    public Mono<String> getDataByHref(String href) {
        return blizzardWebClient.get()
                .uri(href)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<List<String>> getDataInParallel(List<String> hrefList) {
        return Flux.fromIterable(hrefList)
                .flatMapSequential(href -> blizzardWebClient.get()
                        .uri(href)
                        .retrieve()
                        .bodyToMono(String.class))
                .collectList();
    }

    // -- GAME DATA CLASSES AND SPECS --
    public Mono<String> getPlayableClassesIndex() {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/playable-class/index?namespace=static-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class);
    }

    // https://us.api.blizzard.com/data/wow/playable-class/7?namespace=static-us&locale=en_US
    public Mono<String> getPlayableClass(Integer classId) {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/playable-class/" + classId + "?namespace=static-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class);
    }
}
