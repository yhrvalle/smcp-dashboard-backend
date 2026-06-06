package com.yhr.smcp.controllers;

import com.yhr.smcp.entities.GuildMember;
import com.yhr.smcp.services.MemberService;
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

    @PostMapping(value = "/{realm}/{characterName}/sync")
    public ResponseEntity<GuildMember> syncMember(
            @PathVariable String realm,
            @PathVariable String characterName) {
        GuildMember member = memberService.syncMember(realm, characterName);
        return ResponseEntity.ok(member);
    }


}
