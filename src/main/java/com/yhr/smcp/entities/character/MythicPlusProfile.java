package com.yhr.smcp.entities.character;

import com.yhr.smcp.entities.character.mythic.MythicSeason;
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
public class MythicPlusProfile {

    private Double currentMythicRating;
    private TreeMap<String, Double> ratingColor; // red : valor
    @Builder.Default
    private List<String> seasonsURL = new ArrayList<>();
    @Builder.Default
    private List<MythicSeason> seasons = new ArrayList<>();
}
