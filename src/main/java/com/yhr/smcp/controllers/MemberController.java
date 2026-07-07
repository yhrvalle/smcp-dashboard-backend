package com.yhr.smcp.controllers;

import com.yhr.smcp.entities.guild.GuildMember;
import com.yhr.smcp.entities.character.MythicPlusProfile;
import com.yhr.smcp.services.guild.MemberService;
import com.yhr.smcp.services.MythicPlusService;
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
    private final MemberService memberService;
    private final MythicPlusService mythicPlusService;

    @PostMapping(value = "/{realm}/{characterName}/sync")
    public ResponseEntity<GuildMember> syncMember(
            @PathVariable String realm,
            @PathVariable String characterName) {
        GuildMember member = memberService.syncMember(realm, characterName);
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
