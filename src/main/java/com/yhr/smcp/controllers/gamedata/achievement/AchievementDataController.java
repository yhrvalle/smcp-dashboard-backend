package com.yhr.smcp.controllers.gamedata.achievement;


import com.yhr.smcp.services.gamedata.achievement.AchievementsDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gamedata")
public class AchievementDataController {
    private final AchievementsDataService achievementsDataService;

    @PostMapping("/sync-achiev")
    public ResponseEntity<String> syncAchievements() {
        achievementsDataService.syncAchievements();
        return ResponseEntity.ok().build(); // TODO: add some return message
    }
}
