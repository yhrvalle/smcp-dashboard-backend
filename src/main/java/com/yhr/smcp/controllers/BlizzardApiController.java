package com.yhr.smcp.controllers;

import com.yhr.smcp.services.BlizzardApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class BlizzardApiController {

    private final BlizzardApiService blizzardApiService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) //TODO: remover media type
    public Mono<ResponseEntity<String>> getGuild() {
        String realm = "azralon";
        String guildName = "send-me-cat-pics";

        return blizzardApiService.getGuildRoster(realm, guildName)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/yhera", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> getYhera() {
        String realm = "azralon";
        String characterName = "yhera";
        return blizzardApiService.getCharacter(realm, characterName)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
