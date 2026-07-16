package com.yhr.smcp.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BlizzardStaticApiClient {
    private final WebClient blizzardWebClient;

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

    public Mono<String> getRaceIndex() {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/playable-race/index?namespace=static-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class);
    }
}
