package com.yhr.smcp.services.queries.mythicplus;

import com.yhr.smcp.dto.response.mythicplus.KeystoneRunResponseDTO;
import com.yhr.smcp.entities.character.mythicplus.KeystoneRun;
import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneAffix;
import com.yhr.smcp.mappers.MythicPlusMapper;
import com.yhr.smcp.repositories.character.mythicplus.KeystoneRunRepository;
import com.yhr.smcp.repositories.gamedata.mythicplus.KeystoneAffixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeystoneRunQueryService {
    private final KeystoneRunRepository keystoneRunRepository;
    private final KeystoneAffixRepository keystoneAffixRepository;

    public Page<KeystoneRunResponseDTO> getRunsBySeason(Long seasonId, Pageable pageable) {
        Page<KeystoneRun> runs = keystoneRunRepository.findByMythicSeasonId(seasonId, pageable);
        Map<Integer, KeystoneAffix> affixMap = getAffixMap(runs);

        return runs.map(run -> MythicPlusMapper.buildKeystoneRunDTO(run, affixMap));
    }

    private Map<Integer, KeystoneAffix> getAffixMap(Page<KeystoneRun> runs) {
        List<Integer> allAffixIds = runs.stream()
                .flatMap(run -> run.getAffixIds().stream())
                .distinct()
                .toList();
        return keystoneAffixRepository.findAllById(allAffixIds).stream()
                .collect(Collectors.toMap(KeystoneAffix::getId, Function.identity()));
    }
}
