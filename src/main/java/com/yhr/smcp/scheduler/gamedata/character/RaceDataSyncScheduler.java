package com.yhr.smcp.scheduler.gamedata.character;

import com.yhr.smcp.services.gamedata.character.PlayableRaceDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RaceDataSyncScheduler {
    private final PlayableRaceDataService playableRaceDataService;

    @EventListener(ApplicationReadyEvent.class)
    public void raceSyncOnStartup() {
        try {
            playableRaceDataService.syncRaces();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Scheduled(cron = "${scheduler.gamedata.sync.cron}")
    public void scheduledRaceSync() {
        try {
            playableRaceDataService.syncRaces();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
