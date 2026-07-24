package com.yhr.smcp.repositories.gamedata.mythicplus;

import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KeystoneSeasonsRepository extends JpaRepository<KeystoneSeason, Long> {

    @Query("select a.id from KeystoneSeason a")
    List<Long> findAllIds();
}
