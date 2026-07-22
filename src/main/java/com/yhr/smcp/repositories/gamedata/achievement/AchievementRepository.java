package com.yhr.smcp.repositories.gamedata.achievement;

import com.yhr.smcp.entities.gamedata.achievement.Achievements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievements, Long> {
}
