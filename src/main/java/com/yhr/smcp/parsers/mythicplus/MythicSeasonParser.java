package com.yhr.smcp.parsers.mythicplus;

import com.yhr.smcp.entities.character.mythicplus.KeystoneRun;
import com.yhr.smcp.entities.character.mythicplus.MythicSeason;
import com.yhr.smcp.entities.gamedata.PlayableSpecialization;
import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneSeason;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import com.yhr.smcp.util.mythic.RatingColors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
@AllArgsConstructor
public class MythicSeasonParser {
    private final KeystoneRunParser keystoneRunParser;

    public MythicSeason parse(JsonNode season, Map<Integer, PlayableSpecialization> specClassMap, Map<Integer, KeystoneSeason> keystoneSeasonMap) {
        try {
            List<KeystoneRun> runs = new ArrayList<>();
            season.path("best_runs").forEach(run -> {
                runs.add(keystoneRunParser.parse(run, specClassMap));
            });


            Integer id = season.path("season").path("id").asInt();
            // TODO: essa parte aqui está meio estranha pq seria guardar 2 coisas iguais em lugares diferentes
            //  keystoneSeason game data e aqui no perfil, talvez no front que faça esse JOIN TABLES se pá
//            String name = keystoneSeasonMap.get(id).getName();
//            Instant startTimestamp = keystoneSeasonMap.get(id).getStartTimestamp();
//            Instant endTimestamp = keystoneSeasonMap.get(id).getEndTimestamp();

            Double seasonRating = season.path("mythic_rating").path("rating").asDouble();

            JsonNode colors = season.path("mythic_rating").path("color");
            TreeMap<String, Double> ratingColor = new TreeMap<>();
            ratingColor = RatingColors.ratingColorParserUtil(colors);


            return MythicSeason.builder()
                    .id(id)
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
