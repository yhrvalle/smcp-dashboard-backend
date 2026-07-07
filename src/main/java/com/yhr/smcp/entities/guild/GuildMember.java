package com.yhr.smcp.entities.guild;

import com.yhr.smcp.entities.character.MythicPlusProfile;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "tb_guild_members")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuildMember {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "guild_id", nullable = false)
    private Guild guild;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String realm;

    private Integer guildRank;
    private String activeTitle;
    private String gender;
    private String faction;
    private Integer raceId;
    private Integer classId;
    private Integer activeSpecializationId;
    private Integer level;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "mythic_plus_profile_id")
    private MythicPlusProfile mythicPlusProfile;

}
