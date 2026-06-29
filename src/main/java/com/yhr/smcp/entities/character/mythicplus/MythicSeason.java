package com.yhr.smcp.entities.character.mythicplus;

import com.yhr.smcp.entities.character.MythicPlusProfile;
import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneSeason;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.jdbc.JdbcType;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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
