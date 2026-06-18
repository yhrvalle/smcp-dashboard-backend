package com.yhr.smcp.entities.character.mythicplus;

import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneAffix;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeystoneRun {
    private Instant completedTimestamp;
    private Instant duration;
    private Integer level;
    private List<KeystoneAffix> affixesName = new ArrayList<>();
    private List<KeystoneMember> members = new ArrayList<KeystoneMember>();

    // Dungeon Data
    private String dungeonName;
    private Boolean isTimed;
    private TreeMap<String, Double> ratingColor;
    private Double dungeonMythicRating;
}
