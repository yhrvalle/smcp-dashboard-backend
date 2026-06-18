package com.yhr.smcp.entities;

import com.yhr.smcp.entities.character.Achievements;
import com.yhr.smcp.entities.character.MythicPlusProfile;
import com.yhr.smcp.entities.character.PVPSummary;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "tb_members")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuildMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String realm;

    private Integer guildRank;
    private String activeTitle;
    private String gender;
    private String faction;
    private String race;
    private String characterClass;
    private String activeSpecialization;
    private Integer level;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "mythic_plus_profile_id")
    private MythicPlusProfile mythicPlusProfile;

}
