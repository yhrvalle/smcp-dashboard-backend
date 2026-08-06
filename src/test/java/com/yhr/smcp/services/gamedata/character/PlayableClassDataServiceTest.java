package com.yhr.smcp.services.gamedata.character;

import com.yhr.smcp.client.BlizzardStaticApiClient;
import com.yhr.smcp.parsers.gamedata.character.PlayableClassesParser;
import com.yhr.smcp.parsers.gamedata.character.PlayableSpecializationsParser;
import com.yhr.smcp.repositories.gamedata.character.PlayableClassRepository;
import com.yhr.smcp.repositories.gamedata.character.PlayableSpecializationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@ExtendWith(MockitoExtension.class)
class PlayableClassDataServiceTest {

    @Mock
    private BlizzardStaticApiClient blizzardStaticApiClient;
    @Mock
    private PlayableClassRepository playableClassRepository;
    @Mock
    private PlayableSpecializationRepository playableSpecializationRepository;
    @Mock
    private PlayableClassesParser playableClassesParser;
    @Mock
    private PlayableSpecializationsParser playableSpecializationsParser;
    private PlayableClassDataService playableClassDataService;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = JsonMapper.builder().build();
        playableClassDataService = new PlayableClassDataService(playableClassRepository,
                playableSpecializationRepository,
                playableClassesParser,
                playableSpecializationsParser,
                blizzardStaticApiClient,
                objectMapper);
    }


}
