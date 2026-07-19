package com.yhr.smcp.mappers;

import com.yhr.smcp.dto.response.mythicplus.*;
import com.yhr.smcp.entities.character.mythicplus.KeystoneRun;
import com.yhr.smcp.entities.character.mythicplus.MythicPlusProfile;
import com.yhr.smcp.entities.character.mythicplus.MythicSeason;
import com.yhr.smcp.entities.gamedata.character.PlayableSpecialization;
import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneAffix;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MythicPlusMapper {

    public static MythicPlusProfileDTO buildMythicPlusProfileDTO(MythicPlusProfile mythicPlusProfile) {
        return new MythicPlusProfileDTO(
                mythicPlusProfile.getId(),
                mythicPlusProfile.getCurrentMythicRating(),
                mythicPlusProfile.getRatingColor()
        );
    }

    public static MythicSeasonDTO buildMythicSeasonDTO(MythicSeason mythicSeason) {
        return new MythicSeasonDTO(
                mythicSeason.getId(),
                mythicSeason.getSeasonRating(),
                mythicSeason.getRatingColor(),
                mythicSeason.getKeystoneSeason().getName(),
                mythicSeason.getKeystoneSeason().getStartTimestamp(),
                mythicSeason.getKeystoneSeason().getEndTimestamp()
        );
    }

    public static KeystoneRunDTO buildKeystoneRunDTO(KeystoneRun keystoneRun, Map<Integer, KeystoneAffix> affixMap) {
        List<AffixDTO> affixes = getAffixDTOS(keystoneRun, affixMap);
        return new KeystoneRunDTO(
                keystoneRun.getId(),
                keystoneRun.getDungeonName(),
                keystoneRun.getLevel(),
                keystoneRun.getIsTimed(),
                keystoneRun.getDungeonMythicRating(),
                keystoneRun.getRatingColor(),
                keystoneRun.getCompletedTimestamp(),
                keystoneRun.getDuration(),
                affixes
        );
    }

    public static KeystoneRunDetailDTO buildKeystoneRunDetailDTO(KeystoneRun keystoneRun, Map<Integer, KeystoneAffix> affixMap,
                                                                 Map<Integer, PlayableSpecialization> specMap) {
        KeystoneRunDTO run = buildKeystoneRunDTO(keystoneRun, affixMap);
        List<KeystoneRunDetailDTO.KeystoneMembersDTO> members = getMembersDTOS(keystoneRun, specMap);
        return new KeystoneRunDetailDTO(
                run,
                members
        );
    }

    private static @NonNull List<KeystoneRunDetailDTO.KeystoneMembersDTO> getMembersDTOS(KeystoneRun keystoneRun,
                                                                                         Map<Integer, PlayableSpecialization> specMap) {
        return keystoneRun.getMembers().stream()
                .map(member -> {
                            PlayableSpecialization spec = specMap.get(member.getSpecializationId());
                            String specName = spec != null ? spec.getName() : null;
                            String className = spec != null ? spec.getPlayableClass().getName() : null;
                            return new KeystoneRunDetailDTO.KeystoneMembersDTO(
                                    member.getCharacterName(),
                                    member.getRealm(),
                                    specName,
                                    className,
                                    member.getRace(),
                                    member.getItemLevel()
                            );
                        }
                )
                .toList();
    }


    private static @NonNull List<AffixDTO> getAffixDTOS(KeystoneRun keystoneRun, Map<Integer, KeystoneAffix> affixMap) {
        return keystoneRun.getAffixIds().stream()
                .map(affixMap::get)
                .filter(Objects::nonNull)
                .map(affix -> new AffixDTO(affix.getId(), affix.getName(), affix.getDescription()))
                .toList();
    }
}
