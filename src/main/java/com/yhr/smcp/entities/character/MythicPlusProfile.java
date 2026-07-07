package com.yhr.smcp.entities.character;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yhr.smcp.entities.guild.GuildMember;
import jakarta.persistence.*;
import lombok.*;

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

}
