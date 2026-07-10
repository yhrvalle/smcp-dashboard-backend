package com.yhr.smcp.entities.character;

import com.yhr.smcp.entities.character.mythicplus.MythicPlusProfile;
import com.yhr.smcp.entities.guild.GuildMember;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_character_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterProfile {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private GuildMember guildMember;

    private String activeTitle;
    private String gender;
    private String faction;
    private Integer activeSpecializationId;
    private Long itemLevel;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "mythic_plus_profile_id")
    private MythicPlusProfile mythicPlusProfile;


}
