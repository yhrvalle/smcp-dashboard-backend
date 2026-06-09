package com.yhr.smcp.entities.gamedata;

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
@Table(name = "playable_class")
@NoArgsConstructor
@AllArgsConstructor
public class PlayableClass {
    @Id
    private Integer id;
    private String className;

}
