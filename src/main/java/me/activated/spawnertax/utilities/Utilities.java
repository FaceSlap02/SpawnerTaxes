package me.activated.spawnertax.utilities;

import me.activated.spawnertax.utilities.chat.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

public class Utilities {

    public static boolean isEntity(String type) {
        try {
            EntityType.valueOf(type);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static int getPercentage(Player player) {
        return IntStream.rangeClosed(0, 100).filter(i -> player.hasPermission("spawnertax." + i)).findFirst().orElse(0);
    }

    public static void logConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(Color.translate(message));
    }
}
