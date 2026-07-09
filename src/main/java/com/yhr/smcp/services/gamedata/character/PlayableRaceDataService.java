package com.yhr.smcp.services.gamedata.character;

import com.yhr.smcp.entities.gamedata.character.PlayableRace;
import com.yhr.smcp.parsers.gamedata.character.PlayableRaceParser;
import com.yhr.smcp.repositories.gamedata.character.PlayableRaceRepository;
import com.yhr.smcp.services.BlizzardApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayableRaceDataService {
    private final BlizzardApiService blizzardApiService;
    private final ObjectMapper objectMapper;
    private final PlayableRaceRepository playableRaceRepository;
    private final PlayableRaceParser playableRaceParser;

    public void syncRaces() {
        try {
            String rawJSon = blizzardApiService.getRaceIndex().block();
            JsonNode raceRoot = objectMapper.readTree(rawJSon);
            raceRoot.path("races").forEach(raceNode -> {
                Integer raceId = raceNode.path("id").asInt();
                if (playableRaceRepository.existsById(raceId)) {
                    return;
                }
                PlayableRace race = playableRaceParser.parse(raceNode);
                playableRaceRepository.save(race);
            });
        } catch (Exception e) {
            throw new RuntimeException("PlayableRaceDataService syncRaces error: " + e.getMessage(), e);
        }

    }
}
