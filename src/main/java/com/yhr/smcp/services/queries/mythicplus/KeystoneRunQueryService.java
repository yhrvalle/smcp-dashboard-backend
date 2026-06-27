package com.yhr.smcp.services.queries.mythicplus;

import com.yhr.smcp.entities.character.mythicplus.KeystoneRun;
import com.yhr.smcp.repositories.character.mythicplus.KeystoneRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeystoneRunQueryService {
    private final KeystoneRunRepository keystoneRunRepository;

    public Page<KeystoneRun> getRunsBySeason(Long profileId, Pageable pageable) {
        return keystoneRunRepository.findByMythicSeasonId(profileId, pageable);
    }
}
