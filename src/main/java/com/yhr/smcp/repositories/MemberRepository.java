package com.yhr.smcp.repositories;

import com.yhr.smcp.entities.guild.GuildMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<GuildMember, Long> {
    Optional<GuildMember> findByNameAndRealm(String name, String realm);
}
