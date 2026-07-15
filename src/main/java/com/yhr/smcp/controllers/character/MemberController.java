package com.yhr.smcp.controllers.character;

import com.yhr.smcp.entities.character.CharacterProfile;
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

    @PostMapping(value = "/{realm}/{characterName}/character/sync")
    public ResponseEntity<CharacterProfile> syncMember(
            @PathVariable String realm,
            @PathVariable String characterName) {

        CharacterProfile charProfile = characterService.syncCharacter(realm, characterName);
        return ResponseEntity.ok(charProfile);
    }
    
}
