package com.yhr.smcp.entities.mythic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MythicSeason {
    private String seasonName;
    private Double startTime;
    private Double endTime;
    private List<KeystoneRun> bestRuns = new ArrayList<>();

}
