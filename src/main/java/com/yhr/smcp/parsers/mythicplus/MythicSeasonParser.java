package com.yhr.smcp.parsers.mythicplus;

import com.yhr.smcp.entities.character.mythicplus.KeystoneRun;
import com.yhr.smcp.entities.character.mythicplus.MythicSeason;
import com.yhr.smcp.entities.gamedata.character.PlayableSpecialization;
import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneAffix;
import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneSeason;
import com.yhr.smcp.exceptions.BlizzardParsingException;
import com.yhr.smcp.util.mythic.RatingColors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class MythicSeasonParser {
    private final KeystoneRunParser keystoneRunParser;

    public SeasonParserResult parse(JsonNode season, Map<Integer, PlayableSpecialization> specClassMap,
                                    Map<Integer, KeystoneSeason> keystoneSeasonMap, Map<Integer, KeystoneAffix> keystoneAffixMap) {
        try {
            List<KeystoneRun> runs = new ArrayList<>();
            season.path("best_runs").forEach(run -> {
                runs.add(keystoneRunParser.parse(run, specClassMap, keystoneAffixMap));
            });

            Integer id = season.path("season").path("id").asInt();
            KeystoneSeason keystoneSeason = keystoneSeasonMap.get(id);

            Double seasonRating = season.path("mythic_rating").path("rating").asDouble();

            JsonNode colors = season.path("mythic_rating").path("color");
            String ratingColor = RatingColors.ratingColorParserUtil(colors);

            MythicSeason mythicSeason = MythicSeason.builder()
                    .seasonRating(seasonRating)
                    .ratingColor(ratingColor)
                    .keystoneSeason(keystoneSeason)
                    .build();
            return new SeasonParserResult(mythicSeason, runs);
        } catch (BlizzardParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new BlizzardParsingException("MythicSeason", "season=" + season.path("season").path("id").asString(), e);
        }
    }

    public record SeasonParserResult(MythicSeason mythicSeason, List<KeystoneRun> keystoneRuns) {
    }
}
