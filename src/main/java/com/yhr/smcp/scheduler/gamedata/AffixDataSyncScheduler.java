package com.yhr.smcp.scheduler.gamedata;

import com.yhr.smcp.services.gamedata.KeystoneAffixDataService;
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

    @EventListener(ApplicationReadyEvent.class) //todo: Melhorar os tratamentos de erros
    public void syncOnStartup() {
        try {
            keystoneAffixDataService.syncKeystoneAffixes();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Scheduled(cron = "${scheduler.gamedata.sync.cron}")
    public void scheduledAffixDataSync() {
        try {
            keystoneAffixDataService.syncKeystoneAffixes();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

}
