package com.yhr.smcp.controllers.gamedata;

import com.yhr.smcp.services.gamedata.KeystoneAffixService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/gamedata")
public class KeystoneAffixController {

    private final KeystoneAffixService keystoneAffixService;

    @PostMapping("/sync-affixes")
    public ResponseEntity<String> syncAffixes() {
        keystoneAffixService.syncKeystoneAffixes();
        return ResponseEntity.ok().build();
    }
}
