package com.yhr.smcp.controllers;

import com.yhr.smcp.services.GameDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/data")
public class GameDataController {

    private final GameDataService gameDataService;

    @PostMapping("/sync-classes")
    public ResponseEntity<String> syncClasses() {
        gameDataService.syncPlayableClasses();
        return ResponseEntity.ok().build();
    }


}
