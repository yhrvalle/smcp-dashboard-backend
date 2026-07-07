package com.yhr.smcp.services.queries.mythicplus;

import com.yhr.smcp.dto.response.mythicplus.MythicSeasonResponseDTO;
import com.yhr.smcp.mappers.MythicPlusMapper;
import com.yhr.smcp.repositories.character.mythicplus.MythicSeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MythicSeasonQueryService {
    private final MythicSeasonRepository mythicSeasonRepository;

    public Page<MythicSeasonResponseDTO> getSeasonByProfile(Long profileId, Pageable pageable) {
        return mythicSeasonRepository.findByProfileId(profileId, pageable)
                .map(MythicPlusMapper::buildMythicSeasonDTO);
    }
}
