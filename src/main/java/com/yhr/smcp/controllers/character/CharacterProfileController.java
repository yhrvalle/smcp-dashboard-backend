package com.yhr.smcp.controllers.character;

import com.yhr.smcp.dto.response.character.CharacterProfileDTO;
import com.yhr.smcp.entities.character.CharacterProfile;
import com.yhr.smcp.services.character.CharacterService;
import com.yhr.smcp.services.queries.character.CharacterProfileQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/character")
@RequiredArgsConstructor
public class CharacterProfileController {
    private final CharacterProfileQueryService characterProfileQueryService;
    private final CharacterService characterService;

    @GetMapping("/{id}")
    public ResponseEntity<CharacterProfileDTO> getCharacterProfile(@PathVariable Long id) {
        CharacterProfileDTO characterProfileDTO = characterProfileQueryService.getCharacterProfile(id);
        return ResponseEntity.ok(characterProfileDTO);
    }

    @PostMapping(value = "/{realm}/{characterName}/character/sync")
    public ResponseEntity<CharacterProfileDTO> syncMember(
            @PathVariable String realm,
            @PathVariable String characterName) {

        CharacterProfile character = characterService.syncCharacter(realm, characterName);
        return ResponseEntity.ok(characterProfileQueryService.getCharacterProfile(character.getId()));
    }
}
