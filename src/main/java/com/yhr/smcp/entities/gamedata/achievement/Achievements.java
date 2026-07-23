package com.yhr.smcp.entities.gamedata.achievement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@Table(name = "tb_gamedata_achievements")
@NoArgsConstructor
@AllArgsConstructor
public class Achievements {
    @Id
    private Long id;
    private String name;
    
    @Column(length = 1000)
    private String description;
    private Integer points;
    private String category;

}
