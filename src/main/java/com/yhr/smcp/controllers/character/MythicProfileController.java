package com.yhr.smcp.controllers.character;

import com.yhr.smcp.dto.response.mythicplus.KeystoneRunDTO;
import com.yhr.smcp.dto.response.mythicplus.KeystoneRunDetailDTO;
import com.yhr.smcp.dto.response.mythicplus.MythicPlusProfileDTO;
import com.yhr.smcp.dto.response.mythicplus.MythicSeasonDTO;
import com.yhr.smcp.entities.character.mythicplus.KeystoneRun;
import com.yhr.smcp.services.queries.mythicplus.KeystoneRunQueryService;
import com.yhr.smcp.services.queries.mythicplus.MythicPlusProfileQueryService;
import com.yhr.smcp.services.queries.mythicplus.MythicSeasonQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/character/mythicplus")
@RequiredArgsConstructor
public class MythicProfileController { //TODO: fazer as parada vir só do boneco que está requisitando
    private final MythicPlusProfileQueryService mythicPlusProfileQueryService;
    private final MythicSeasonQueryService mythicSeasonQueryService;
    private final KeystoneRunQueryService keystoneRunQueryService;

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

    @GetMapping("/{id}/season/{seasonId}/runs")
    public ResponseEntity<Page<KeystoneRunDTO>> getKeystoneRuns(
            @PathVariable Long id, @PathVariable Long seasonId, Pageable pageable) {
        MythicSeasonDTO season = mythicSeasonQueryService.getCharacterMythicSeason(id, seasonId);
        Page<KeystoneRunDTO> runs = keystoneRunQueryService.getRunsBySeason(season.id(), pageable);
        return ResponseEntity.ok(runs);
    }


    @GetMapping("/{id}/season/{seasonId}/runs/{runId}")
    public ResponseEntity<KeystoneRunDetailDTO> getKeystoneRunDetail(
            @PathVariable Long id, @PathVariable Long seasonId, @PathVariable Long runId) {
        MythicSeasonDTO season = mythicSeasonQueryService.getCharacterMythicSeason(id, seasonId);
        KeystoneRunDetailDTO run = keystoneRunQueryService.getRunDetailById(season.id(), runId);
        return ResponseEntity.ok(run);
    }

}
