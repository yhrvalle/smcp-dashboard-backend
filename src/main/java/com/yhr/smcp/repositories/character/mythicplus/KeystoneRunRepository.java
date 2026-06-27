package com.yhr.smcp.repositories.character.mythicplus;

import com.yhr.smcp.entities.character.mythicplus.KeystoneRun;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeystoneRunRepository extends JpaRepository<KeystoneRun, Long> {
    Page<KeystoneRun> findByMythicSeasonId(Long mythicSeasonId, Pageable pageable);

}
