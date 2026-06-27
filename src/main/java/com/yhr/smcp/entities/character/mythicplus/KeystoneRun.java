package com.yhr.smcp.entities.character.mythicplus;

import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneAffix;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Entity
@Table(name = "tb_mythic_runs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeystoneRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mythic_season_id", nullable = false)
    private MythicSeason mythicSeason;

    private Instant completedTimestamp;
    private Instant duration;
    private Integer level;
    private List<KeystoneAffix> affixesName = new ArrayList<>();
    @JdbcTypeCode(SqlTypes.JSON)
    private List<KeystoneMember> members = new ArrayList<KeystoneMember>();

    // Dungeon Data
    private String dungeonName;
    private Boolean isTimed;
    private TreeMap<String, Double> ratingColor;
    private Double dungeonMythicRating;
}
