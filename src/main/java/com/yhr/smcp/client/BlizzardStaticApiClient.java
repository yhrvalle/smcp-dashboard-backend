package com.yhr.smcp.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.PrematureCloseException;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class BlizzardStaticApiClient {
    private static final Retry RETRY_SPEC = Retry.backoff(3, Duration.ofSeconds(Duration.ofSeconds(1)))
            .maxBackoff(Duration.ofSeconds(10))
            .filter(e -> e instanceof WebClientResponseException.TooManyRequests
                    || e.getCause() instanceof PrematureCloseException);
    private final WebClient blizzardWebClient;

    // Class
    public Mono<String> getPlayableClassesIndex() {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/playable-class/index?namespace=static-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getPlayableClassDetails(Integer classId) {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/playable-class/" + classId + "?namespace=static-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(RETRY_SPEC);

    }

    // Affix
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
                .bodyToMono(String.class)
                .retryWhen(RETRY_SPEC);

    }

    // Race
    public Mono<String> getRaceIndex() {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/playable-race/index?namespace=static-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class);
    }

    // Achievements
    public Mono<String> getAchievementIndex() {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/achievement/index?namespace=static-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getAchievementDetails(Long achievementId) {
        return blizzardWebClient.get()
                .uri("https://us.api.blizzard.com/data/wow/achievement/" + achievementId + "?namespace=static-us&locale=en_US")
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(RETRY_SPEC);
    }
}
