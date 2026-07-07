package com.yhr.smcp.entities.character.mythicplus;

import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneSeason;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_mythic_profile_seasons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MythicSeason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double seasonRating;

    private String ratingColor;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private MythicPlusProfile profile;

    @ManyToOne
    @JoinColumn(name = "keystone_season_id", nullable = false)
    private KeystoneSeason keystoneSeason;
}
