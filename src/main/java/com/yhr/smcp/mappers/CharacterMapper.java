package com.yhr.smcp.mappers;

import com.yhr.smcp.dto.response.character.CharacterProfileDTO;
import com.yhr.smcp.entities.character.CharacterProfile;
import com.yhr.smcp.entities.guild.GuildMember;

public class CharacterMapper {
    public static CharacterProfileDTO toCharacterProfileDTO(CharacterProfile charProfile,
                                                            String className, String raceName, String specName) {
        GuildMember guildMember = charProfile.getGuildMember();
        Double currentMythicRating = charProfile.getMythicPlusProfile() != null ? charProfile.getMythicPlusProfile().getCurrentMythicRating() : null;
        String ratingColorHex = charProfile.getMythicPlusProfile() != null ? charProfile.getMythicPlusProfile().getRatingColor() : null;
        return new CharacterProfileDTO(
                guildMember.getId(),
                guildMember.getName(),
                guildMember.getRealm(),
                charProfile.getActiveTitle(),
                guildMember.getLevel(),
                charProfile.getItemLevel(),
                charProfile.getFaction(),
                charProfile.getGender(),
                raceName,
                className,
                specName,
                currentMythicRating,
                ratingColorHex
        );
    }
}
