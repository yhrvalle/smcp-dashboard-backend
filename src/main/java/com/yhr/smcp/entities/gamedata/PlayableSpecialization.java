package com.yhr.smcp.entities.gamedata;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@Table(name = "specialization")
@NoArgsConstructor
@AllArgsConstructor
public class PlayableSpecialization {
    @Id
    private Integer id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private PlayableClass playableClass;

}
