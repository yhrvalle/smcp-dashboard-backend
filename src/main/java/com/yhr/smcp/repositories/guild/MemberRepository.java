package com.yhr.smcp.repositories.guild;

import com.yhr.smcp.entities.guild.GuildMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<GuildMember, Long> {
}
