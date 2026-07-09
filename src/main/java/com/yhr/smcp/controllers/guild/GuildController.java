package com.yhr.smcp.controllers.guild;

import com.yhr.smcp.dto.response.guild.GuildDTO;
import com.yhr.smcp.dto.response.guild.GuildMemberDTO;
import com.yhr.smcp.entities.guild.Guild;
import com.yhr.smcp.services.guild.GuildService;
import com.yhr.smcp.services.queries.guild.GuildMemberQueryService;
import com.yhr.smcp.services.queries.guild.GuildQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guild")
@RequiredArgsConstructor
public class GuildController {
    private final GuildService guildService;
    private final GuildMemberQueryService guildMemberQueryService;
    private final GuildQueryService guildQueryService;

    @PostMapping("{realm}/{guildSlug}/sync")
    public ResponseEntity<GuildDTO> syncGuild(@PathVariable("realm") String realm,
                                              @PathVariable("guildSlug") String guildSlug) {
        Guild guild = guildService.syncGuild(realm, guildSlug);
        return ResponseEntity.ok(guildQueryService.getGuild(guild.getId()));
    }

    @GetMapping("{guildId}/members")
    public ResponseEntity<Page<GuildMemberDTO>> getGuildMembers(@PathVariable("guildId") Long guildId,
                                                                Pageable pageable) {
        Page<GuildMemberDTO> members = guildMemberQueryService.getGuildRoster(guildId, pageable);
        return ResponseEntity.ok(members);

    }

}
