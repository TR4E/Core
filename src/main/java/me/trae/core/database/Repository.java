package me.trae.core.database;

import me.trae.core.Main;
import org.bukkit.scheduler.BukkitRunnable;

public final class Repository {

    private final Config config;
    private boolean spawnCommandAdminOnly, clearInventoryCommandAdminOnly, disableTNT, disableMobSpawners;
    private int maxPlayerSlots;
    private String serverName, serverWorld, serverWebsite, serverMOTD;

    public Repository(final Main instance) {
        this.config = instance.getConfigManager().getConfig(ConfigManager.ConfigType.MAIN_CONFIG);
        load(instance);
    }

    public void reload(final Main instance, final boolean admin) {
        config.loadFile();
        config.saveFile();
        load(instance);
        if (admin) {
            instance.getConfigManager().getConfig(ConfigManager.ConfigType.GAMERS_DATA).loadFile();
            instance.getConfigManager().getConfig(ConfigManager.ConfigType.GAMERS_DATA).saveFile();
            instance.getConfigManager().getConfig(ConfigManager.ConfigType.CLIENTS_DATA).loadFile();
            instance.getConfigManager().getConfig(ConfigManager.ConfigType.CLIENTS_DATA).saveFile();
            instance.getClientRepository().loadClients(instance);
        }
    }

    public void load(final Main instance) {
        new BukkitRunnable() {
            @Override
            public void run() {
                spawnCommandAdminOnly = config.getConfig().getBoolean("Booleans.Settings.Admin-Commands.Spawn");
                clearInventoryCommandAdminOnly = config.getConfig().getBoolean("Booleans.Settings.Admin-Commands.Clear-Inventory");
                disableTNT = config.getConfig().getBoolean("Booleans.Settings.Disabled-Blocks.TNT");
                disableMobSpawners = config.getConfig().getBoolean("Booleans.Settings.Disable-Blocks.Mob-Spawners");
                maxPlayerSlots = config.getConfig().getInt("Integers.Server.Max-Player-Slots");
                serverName = config.getConfig().getString("Strings.Server.Name");
                serverWorld = config.getConfig().getString("Strings.Server.World");
                serverWebsite = config.getConfig().getString("Strings.Server.Website");
                serverMOTD = config.getConfig().getString("Strings.Server.MOTD");
            }
        }.runTaskAsynchronously(instance);
    }

    public final boolean isSpawnCommandAdminOnly() {
        return spawnCommandAdminOnly;
    }

    public final boolean isClearInventoryCommandAdminOnly() {
        return clearInventoryCommandAdminOnly;
    }

    public final boolean isDisableTNT() {
        return disableTNT;
    }

    public final boolean isDisableMobSpawners() {
        return disableMobSpawners;
    }

    public final int getMaxPlayerSlots() {
        return maxPlayerSlots;
    }

    public final String getServerName() {
        return serverName;
    }

    public final String getServerWorld() {
        return serverWorld;
    }

    public final String getServerWebsite() {
        return serverWebsite;
    }

    public final String getServerMOTD() {
        return serverMOTD;
    }
}