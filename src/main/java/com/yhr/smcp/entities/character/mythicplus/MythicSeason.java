package com.yhr.smcp.entities.character.mythicplus;

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
    private String seasonDataURL; // TODO: ao inves de pegar a URL, pegar apenas o ID e get no DB local (que vou fazer ainda)
    private Double startTime;     //     Assim essas infos não precisam ser requests a partir da Profile API (nested)
    private Double endTime;
    private List<KeystoneRun> bestRuns = new ArrayList<>();
    private Double seasonRating;
    private TreeMap<String, Double> ratingColor;

}
