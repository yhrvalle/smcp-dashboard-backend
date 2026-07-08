package com.yhr.smcp.scheduler.gamedata;

import com.yhr.smcp.services.gamedata.PlayableClassDataService;
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

    @EventListener(ApplicationReadyEvent.class)  //TODO: melhorar os tratamentos de erros
    public void syncOnStartup() {
        try {
            playableClassDataService.syncPlayableClasses();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Scheduled(cron = "${scheduler.gamedata.sync.cron}")
    public void scheduledClassDataSync() {
        try {
            playableClassDataService.syncPlayableClasses();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
