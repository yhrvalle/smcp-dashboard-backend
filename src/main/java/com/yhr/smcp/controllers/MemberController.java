package com.yhr.smcp.controllers;

import com.yhr.smcp.entities.guild.GuildMember;
import com.yhr.smcp.entities.character.mythicplus.MythicPlusProfile;
import com.yhr.smcp.services.character.CharacterService;
import com.yhr.smcp.services.character.MythicPlusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final CharacterService characterService;
    private final MythicPlusService mythicPlusService;

    @PostMapping(value = "/{realm}/{characterName}/sync")
    public ResponseEntity<GuildMember> syncMember(
            @PathVariable String realm,
            @PathVariable String characterName) {
        GuildMember member = characterService.syncMember(realm, characterName);
        return ResponseEntity.ok(member);
    }

    @PostMapping(value = "mplus/{realm}/{characterName}/sync")
    public ResponseEntity<MythicPlusProfile> syncMythicPlusProfile(
            @PathVariable String realm,
            @PathVariable String characterName) {
        MythicPlusProfile profile = mythicPlusService.syncProfile(realm, characterName);
        return ResponseEntity.ok(profile);
    }


}
