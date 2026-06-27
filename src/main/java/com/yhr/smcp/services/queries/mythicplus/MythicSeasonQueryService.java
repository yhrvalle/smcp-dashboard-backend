package com.yhr.smcp.services.queries.mythicplus;

import com.yhr.smcp.entities.character.mythicplus.MythicSeason;
import com.yhr.smcp.repositories.character.mythicplus.MythicSeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MythicSeasonQueryService {
    private final MythicSeasonRepository mythicSeasonRepository;

    public Page<MythicSeason> getSeasonByProfile(Long profileId, Pageable pageable) {
        return mythicSeasonRepository.findByProfileId(profileId, pageable);
    }
}
