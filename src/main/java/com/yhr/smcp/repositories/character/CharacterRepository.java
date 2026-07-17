package com.yhr.smcp.repositories.character;

import com.yhr.smcp.entities.character.CharacterProfile;
import com.yhr.smcp.entities.character.mythicplus.MythicPlusProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<CharacterProfile, Long> {
    @Query("select c.mythicPlusProfile.id from CharacterProfile c where c.id = :id")
    Optional<Long> findMythicPlusProfileIdById(@Param("id") Long id);

    @Query("select c.mythicPlusProfile from CharacterProfile c where c.id = :characterId")
    Optional<MythicPlusProfile> findMythicPlusProfileByCharacterId(@Param("characterId") Long characterId);


}
