package com.yhr.smcp.services.queries.guild;

import com.yhr.smcp.dto.response.guild.GuildMemberDTO;
import com.yhr.smcp.entities.gamedata.character.PlayableClass;
import com.yhr.smcp.mappers.GuildMapper;
import com.yhr.smcp.repositories.guild.GuildMemberRepository;
import com.yhr.smcp.services.gamedata.character.PlayableClassDataService;
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

    public Page<GuildMemberDTO> getGuildRoster(Long guildId, Pageable pageable) {
        return guildMemberRepository.findByGuildId(guildId, pageable)
                .map(member -> {
                    PlayableClass playableClass = playableClassDataService.findPlayableClassById(member.getClassId());
                    String className = playableClass != null ? playableClass.getName() : null;
                    return GuildMapper.toGuildMemberDTO(member, className);
                });
    }
}
