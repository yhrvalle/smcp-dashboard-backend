package com.yhr.smcp.services.queries.character;

import com.yhr.smcp.dto.response.character.CharacterProfileDTO;
import com.yhr.smcp.entities.character.CharacterProfile;
import com.yhr.smcp.entities.gamedata.character.PlayableClass;
import com.yhr.smcp.entities.gamedata.character.PlayableRace;
import com.yhr.smcp.entities.gamedata.character.PlayableSpecialization;
import com.yhr.smcp.entities.guild.GuildMember;
import com.yhr.smcp.mappers.CharacterMapper;
import com.yhr.smcp.repositories.character.CharacterRepository;
import com.yhr.smcp.services.gamedata.character.PlayableClassDataService;
import com.yhr.smcp.services.gamedata.character.PlayableRaceDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CharacterProfileQueryService {
    private final CharacterRepository characterRepository;
    private final PlayableClassDataService playableClassDataService;
    private final PlayableRaceDataService playableRaceDataService;

    public CharacterProfileDTO getCharacterProfile(Long id) {
        CharacterProfile profile = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CharacterProfileQueryService, character " + id + " not found!")
                );
        String className = getClassName(profile.getGuildMember());
        String raceName = getRaceName(profile.getGuildMember());
        String specName = getSpecName(profile);
        return CharacterMapper.toCharacterProfileDTO(profile, className, raceName, specName);
    }

    //TODO: pensar se é melhor retornar algo ou só null mesmo
    private String getSpecName(CharacterProfile profile) {
        PlayableSpecialization spec = playableClassDataService.findPlayableSpecializationById(profile.getActiveSpecializationId());
        return spec != null ? spec.getName() : null;
    }


    private String getClassName(GuildMember guildMember) {
        PlayableClass playableClass = playableClassDataService.findPlayableClassById(guildMember.getClassId());
        return playableClass != null ? playableClass.getName() : null;
    }

    private String getRaceName(GuildMember guildMember) {
        PlayableRace playableRace = playableRaceDataService.findPlayableRaceId(guildMember.getRaceId());
        return playableRace != null ? playableRace.getName() : null;
    }
}
