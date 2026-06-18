package com.yhr.smcp.entities.gamedata.mythicplus;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Table(name = "tb_mythic_seasons")
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeystoneSeason {
    @Id
    private Integer id;
    private Instant startTimestamp;
    private Instant endTimestamp;
    private String name;

}
