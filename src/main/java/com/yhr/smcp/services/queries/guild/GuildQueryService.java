package com.yhr.smcp.services.queries.guild;

import com.yhr.smcp.dto.response.guild.GuildDTO;
import com.yhr.smcp.entities.guild.Guild;
import com.yhr.smcp.mappers.GuildMapper;
import com.yhr.smcp.repositories.guild.GuildRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuildQueryService {

    private final GuildRepository guildRepository;

    public GuildDTO getGuild(Long guildId) {
        Guild guild = guildRepository.findById(guildId).orElseThrow(
                () -> new RuntimeException("GuildQueryService = guild=" + guildId + " not found")
        );
        return GuildMapper.toGuildDTO(guild);
    }
}
