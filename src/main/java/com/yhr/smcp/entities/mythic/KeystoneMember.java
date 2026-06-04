package com.yhr.smcp.entities.mythic;

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
    private String specializationName;
    private String playableClass; // url dentro
    private String race;
    private Double itemLevel;

}
