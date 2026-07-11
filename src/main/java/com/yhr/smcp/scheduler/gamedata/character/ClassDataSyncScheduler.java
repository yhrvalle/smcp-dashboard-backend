package com.yhr.smcp.scheduler.gamedata.character;

import com.yhr.smcp.services.gamedata.character.PlayableClassDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClassDataSyncScheduler {
    private final PlayableClassDataService playableClassDataService;

    @EventListener(ApplicationReadyEvent.class)
    public void classSyncOnStartup() {
        try {
            playableClassDataService.syncPlayableClasses();

        } catch (Exception e) {
            log.error("classSyncOnStartup: startup sync failed", e);
        }
    }

    @Scheduled(cron = "${scheduler.gamedata.sync.cron}")
    public void scheduledClassSync() {
        try {
            playableClassDataService.syncPlayableClasses();

        } catch (Exception e) {
            log.error("scheduledClassDataSync: scheduled sync failed", e);
        }
    }

}
