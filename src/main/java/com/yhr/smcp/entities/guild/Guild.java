package com.yhr.smcp.entities.guild;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tb_guild")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guild {
    @Id
    private Long id;
    private String name;
    private String faction;
    private String realm;
    private Instant createdTimestamp;
    private Integer achievementPoints;
    private Integer memberCount;
}
