package com.yhr.smcp.scheduler.gamedata;

import com.yhr.smcp.services.gamedata.PlayableClassDataService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClassDataSyncScheduler {
    private final PlayableClassDataService playableClassDataService;

    @PostConstruct
    public void syncOnStartup() {
        log.info("ClassDataSyncScheduler - run on startup");
        playableClassDataService.syncPlayableClasses();
    }

    @Scheduled(cron = "${scheduler.gamedata.sync.cron}")
    public void scheduledClassDataSync() {
        log.info("ClassDataSyncScheduler - running scheduled sync");
        playableClassDataService.syncPlayableClasses();
    }

}
