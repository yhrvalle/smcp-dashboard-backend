package com.yhr.smcp.repositories.guild;

import com.yhr.smcp.entities.guild.GuildMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuildMemberRepository extends JpaRepository<GuildMember, Long> {
    public Page<GuildMember> findByGuildId(Long guildId, Pageable pageable);

    public Optional<GuildMember> findByRealmAndNameIgnoreCase(String realm, String name);
}
