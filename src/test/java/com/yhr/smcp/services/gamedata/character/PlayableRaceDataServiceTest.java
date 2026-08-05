package com.yhr.smcp.services.gamedata.character;

import com.yhr.smcp.client.BlizzardStaticApiClient;
import com.yhr.smcp.entities.gamedata.character.PlayableRace;
import com.yhr.smcp.exceptions.BlizzardSyncException;
import com.yhr.smcp.parsers.gamedata.character.PlayableRaceParser;
import com.yhr.smcp.repositories.gamedata.character.PlayableRaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import reactor.core.publisher.Mono;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PlayableRaceDataServiceTest {
    @Mock
    private BlizzardStaticApiClient blizzardStaticApiClient;
    @Mock
    private PlayableRaceRepository playableRaceRepository;
    @Mock
    private PlayableRaceParser playableRaceParser;
    private PlayableRaceDataService playableRaceDataService;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = JsonMapper.builder().build();
        playableRaceDataService = new PlayableRaceDataService(
                blizzardStaticApiClient,
                objectMapper,
                playableRaceRepository,
                playableRaceParser
        );
    }

    @Test
    void syncRaces_whenRaceAlreadyExists() {
        String indexJson = """
                {
                   "races": [
                     { "id": 1, "name": "Human" }
                   ]
                }
                """;

        when(blizzardStaticApiClient.getRaceIndex()).thenReturn(Mono.just(indexJson));
        when(playableRaceRepository.existsById(1)).thenReturn(true);

        playableRaceDataService.syncRaces();

        verify(playableRaceParser, never()).parse(any());
        verify(playableRaceRepository, never()).save(any());
    }

    @Test
    void syncRaces_whenRaceIsNew() {
        String indexJson = """
                {
                   "races": [
                     { "id": 2, "name": "Orc" }
                   ]
                }
                """;
        PlayableRace parsedRace = PlayableRace.builder().id(2).name("Orc").build();

        when(blizzardStaticApiClient.getRaceIndex()).thenReturn(Mono.just(indexJson));
        when(playableRaceRepository.existsById(2)).thenReturn(false);
        when(playableRaceParser.parse(any())).thenReturn(parsedRace);

        playableRaceDataService.syncRaces();

        verify(playableRaceRepository).save(parsedRace);
    }

    @Test
    void syncRaces_whenFetchFails() {
        when(blizzardStaticApiClient.getRaceIndex())
                .thenReturn(Mono.error(new RuntimeException("timeout")));

        assertThatExceptionOfType(BlizzardSyncException.class)
                .isThrownBy(() -> playableRaceDataService.syncRaces());
    }

    @Test
    void findPlayableRaceId_whenRaceDoesNotExist() {
        when(playableRaceRepository.findById(99)).thenReturn(java.util.Optional.empty());

        PlayableRace result = playableRaceDataService.findPlayableRaceId(99);

        assertThat(result).isNull();
    }

}
