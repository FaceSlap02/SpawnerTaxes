package me.activated.spawnertax;

import lombok.Getter;
import lombok.Setter;
import me.activated.spawnertax.commands.SpawnerTaxCommand;
import me.activated.spawnertax.info.SpawnerManager;
import me.activated.spawnertax.listener.PlayerListener;
import me.activated.spawnertax.player.PlayerManager;
import me.activated.spawnertax.utilities.Utilities;
import me.activated.spawnertax.utilities.file.ConfigurationFile;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SpawnerTax extends JavaPlugin {

    @Getter
    public static SpawnerTax INSTANCE;

    public boolean DEV_MODE = false;

    @Setter
    private ConfigurationFile configurationFile;
    private Economy economy;

    private SpawnerManager spawnerManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.configurationFile = new ConfigurationFile(this, "config.yml");

        this.spawnerManager = new SpawnerManager(this);
        this.spawnerManager.loadSpawners(this.configurationFile);
        this.playerManager = new PlayerManager(this);

        this.registerListeners();
        this.registerEconomy();

        if (this.economy == null) {
            this.getServer().getPluginManager().disablePlugin(this);
            Utilities.logConsole("&cSpawner Tax disabled due to Vault economy integration not found!");
            return;
        }

        new SpawnerTaxCommand();

        Utilities.logConsole("&aSpawner Tax has been successfully enabled!");
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    private void registerEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null || !this.getServer().getPluginManager().getPlugin("Vault").isEnabled())
            return;

        RegisteredServiceProvider<Economy> provider = this.getServer().getServicesManager().getRegistration(Economy.class);

        if (provider == null) return;

        this.economy = provider.getProvider();
    }
}
