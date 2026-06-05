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
public class MythicSeason {
    private String seasonName;
    private String seasonDataURL;
    private Double startTime;
    private Double endTime;
    private List<KeystoneRun> bestRuns = new ArrayList<>();
    private Double seasonRating;
    private TreeMap<String, Double> ratingColor;

}
