package com.yhr.smcp.services.guild;

import com.yhr.smcp.entities.guild.Guild;
import com.yhr.smcp.services.BlizzardApiService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
public class GuildService {
    private final BlizzardApiService blizzardApiService;
    private final ObjectMapper objectMapper;


}
