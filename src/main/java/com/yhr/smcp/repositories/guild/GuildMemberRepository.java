package com.yhr.smcp.repositories.guild;

import com.yhr.smcp.entities.guild.GuildMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuildMemberRepository extends JpaRepository<GuildMember, Long> {
    public Page<GuildMember> findByGuildId(Long guildId, Pageable pageable);
}
