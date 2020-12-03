package me.activated.spawnertax.listener;

import lombok.RequiredArgsConstructor;
import me.activated.spawnertax.SpawnerTax;
import me.activated.spawnertax.info.SpawnerInfo;
import me.activated.spawnertax.utilities.Utilities;
import me.activated.spawnertax.utilities.cooldown.Cooldown;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final SpawnerTax plugin;

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void handleBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("spawnertax.admin") && !plugin.DEV_MODE) return;
        if (!player.hasPermission(plugin.getConfigurationFile().getString("permission-to-check"))) return;

        Block block = event.getBlock();

        if (block.getType() != Material.MOB_SPAWNER) return;

        CreatureSpawner spawner = (CreatureSpawner) block.getState();

        if (plugin.getPlayerManager().getCooldownByLocation(player.getUniqueId(), spawner.getLocation()) != null)
            return;

        SpawnerInfo spawnerInfo = plugin.getSpawnerManager().getSpawnerInfo(spawner.getSpawnedType());

        if (spawnerInfo == null) return;

        int percentage = Utilities.getPercentage(player);
        int cost = spawnerInfo.getCost() - percentage;

        if (plugin.getEconomy().getBalance(player) < cost) {
            event.setCancelled(true);
            player.sendMessage(plugin.getConfigurationFile().getString("messages.prevented")
                    .replace("{amount}", String.valueOf(cost)));
            return;
        }

        Utilities.logConsole("&a" + player.getName() + "'s percentage is " + percentage); //DEBUG

        plugin.getEconomy().withdrawPlayer(player, cost);
        player.sendMessage(plugin.getConfigurationFile().getString("messages.mined")
                .replace("{amount}", String.valueOf(cost)));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handlePlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("spawnertax.admin") && !plugin.DEV_MODE) return;
        if (!player.hasPermission(plugin.getConfigurationFile().getString("permission-to-check"))) return;

        Block block = event.getBlock();

        if (block.getType() != Material.MOB_SPAWNER) return;

        CreatureSpawner spawner = (CreatureSpawner) block.getState();

        Cooldown cooldown = plugin.getPlayerManager().getCooldownByLocation(player.getUniqueId(), spawner.getLocation());
        if (cooldown != null) {
            plugin.getPlayerManager().getCooldowns().get(player.getUniqueId()).remove(cooldown);
        }

        plugin.getPlayerManager().createCooldown(player.getUniqueId(), spawner.getLocation());

        if (plugin.getPlayerManager().getCooldowns().get(player.getUniqueId()) != null) {
            plugin.getPlayerManager().getCooldowns().get(player.getUniqueId()).removeIf(c -> c == null || c.hasExpired());
        }
    }
}
