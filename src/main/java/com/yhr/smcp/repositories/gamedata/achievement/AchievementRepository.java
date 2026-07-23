package com.yhr.smcp.repositories.gamedata.achievement;

import com.yhr.smcp.entities.gamedata.achievement.Achievements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievements, Long> {
    @Query("select a.id from Achievements a")
    List<Long> findAllIds();
}
