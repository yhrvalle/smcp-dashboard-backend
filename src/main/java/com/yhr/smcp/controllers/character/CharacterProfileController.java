package com.yhr.smcp.controllers.character;

import com.yhr.smcp.dto.response.character.CharacterProfileDTO;
import com.yhr.smcp.services.queries.character.CharacterProfileQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/character")
@RequiredArgsConstructor
public class CharacterProfileController {
    private final CharacterProfileQueryService characterProfileQueryService;

    @GetMapping("/{id}")
    public ResponseEntity<CharacterProfileDTO> getCharacterProfile(@PathVariable Long id) {
        CharacterProfileDTO characterProfileDTO = characterProfileQueryService.getCharacterProfile(id);
        return ResponseEntity.ok(characterProfileDTO);
    }
}
