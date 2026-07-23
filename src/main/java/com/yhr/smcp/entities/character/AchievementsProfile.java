package com.yhr.smcp.entities.character;

import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Builder
@RequiredArgsConstructor
@Table(name = "tb_achievements")
public class AchievementsProfile {
    //TODO: olhar o json desse endpoint

}
