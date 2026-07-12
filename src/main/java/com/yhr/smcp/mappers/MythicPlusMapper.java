package com.yhr.smcp.mappers;

import com.yhr.smcp.dto.response.mythicplus.*;
import com.yhr.smcp.entities.character.mythicplus.MythicPlusProfile;
import com.yhr.smcp.entities.character.mythicplus.KeystoneRun;
import com.yhr.smcp.entities.character.mythicplus.MythicSeason;
import com.yhr.smcp.entities.gamedata.mythicplus.KeystoneAffix;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MythicPlusMapper {

    public static MythicPlusProfileResponseDTO buildMythicPlusProfileDTO(MythicPlusProfile mythicPlusProfile) {
        return new MythicPlusProfileResponseDTO(
                mythicPlusProfile.getId(),
                mythicPlusProfile.getCurrentMythicRating(),
                mythicPlusProfile.getRatingColor()
        );
    }

    public static MythicSeasonResponseDTO buildMythicSeasonDTO(MythicSeason mythicSeason) {
        return new MythicSeasonResponseDTO(
                mythicSeason.getId(),
                mythicSeason.getSeasonRating(),
                mythicSeason.getRatingColor(),
                mythicSeason.getKeystoneSeason().getName(),
                mythicSeason.getKeystoneSeason().getStartTimestamp(),
                mythicSeason.getKeystoneSeason().getEndTimestamp()
        );
    }

    public static KeystoneRunResponseDTO buildKeystoneRunDTO(KeystoneRun keystoneRun, Map<Integer, KeystoneAffix> affixMap) {
        List<AffixDTO> affixes = getAffixDTOS(keystoneRun, affixMap);
        return new KeystoneRunResponseDTO(
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

    public static KeystoneRunDetailResponseDTO buildKeystoneRunDetailDTO(KeystoneRun keystoneRun, Map<Integer, KeystoneAffix> affixMap) {
        List<AffixDTO> affixes = getAffixDTOS(keystoneRun, affixMap);
        List<KeystoneRunDetailResponseDTO.KeystoneMembersDTO> members = getMembersDTOS(keystoneRun);
        return new KeystoneRunDetailResponseDTO(
                keystoneRun.getId(),
                keystoneRun.getDungeonName(),
                keystoneRun.getLevel(),
                keystoneRun.getDungeonMythicRating(),
                keystoneRun.getIsTimed(),
                keystoneRun.getRatingColor(),
                keystoneRun.getCompletedTimestamp(),
                keystoneRun.getDuration(),
                affixes,
                members
        );
    }


    // extracted methods //TODO: fix this
    private static @NonNull List<KeystoneRunDetailResponseDTO.KeystoneMembersDTO> getMembersDTOS(KeystoneRun keystoneRun) {
        return keystoneRun.getMembers().stream()
                .map(member -> new KeystoneRunDetailResponseDTO.KeystoneMembersDTO(
                        member.getCharacterName(),
                        member.getRealm(),

                        member.getRace(),
                        member.getItemLevel()
                ))
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
