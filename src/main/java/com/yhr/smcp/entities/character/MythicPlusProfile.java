package com.yhr.smcp.entities.character;

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
    private Long id; //BUG: ESTÁ NULL

    private Double currentMythicRating;
    private TreeMap<String, Double> ratingColor; // red : valor

    @OneToOne(mappedBy = "mythicPlusProfile")
    private GuildMember member;
}
