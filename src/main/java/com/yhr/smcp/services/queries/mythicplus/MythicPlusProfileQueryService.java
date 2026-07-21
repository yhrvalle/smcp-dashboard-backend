package com.yhr.smcp.services.queries.mythicplus;

import com.yhr.smcp.dto.response.mythicplus.MythicPlusProfileDTO;
import com.yhr.smcp.entities.character.mythicplus.MythicPlusProfile;
import com.yhr.smcp.exceptions.ResourceNotFoundException;
import com.yhr.smcp.mappers.MythicPlusMapper;
import com.yhr.smcp.repositories.character.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MythicPlusProfileQueryService {
    private final CharacterRepository characterRepository;

    public MythicPlusProfileDTO getCharacterMythicProfile(Long id) {
        MythicPlusProfile profile = characterRepository.findMythicPlusProfileByCharacterId(id)
                .orElseThrow(() -> new ResourceNotFoundException("profile=" + id));
        return MythicPlusMapper.buildMythicPlusProfileDTO(profile);

    }


}

