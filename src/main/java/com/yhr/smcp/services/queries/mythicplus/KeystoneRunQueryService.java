package com.yhr.smcp.services.queries.mythicplus;

import com.yhr.smcp.dto.response.mythicplus.KeystoneRunDTO;
import com.yhr.smcp.dto.response.mythicplus.KeystoneRunDetailDTO;
import com.yhr.smcp.entities.character.mythicplus.KeystoneMember;
import com.yhr.smcp.entities.character.mythicplus.KeystoneRun;
import com.yhr.smcp.entities.gamedata.character.PlayableSpecialization;
import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneAffix;
import com.yhr.smcp.mappers.MythicPlusMapper;
import com.yhr.smcp.repositories.character.mythicplus.KeystoneRunRepository;
import com.yhr.smcp.services.gamedata.character.PlayableClassDataService;
import com.yhr.smcp.services.gamedata.mythicplus.KeystoneAffixDataService;
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
    private final KeystoneAffixDataService keystoneAffixDataService;
    private final PlayableClassDataService playableClassDataService;


    public Page<KeystoneRunDTO> getRunsBySeason(Long seasonId, Pageable pageable) {
        Page<KeystoneRun> runs = keystoneRunRepository.findByMythicSeasonId(seasonId, pageable);
        Map<Integer, KeystoneAffix> affixMap = getAffixMap(runs.getContent());

        return runs.map(run -> MythicPlusMapper.buildKeystoneRunDTO(run, affixMap));
    }

    public KeystoneRunDetailDTO getRunDetailById(Long runId) {
        KeystoneRun run = keystoneRunRepository.findById(runId).orElseThrow(() -> new RuntimeException("keystoneRunQueryService, run not found id=" + runId));
        Map<Integer, KeystoneAffix> affixMap = getAffixMap(List.of(run));
        Map<Integer, PlayableSpecialization> specializationMap = getSpecializationMap(List.of(run));

        return MythicPlusMapper.buildKeystoneRunDetailDTO(run, affixMap, specializationMap);
    }

    private Map<Integer, KeystoneAffix> getAffixMap(List<KeystoneRun> runs) {
        List<Integer> allAffixIds = runs.stream()
                .flatMap(run -> run.getAffixIds().stream())
                .distinct()
                .toList();
        return keystoneAffixDataService.findAllKeystoneAffixByIds(allAffixIds).stream()
                .collect(Collectors.toMap(KeystoneAffix::getId, Function.identity()));
    }

    private Map<Integer, PlayableSpecialization> getSpecializationMap(List<KeystoneRun> runs) {
        List<Integer> allSpecIds = runs.stream()
                .flatMap(run -> run.getMembers().stream())
                .map(KeystoneMember::getSpecializationId)
                .distinct()
                .toList();
        return playableClassDataService.findAllPlayableSpecializationsByIds(allSpecIds).stream()
                .collect(Collectors.toMap(PlayableSpecialization::getId, Function.identity()));

    }
}
