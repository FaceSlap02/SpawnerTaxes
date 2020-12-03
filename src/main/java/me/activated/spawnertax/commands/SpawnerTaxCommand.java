package me.activated.spawnertax.commands;

import me.activated.spawnertax.SpawnerTax;
import me.activated.spawnertax.utilities.Utilities;
import me.activated.spawnertax.utilities.chat.Color;
import me.activated.spawnertax.utilities.file.ConfigurationFile;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class SpawnerTaxCommand implements CommandExecutor {

    public SpawnerTaxCommand() {
        SpawnerTax.INSTANCE.getCommand("spawnertax").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Utilities.logConsole("&cThis command is for in game usage only!");
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("spawnertax.admin")) {
            this.sendInfo(player);
            return false;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            SpawnerTax.INSTANCE.setConfigurationFile(new ConfigurationFile(SpawnerTax.INSTANCE, "config.yml"));
            SpawnerTax.INSTANCE.getSpawnerManager().loadSpawners(SpawnerTax.INSTANCE.getConfigurationFile());

            player.sendMessage(Color.translate("&aSpawner Tax config has been reloaded."));
            return false;
        }
        if (args.length < 2) {
            this.sendInfo(player);
            return false;
        }
        if (args[0].equalsIgnoreCase("settype")) {
            if (!Utilities.isEntity(args[1].toUpperCase())) {
                player.sendMessage(Color.translate("&cInvalid entity type!"));
                return false;
            }
            Block block = this.getPlayerTargetBlock(player);

            if (block == null || block.getType() == Material.AIR || block.getType() != Material.MOB_SPAWNER) {
                player.sendMessage(Color.translate("&cYou must be looking at spawner!"));
                return false;
            }

            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            spawner.setSpawnedType(EntityType.valueOf(args[1].toUpperCase()));
            spawner.update();

            player.sendMessage(Color.translate("&cSpawner has been updated."));
            return false;
        }
        this.sendInfo(player);
        return false;
    }

    private void sendInfo(Player player) {
        player.sendMessage(Color.translate("&c&m-----&7&m-------------------------------&c&m-----"));
        player.sendMessage(Color.translate("&bSpawnerTax plugin made by &3FaceSlap_/Activated_"));
        player.sendMessage(Color.translate("&bVersion: &3v" + SpawnerTax.INSTANCE.getDescription().getVersion()));
        player.sendMessage(Color.translate("&c&m-----&7&m-------------------------------&c&m-----"));
    }

    private Block getPlayerTargetBlock(Player player) {
        BlockIterator iterator = new BlockIterator(player, 5);
        Block block = iterator.next();

        while (iterator.hasNext()) {
            block = iterator.next();
        }

        return block;
    }
}
