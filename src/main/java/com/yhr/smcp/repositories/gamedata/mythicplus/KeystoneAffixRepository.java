package com.yhr.smcp.repositories.gamedata.mythicplus;

import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneAffix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KeystoneAffixRepository extends JpaRepository<KeystoneAffix, Integer> {

    @Query("select a.id from KeystoneAffix a")
    List<Integer> findAllIds();
}
