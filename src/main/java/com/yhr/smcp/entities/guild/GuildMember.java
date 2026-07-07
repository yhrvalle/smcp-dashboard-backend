package com.yhr.smcp.entities.guild;

import com.yhr.smcp.entities.character.mythicplus.MythicPlusProfile;
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
    private Integer raceId;
    private Integer classId;
    private Integer level;


}
