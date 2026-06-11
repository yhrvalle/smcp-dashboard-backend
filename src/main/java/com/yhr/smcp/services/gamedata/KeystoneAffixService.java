package com.yhr.smcp.services.gamedata;

import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneAffix;
import com.yhr.smcp.parsers.gamedata.mythicplus.KeystoneAffixParser;
import com.yhr.smcp.repositories.gamedata.mythicplus.KeystoneAffixRepository;
import com.yhr.smcp.services.BlizzardApiService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeystoneAffixService {
    private final KeystoneAffixRepository keystoneAffixRepository;
    private final KeystoneAffixParser keystoneAffixParser;
    private final BlizzardApiService blizzardApiService;
    private final ObjectMapper objectMapper;

    @Transactional
    public void syncKeystoneAffixes() {
        try {
            String rawIndexJson = blizzardApiService.getAffixIndex().block();
            JsonNode indexRoot = objectMapper.readTree(rawIndexJson);
            indexRoot.path("affixes").forEach(affix -> {
                Integer affixId = affix.path("id").asInt();
                String affixDetailsJson = blizzardApiService.getAffixDetails(affixId).block();
                JsonNode affixRoot = objectMapper.readTree(affixDetailsJson);
                KeystoneAffix keystoneAffix = keystoneAffixParser.parse(affixRoot);
                keystoneAffixRepository.save(keystoneAffix);

            });
        } catch (Exception e) {
            throw new RuntimeException("Keystone Affix sync failed: " + e.getMessage(), e);
        }
    }

    public KeystoneAffix findKeystoneAffixById(Integer affixId) {
        return keystoneAffixRepository.findById(affixId).orElse(null);
    }

    public void saveKeystoneAffix(KeystoneAffix keystoneAffix) {
        keystoneAffixRepository.save(keystoneAffix);
    }
}
