package com.yhr.smcp.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BlizzardApiService {
    private final WebClient BlizzardWebClient;

    public BlizzardApiService(WebClient webClient) {
        this.BlizzardWebClient = webClient;
    }

    public Mono<String> getGuildRoster(String realm, String guildName) {
        return BlizzardWebClient.get()
                .uri("/data/wow/guild/{realmSlug}/{guildSlug}/roster?namespace=profile-us&locale=en_US", realm, guildName)
                .retrieve()
                .bodyToMono(String.class);

    }

    public Mono<String> getCharacter(String realm, String characterName) {
        return BlizzardWebClient.get()
                .uri("/profile/wow/character/{realmSlug}/{characterName}?namespace=profile-us&locale=en_US", realm, characterName)
                .retrieve()
                .bodyToMono(String.class);
    }
}
