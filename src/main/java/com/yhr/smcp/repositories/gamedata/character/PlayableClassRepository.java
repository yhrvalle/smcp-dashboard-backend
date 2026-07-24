package com.yhr.smcp.repositories.gamedata.character;

import com.yhr.smcp.entities.gamedata.character.PlayableClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayableClassRepository extends JpaRepository<PlayableClass, Integer> {

    @Query("select a.id from PlayableClass a")
    List<Integer> findAllIds();
}
