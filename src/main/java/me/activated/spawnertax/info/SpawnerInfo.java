package me.activated.spawnertax.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.EntityType;

@Getter
@Setter
@AllArgsConstructor
public class SpawnerInfo {

    private EntityType type;
    private int cost;
}
