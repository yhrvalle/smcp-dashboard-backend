package com.yhr.smcp.scheduler.gamedata.mythicplus;

import com.yhr.smcp.services.gamedata.mythicplus.KeystoneSeasonDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MythicSeasonDataSyncScheduler {
    private final KeystoneSeasonDataService keystoneSeasonDataService;

    @EventListener(ApplicationReadyEvent.class)
    public void mythicSeasonSyncOnStartup() {
        long start = System.currentTimeMillis();
        try {
            keystoneSeasonDataService.syncMythicSeasons();
            log.info("mythicSeasonSyncOnStartup: sync finished in {}ms", System.currentTimeMillis() - start);

        } catch (Exception e) {
            log.error("mythicSeasonSyncOnStartup: startup sync failed", e);
        }
    }

    @Scheduled(cron = "${scheduler.gamedata.sync.cron}")
    public void scheduledMythicSeasonSync() {
        try {
            keystoneSeasonDataService.syncMythicSeasons();
        } catch (Exception e) {
            log.error("scheduledMythicSeasonSync: scheduled sync failed", e);
        }

    }
}
