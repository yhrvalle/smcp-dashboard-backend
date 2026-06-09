package com.yhr.smcp.parsers.mythicplus;

import com.yhr.smcp.entities.character.mythic.KeystoneRun;
import com.yhr.smcp.entities.character.mythic.MythicSeason;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import com.yhr.smcp.util.mythic.RatingColors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
@AllArgsConstructor
public class MythicSeasonParser {
    private final KeystoneRunParser keystoneRunParser;

    public MythicSeason parse(JsonNode season, Map<String, String> specClassMap) {
        try {
            List<KeystoneRun> runs = new ArrayList<>();
            season.path("best_runs").forEach(run -> {
                runs.add(keystoneRunParser.parse(run, specClassMap));
            });
            // esses campos está dentro de uma season url nao é o mesmo que esta as best runs
            //String seasonName = season.path("season_name").path("en_US").asString();
            //Double startTime = season.path("start_timestamp").asDouble();
            //Double endTime = season.path("end_timestamp").asDouble();
            Double seasonRating = season.path("mythic_rating").path("rating").asDouble();

            JsonNode colors = season.path("mythic_rating").path("color");
            TreeMap<String, Double> ratingColor = new TreeMap<>();
            ratingColor = RatingColors.ratingColorParserUtil(colors);


            return MythicSeason.builder()

                    // season name, season data url, start time start end
                    .bestRuns(runs)
                    .seasonRating(seasonRating)
                    .ratingColor(ratingColor)
                    .build();

        } catch (BlizzardParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new BlizzardParsingException("MythicSeason", "season=" + season.path("season").path("id").asString(), e);
        }
    }
}
