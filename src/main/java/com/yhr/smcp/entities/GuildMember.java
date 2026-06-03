package com.yhr.smcp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "guild_members")
public class GuildMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String characterName;

    @Column(nullable = false)
    private String realm;

    private int guildRank;
    private String activeTitle;
    private String gender;
    private String faction;
    private String race;
    private String characterClass;
    private String activeSpecialization;
    private Double achievementPoints;
    private Integer level;


}
