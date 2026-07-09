package com.yhr.smcp.controllers.gamedata.character;

import com.yhr.smcp.services.gamedata.character.PlayableClassDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/gamedata")
public class PlayableClassDataController {

    private final PlayableClassDataService playableClassDataService;

    @PostMapping("/sync-classes")
    public ResponseEntity<String> syncClasses() {
        playableClassDataService.syncPlayableClasses();
        return ResponseEntity.ok().build(); //TODO: add some return thing
    }


}
