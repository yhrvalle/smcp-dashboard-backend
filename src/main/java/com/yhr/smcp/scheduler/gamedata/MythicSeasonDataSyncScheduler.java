package com.yhr.smcp.scheduler.gamedata;

import com.yhr.smcp.services.gamedata.KeystoneSeasonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MythicSeasonDataSyncScheduler {
    private final KeystoneSeasonService keystoneSeasonService;

    @Scheduled(cron = "${scheduler.gamedata.sync.cron}")
    public void syncMythicSeasonData() {


    }
}
