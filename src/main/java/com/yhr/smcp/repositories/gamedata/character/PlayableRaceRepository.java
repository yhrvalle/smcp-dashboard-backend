package com.yhr.smcp.repositories.gamedata.character;

import com.yhr.smcp.entities.gamedata.character.PlayableRace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayableRaceRepository extends JpaRepository<PlayableRace, Integer> {
    @Query("select a.id from PlayableRace a")
    List<Integer> findAllIds();
}
