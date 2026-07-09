package com.yhr.smcp.services.queries.guild;

import com.yhr.smcp.dto.response.guild.GuildMemberDTO;
import com.yhr.smcp.entities.gamedata.character.PlayableClass;
import com.yhr.smcp.entities.gamedata.character.PlayableRace;
import com.yhr.smcp.entities.guild.GuildMember;
import com.yhr.smcp.mappers.GuildMapper;
import com.yhr.smcp.repositories.guild.GuildMemberRepository;
import com.yhr.smcp.services.gamedata.character.PlayableClassDataService;
import com.yhr.smcp.services.gamedata.character.PlayableRaceDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuildMemberQueryService {
    private final GuildMemberRepository guildMemberRepository;
    private final PlayableClassDataService playableClassDataService;
    private final PlayableRaceDataService playableRaceDataService;

    public Page<GuildMemberDTO> getGuildRoster(Long guildId, Pageable pageable) {
        return guildMemberRepository.findByGuildId(guildId, pageable)
                .map(member -> {
                    String className = getClassName(member);
                    String raceName = getRaceName(member);
                    return GuildMapper.toGuildMemberDTO(member, className, raceName);
                });
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
