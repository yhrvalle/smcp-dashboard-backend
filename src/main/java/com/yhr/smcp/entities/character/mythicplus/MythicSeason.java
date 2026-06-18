package com.yhr.smcp.entities.character.mythicplus;

import jakarta.persistence.Id;
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

    private Integer id;
    private List<KeystoneRun> bestRuns = new ArrayList<>();
    private Double seasonRating;
    private TreeMap<String, Double> ratingColor;

}
