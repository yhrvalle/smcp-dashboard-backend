package com.yhr.smcp.entities.character.mythicplus;

import com.yhr.smcp.entities.gamedata.PlayableClass;
import com.yhr.smcp.entities.gamedata.PlayableSpecialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeystoneMember {
    private String characterName;
    private String realm;
    private PlayableSpecialization specializationName;
    private PlayableClass playableClass;
    private String race;
    private Double itemLevel;

}
