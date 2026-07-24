package com.yhr.smcp.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BlizzardDynamicApiClient {
    private final WebClient blizzardWebClient;

    public Mono<String> getSeasonIndex() {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/mythic-keystone/season/index?namespace=dynamic-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getSeasonDetails(Long seasonId) {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/mythic-keystone/season/" + seasonId + "?namespace=dynamic-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class);
    }
}
