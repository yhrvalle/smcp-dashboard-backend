package com.yhr.smcp.controllers.gamedata.mythicplus;

import com.yhr.smcp.services.gamedata.KeystoneSeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/gamedata")
public class KeystoneSeasonController {
    private final KeystoneSeasonService keystoneSeasonService;

    @PostMapping("/sync-seasons")
    public ResponseEntity<String> syncSeasons() {
        keystoneSeasonService.syncMythicSeasons();
        return ResponseEntity.ok().build();
    }
}
