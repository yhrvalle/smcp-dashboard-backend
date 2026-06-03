package com.yhr.smcp.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class GuildSnapshot {
    // 1 Guild Snapshot has many Members
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<GuildMember> members;
}
