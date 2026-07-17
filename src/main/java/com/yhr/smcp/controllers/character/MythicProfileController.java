package com.yhr.smcp.controllers.character;

import com.yhr.smcp.dto.response.mythicplus.MythicPlusProfileDTO;
import com.yhr.smcp.dto.response.mythicplus.MythicSeasonDTO;
import com.yhr.smcp.services.queries.mythicplus.MythicPlusProfileQueryService;
import com.yhr.smcp.services.queries.mythicplus.MythicSeasonQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/character/mythicplus")
@RequiredArgsConstructor
public class MythicProfileController {
    private final MythicPlusProfileQueryService mythicPlusProfileQueryService;
    private final MythicSeasonQueryService mythicSeasonQueryService;

    @GetMapping("/{id}")
    public ResponseEntity<MythicPlusProfileDTO> getMythicPlusProfile(@PathVariable Long id) {
        MythicPlusProfileDTO profileDTO = mythicPlusProfileQueryService.getCharacterMythicProfile(id);
        return ResponseEntity.ok(profileDTO);
    }

    @GetMapping("/{id}/season/{seasonId}")
    public ResponseEntity<MythicSeasonDTO> getMythicSeason(@PathVariable Long id, @PathVariable Long seasonId) {
        MythicSeasonDTO mythicSeasonDTO = mythicSeasonQueryService.getCharacterMythicSeason(id, seasonId);
        return ResponseEntity.ok(mythicSeasonDTO);
    }

}
