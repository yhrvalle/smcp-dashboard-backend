package com.yhr.smcp.scheduler.gamedata;

import com.yhr.smcp.services.gamedata.KeystoneSeasonService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MythicSeasonDataSyncScheduler {
    private final KeystoneSeasonService keystoneSeasonService;

    @PostConstruct
    public void syncOnStartup() {
        log.info("MythicSeasonDataSyncScheduler - run on startup");
        keystoneSeasonService.syncMythicSeasons();
    }

    @Scheduled(cron = "${scheduler.gamedata.sync.cron}")
    public void scheduledMythicSeasonDataSync() {
        log.info("MythicSeasonDataSyncScheduler - running scheduled sync");
        keystoneSeasonService.syncMythicSeasons();
    }
}
