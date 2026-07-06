package com.yhr.smcp.services.queries.mythicplus;

import com.yhr.smcp.dto.response.mythicplus.MythicPlusProfileResponseDTO;
import com.yhr.smcp.entities.character.MythicPlusProfile;
import com.yhr.smcp.mappers.MythicPlusMapper;
import com.yhr.smcp.repositories.character.mythicplus.MythicPlusProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MythicPlusProfileQueryService {
    private final MythicPlusProfileRepository mythicPlusProfileRepository;

    public MythicPlusProfileResponseDTO getProfileById(Long id) {
        MythicPlusProfile profile = mythicPlusProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MythicPlusProfileQueryService: Profile not found: " + id));
        return MythicPlusMapper.buildMythicPlusProfileDTO(profile);

    }
}
