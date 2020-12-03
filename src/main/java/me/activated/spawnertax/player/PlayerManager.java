package me.activated.spawnertax.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.activated.spawnertax.SpawnerTax;
import me.activated.spawnertax.utilities.cooldown.Cooldown;
import org.bukkit.Location;

import java.util.*;

@Getter
@Setter
@RequiredArgsConstructor
public class PlayerManager {

    private final SpawnerTax plugin;

    public Map<UUID, List<Cooldown>> cooldowns = new HashMap<>();

    public Cooldown getCooldownByLocation(UUID uuid, Location location) {
        return this.cooldowns.getOrDefault(uuid, new ArrayList<>()).stream().filter(cooldown ->
                cooldown != null && !cooldown.hasExpired() &&
                        cooldown.getLocation().equals(location)).findFirst().orElse(null);
    }

    public void createCooldown(UUID uuid, Location location) {
        this.cooldowns.putIfAbsent(uuid, new ArrayList<>());
        this.cooldowns.get(uuid).add(new Cooldown(this.getDelay(), location));
    }

    public int getDelay() {
        return this.plugin.getConfigurationFile().getInt("spawner-tax-delay-seconds");
    }
}
