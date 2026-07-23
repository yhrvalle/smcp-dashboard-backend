package com.yhr.smcp.controllers.gamedata.character;

import com.yhr.smcp.services.gamedata.character.PlayableRaceDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/gamedata")
public class PlayableRaceDataController {
    private final PlayableRaceDataService playableRaceDataService;

    @PostMapping("/sync-races")
    public ResponseEntity<String> syncRaces() {
        playableRaceDataService.syncRaces();
        return ResponseEntity.ok().build(); //TODO: add some return msg
    }
}
