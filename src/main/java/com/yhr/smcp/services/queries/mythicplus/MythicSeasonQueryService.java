package com.yhr.smcp.services.queries.mythicplus;

import com.yhr.smcp.dto.response.mythicplus.MythicSeasonDTO;
import com.yhr.smcp.entities.character.mythicplus.MythicSeason;
import com.yhr.smcp.exceptions.ResourceNotFoundException;
import com.yhr.smcp.mappers.MythicPlusMapper;
import com.yhr.smcp.repositories.character.CharacterRepository;
import com.yhr.smcp.repositories.character.mythicplus.MythicSeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MythicSeasonQueryService {
    private final MythicSeasonRepository mythicSeasonRepository;
    private final CharacterRepository characterRepository;

    public Page<MythicSeasonDTO> getSeasonByProfile(Long profileId, Pageable pageable) {
        return mythicSeasonRepository.findByProfileId(profileId, pageable)
                .map(MythicPlusMapper::buildMythicSeasonDTO);
    }

    public MythicSeasonDTO getCharacterMythicSeason(Long id, Long seasonId) {
        Long profileId = characterRepository.findMythicPlusProfileIdById(id)
                .orElseThrow(() -> new ResourceNotFoundException("characterId=" + id));
        MythicSeason season = mythicSeasonRepository.findByProfileIdAndKeystoneSeasonId(profileId, seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("seasonId" + id));
        return MythicPlusMapper.buildMythicSeasonDTO(season);
    }
}
