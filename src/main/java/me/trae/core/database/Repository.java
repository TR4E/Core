package me.trae.core.database;

import me.trae.core.Main;
import org.bukkit.scheduler.BukkitRunnable;

public final class Repository {

    private final Config config;
    private boolean disableTNT, disableMobSpawners;
    private String serverName, serverWebsite, serverMOTD;

    public Repository(final Main instance) {
        this.config = instance.getConfigManager().getConfig(ConfigManager.ConfigType.MAIN_CONFIG);
        load(instance);
    }

    public void reload(final Main instance, final boolean admin) {
        config.loadFile();
        config.saveFile();
        load(instance);
        if (admin) {
            instance.getConfigManager().getConfig(ConfigManager.ConfigType.CLIENTS_DATA).loadFile();
            instance.getConfigManager().getConfig(ConfigManager.ConfigType.CLIENTS_DATA).saveFile();
            instance.getClientRepository().loadClients(instance);
        }
    }

    public void load(final Main instance) {
        new BukkitRunnable() {
            @Override
            public void run() {
                disableTNT = config.getConfig().getBoolean("Booleans.Settings.Disabled-Blocks.TNT");
                disableMobSpawners = config.getConfig().getBoolean("Booleans.Settings.Disable-Blocks.Mob-Spawners");
                serverName = config.getConfig().getString("Strings.Server.Name");
                serverWebsite = config.getConfig().getString("Strings.Server.Website");
                serverMOTD = config.getConfig().getString("Strings.Server.MOTD");
            }
        }.runTaskAsynchronously(instance);
    }

    public final boolean isDisableTNT() {
        return disableTNT;
    }

    public final boolean isDisableMobSpawners() {
        return disableMobSpawners;
    }

    public final String getServerName() {
        return serverName;
    }

    public final String getServerWebsite() {
        return serverWebsite;
    }

    public final String getServerMOTD() {
        return serverMOTD;
    }
}