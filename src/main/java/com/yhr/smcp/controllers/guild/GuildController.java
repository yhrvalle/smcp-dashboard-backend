package com.yhr.smcp.controllers.guild;

import com.yhr.smcp.entities.guild.Guild;
import com.yhr.smcp.services.guild.GuildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guild")
@RequiredArgsConstructor
public class GuildController {
    private final GuildService guildService;

    @PostMapping("{realm}/{guildSlug}/sync")
    public ResponseEntity<Guild> syncGuild(@PathVariable("realm") String realm,
                                           @PathVariable("guildSlug") String guildSlug) {
        Guild guild = guildService.syncGuild(realm, guildSlug);
        return ResponseEntity.ok(guild); //TODO: retornar DTO
    }
}
