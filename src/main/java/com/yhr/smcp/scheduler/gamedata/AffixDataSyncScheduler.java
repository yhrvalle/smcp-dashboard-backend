package com.yhr.smcp.scheduler.gamedata;

import com.yhr.smcp.services.gamedata.KeystoneAffixService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AffixDataSyncScheduler {
    private final KeystoneAffixService keystoneAffixService;

    @PostConstruct
    public void syncOnStartup() {
        log.info("AffixDataSyncScheduler - running startup sync");
        keystoneAffixService.syncKeystoneAffixes();
    }

    @Scheduled(cron = "${scheduler.gamedata.sync.cron}")
    public void scheduledAffixDataSync() {
        log.info("AffixDataSyncScheduler - running scheduled sync");
        keystoneAffixService.syncKeystoneAffixes();
    }

}
