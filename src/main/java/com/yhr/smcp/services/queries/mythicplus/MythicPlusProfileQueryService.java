package com.yhr.smcp.services.queries.mythicplus;

import com.yhr.smcp.entities.character.MythicPlusProfile;
import com.yhr.smcp.repositories.character.mythicplus.MythicPlusProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MythicPlusProfileQueryService {
    private final MythicPlusProfileRepository mythicPlusProfileRepository;

    public MythicPlusProfile getProfileById(Long id) {
        return mythicPlusProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MythicPlusProfileQueryService: Profile not found: " + id));
    }
}
