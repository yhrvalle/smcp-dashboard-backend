package com.yhr.smcp.entities.character;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yhr.smcp.entities.GuildMember;
import com.yhr.smcp.entities.character.mythicplus.MythicSeason;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_mythic_plus_profile")
public class MythicPlusProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double currentMythicRating;

    private String ratingColor;

    @OneToOne(mappedBy = "mythicPlusProfile")
    @JsonIgnore
    private GuildMember member; // Irá causar loop, TODO: Criar DTO's
}
