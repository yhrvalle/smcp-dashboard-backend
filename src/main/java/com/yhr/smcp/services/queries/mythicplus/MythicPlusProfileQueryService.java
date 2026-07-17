package com.yhr.smcp.services.queries.mythicplus;

import com.yhr.smcp.dto.response.mythicplus.MythicPlusProfileDTO;
import com.yhr.smcp.dto.response.mythicplus.MythicSeasonDTO;
import com.yhr.smcp.entities.character.CharacterProfile;
import com.yhr.smcp.entities.character.mythicplus.MythicPlusProfile;
import com.yhr.smcp.entities.character.mythicplus.MythicSeason;
import com.yhr.smcp.mappers.MythicPlusMapper;
import com.yhr.smcp.repositories.character.CharacterRepository;
import com.yhr.smcp.repositories.character.mythicplus.MythicPlusProfileRepository;
import com.yhr.smcp.repositories.character.mythicplus.MythicSeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MythicPlusProfileQueryService {
    private final MythicSeasonRepository mythicSeasonRepository;
    private final CharacterRepository characterRepository;

    public MythicPlusProfileDTO getCharacterMythicProfile(Long id) {
        MythicPlusProfile profile = characterRepository.findMythicPlusProfileByCharacterId(id)
                .orElseThrow(() -> new RuntimeException("MythicPlusProfileQueryService: Profile not found: " + id));
        return MythicPlusMapper.buildMythicPlusProfileDTO(profile);

    }


}

