package com.yhr.smcp.services.queries.mythicplus;

import com.yhr.smcp.dto.response.mythicplus.KeystoneRunDetailResponseDTO;
import com.yhr.smcp.dto.response.mythicplus.KeystoneRunResponseDTO;
import com.yhr.smcp.entities.character.mythicplus.KeystoneRun;
import com.yhr.smcp.mappers.MythicPlusMapper;
import com.yhr.smcp.repositories.character.mythicplus.KeystoneRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeystoneRunQueryService {
    private final KeystoneRunRepository keystoneRunRepository;

    public Page<KeystoneRunResponseDTO> getRunsBySeason(Long seasonId, Pageable pageable) {
        return keystoneRunRepository.findByMythicSeasonId(seasonId, pageable)
                .map(MythicPlusMapper::buildKeystoneRunDTO);
    }

    public KeystoneRunDetailResponseDTO getRunById(Long runId) {
        KeystoneRun run = keystoneRunRepository.findById(runId).orElseThrow(() -> new RuntimeException("KeystoneRunQueryService: Run not found:" + runId));
        return MythicPlusMapper.buildKeystoneRunDetailDTO(run);
    }
}
