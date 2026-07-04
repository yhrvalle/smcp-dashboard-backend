package com.yhr.smcp.scheduler.gamedata;

import com.yhr.smcp.services.gamedata.KeystoneAffixDataService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AffixDataSyncScheduler {
    private final KeystoneAffixDataService keystoneAffixDataService;

    @PostConstruct
    public void syncOnStartup() {
        log.info("AffixDataSyncScheduler - running startup sync");
        keystoneAffixDataService.syncKeystoneAffixes();
    }

    @Scheduled(cron = "${scheduler.gamedata.sync.cron}")
    public void scheduledAffixDataSync() {
        log.info("AffixDataSyncScheduler - running scheduled sync");
        keystoneAffixDataService.syncKeystoneAffixes();
    }

}
