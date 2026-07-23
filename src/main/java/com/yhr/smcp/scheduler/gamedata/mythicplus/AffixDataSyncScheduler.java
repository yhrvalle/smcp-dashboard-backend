package com.yhr.smcp.scheduler.gamedata.mythicplus;

import com.yhr.smcp.services.gamedata.mythicplus.KeystoneAffixDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AffixDataSyncScheduler {
    private final KeystoneAffixDataService keystoneAffixDataService;

    @EventListener(ApplicationReadyEvent.class)
    public void affixSyncOnStartup() {
        long start = System.currentTimeMillis();
        try {
            keystoneAffixDataService.syncKeystoneAffixes();
            log.info("affixSyncOnStartup: sync finished in {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("affixSyncOnStartup: startup sync failed", e);
        }

    }

    @Scheduled(cron = "${scheduler.gamedata.sync.cron}")
    public void scheduledAffixSync() {
        try {
            keystoneAffixDataService.syncKeystoneAffixes();
        } catch (Exception e) {
            log.error("scheduledAffixSync: scheduled sync failed", e);
        }

    }

}
