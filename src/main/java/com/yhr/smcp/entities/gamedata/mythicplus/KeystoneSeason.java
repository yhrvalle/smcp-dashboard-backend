package com.yhr.smcp.entities.gamedata.mythicplus;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "tb_mythic_seasons")
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeystoneSeason {
    @Id
    private Integer id;
    private Double startTimestamp;
    private Double endTimestamp;
    private String name;

}
