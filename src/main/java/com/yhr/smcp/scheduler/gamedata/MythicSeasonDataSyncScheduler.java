package com.yhr.smcp.scheduler.gamedata;

import com.yhr.smcp.services.gamedata.KeystoneSeasonDataService;
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

    @EventListener(ApplicationReadyEvent.class) //TODO: melhorar os tratamento de error
    public void syncOnStartup() {
        try {
            keystoneSeasonDataService.syncMythicSeasons();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Scheduled(cron = "${scheduler.gamedata.sync.cron}")
    public void scheduledMythicSeasonDataSync() {
        try {
            keystoneSeasonDataService.syncMythicSeasons();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
