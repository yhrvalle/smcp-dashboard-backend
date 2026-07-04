package com.yhr.smcp.controllers.gamedata.mythicplus;

import com.yhr.smcp.services.gamedata.KeystoneAffixDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/gamedata")
public class KeystoneAffixController {

    private final KeystoneAffixDataService keystoneAffixDataService;

    @PostMapping("/sync-affixes")
    public ResponseEntity<String> syncAffixes() {
        keystoneAffixDataService.syncKeystoneAffixes();
        return ResponseEntity.ok().build();
    }
}
