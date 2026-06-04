package com.yhr.smcp.entities.character.mythic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeystoneRun {
    private Double completedTimestamp;
    private Double duration;
    private Integer level;
    private String affixName;

    private List<KeystoneMember> members = new ArrayList<KeystoneMember>();

    // Dungeon Data
    private String dungeonName;
    private Boolean isTimed;
    private TreeMap<String, Double> ratingColor;
    private Double dungeonMythicRating;
}
