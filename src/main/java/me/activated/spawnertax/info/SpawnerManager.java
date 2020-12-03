package me.activated.spawnertax.info;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.activated.spawnertax.SpawnerTax;
import me.activated.spawnertax.utilities.Utilities;
import me.activated.spawnertax.utilities.file.ConfigurationFile;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class SpawnerManager {

    private final SpawnerTax plugin;

    private List<SpawnerInfo> spawnerInfos = new ArrayList<>();

    public void loadSpawners(ConfigurationFile file) {
        if (file.getConfigurationSection("spawners") == null) return;
        this.spawnerInfos.clear();

        file.getConfigurationSection("spawners").getKeys(false).forEach(key -> {
            String type = file.getString("spawners." + key + ".type");
            int cost = file.getInt("spawners." + key + ".cost");

            if (Utilities.isEntity(type)) {
                this.spawnerInfos.add(new SpawnerInfo(EntityType.valueOf(type), cost));
            }
        });
    }

    public SpawnerInfo getSpawnerInfo(EntityType type) {
        return this.spawnerInfos.stream().filter(spawnerInfo -> spawnerInfo.getType() == type).findFirst().orElse(null);
    }
}
