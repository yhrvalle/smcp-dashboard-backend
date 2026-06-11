package com.yhr.smcp.entities.gamedata.mythicplus;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@Table(name = "tb_affixes")
@NoArgsConstructor
@AllArgsConstructor
public class KeystoneAffix {
    @Id
    private Integer id;
    private String name;
    private String description;
}
