package com.yhr.smcp.repositories.character;

import com.yhr.smcp.entities.character.CharacterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<CharacterProfile, Long> {
}
