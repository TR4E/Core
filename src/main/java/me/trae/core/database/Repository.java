package me.trae.core.database;

import me.trae.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public final class Repository {

    private static boolean gameEnchantments;
    private final Config config;
    private boolean funThrowingTNT, funThrowingWeb, funThrowingPearl, gamePortals, gameMobs, gamePvP, gameAlwaysDay, gameAlwaysNight, gameWeather, gameSaturation, gameBreakCrops, gameOwnersVanishOnJoin, spawnCommandAdminOnly, suicideCommandAdminOnly, clearInvCommandAdminOnly, disableTNT, disableHopper, disableAnvil, disableDroppers, disableDispensers, disableBrewingStands, disableEnderChests, disableTrappedChests, disableMobSpawners;
    private int gamePvPProtection, maxPlayerSlots, spawnCommandCooldown, backCommandCooldown, announceCommandCooldown, supportCommandCooldown, suicideCommandCooldown, clearInvCommandCooldown, spawnCommandCountdown;
    private String serverName, serverWorld, serverWebsite, serverMOTD;
    private List<String> rules;

    public Repository(final Main instance) {
        this.config = instance.getConfigManager().getConfig(ConfigManager.ConfigType.MAIN_CONFIG);
        load(instance);
    }

    public static boolean isGameEnchantments() {
        return gameEnchantments;
    }

    public void reload(final Main instance, final boolean admin) {
        config.loadFile();
        config.saveFile();
        load(instance);
        if (admin) {
            instance.getConfigManager().getConfig(ConfigManager.ConfigType.GAMERS_DATA).loadFile();
            instance.getConfigManager().getConfig(ConfigManager.ConfigType.GAMERS_DATA).saveFile();
            instance.getGamerUtilities().getGamers().clear();
            Bukkit.getOnlinePlayers().forEach(o -> instance.getGamerRepository().loadGamer(o.getUniqueId(), instance));
            instance.getConfigManager().getConfig(ConfigManager.ConfigType.CLIENTS_DATA).loadFile();
            instance.getConfigManager().getConfig(ConfigManager.ConfigType.CLIENTS_DATA).saveFile();
            instance.getClientRepository().loadClients(instance);
        }
    }

    public void load(final Main instance) {
        new BukkitRunnable() {
            @Override
            public void run() {
                gameEnchantments = config.getConfig().getBoolean("Booleans.Settings.Game.Enchantments");
                funThrowingTNT = config.getConfig().getBoolean("Booleans.Settings.Fun-Features.Throwing-TNT");
                funThrowingWeb = config.getConfig().getBoolean("Booleans.Settings.Fun-Features.Throwing-Web");
                funThrowingPearl = config.getConfig().getBoolean("Booleans.Settings.Fun-Features.Throwing-Pearl");
                gamePortals = config.getConfig().getBoolean("Booleans.Settings.Game.Portals");
                gameMobs = config.getConfig().getBoolean("Booleans.Settings.Game.Mobs");
                gamePvP = config.getConfig().getBoolean("Booleans.Settings.Game.PvP");
                gameAlwaysDay = config.getConfig().getBoolean("Booleans.Settings.Game.Always-Day");
                gameAlwaysNight = config.getConfig().getBoolean("Booleans.Settings.Always-Night");
                gameWeather = config.getConfig().getBoolean("Booleans.Settings.Game.Weather");
                gameSaturation = config.getConfig().getBoolean("Booleans.Settings.Game.Saturation");
                gameBreakCrops = config.getConfig().getBoolean("Booleans.Settings.Game.Break-Crops");
                gameOwnersVanishOnJoin = config.getConfig().getBoolean("Booleans.Settings.Game.Owners-Vanish-On-Join");
                spawnCommandAdminOnly = config.getConfig().getBoolean("Booleans.Settings.Admin-Commands.Spawn");
                suicideCommandAdminOnly = config.getConfig().getBoolean("Booleans.Settings.Admin-Commands.Suicide");
                clearInvCommandAdminOnly = config.getConfig().getBoolean("Booleans.Settings.Admin-Commands.Clear-Inventory");
                disableTNT = config.getConfig().getBoolean("Booleans.Settings.Disabled-Blocks.TNT");
                disableHopper = config.getConfig().getBoolean("Booleans.Settings.Disabled-Blocks.Hopper");
                disableAnvil = config.getConfig().getBoolean("Booleans.Settings.Disabled-Blocks.Anvil");
                disableDroppers = config.getConfig().getBoolean("Booleans.Settings.Disabled-Blocks.Dropper");
                disableDispensers = config.getConfig().getBoolean("Booleans.Settings.Disabled-Blocks.Dispenser");
                disableBrewingStands = config.getConfig().getBoolean("Booleans.Settings.Disabled-Blocks.Brewing-Stand");
                disableEnderChests = config.getConfig().getBoolean("Booleans.Settings.Disabled-Blocks.Ender-Chest");
                disableTrappedChests = config.getConfig().getBoolean("Booleans.Settings.Disabled-Blocks.Trapped-Chest");
                disableMobSpawners = config.getConfig().getBoolean("Booleans.Settings.Disable-Blocks.Mob-Spawner");
                gamePvPProtection = config.getConfig().getInt("Integers.Game.PvP-Protection");
                maxPlayerSlots = config.getConfig().getInt("Integers.Server.Max-Player-Slots");
                spawnCommandCooldown = config.getConfig().getInt("Integers.Cooldowns.Spawn-Command");
                backCommandCooldown = config.getConfig().getInt("Integers.Cooldowns.Back-Command");
                announceCommandCooldown = config.getConfig().getInt("Integers.Cooldowns.Announce-Command");
                supportCommandCooldown = config.getConfig().getInt("Integers.Cooldowns.Support-Command");
                suicideCommandCooldown = config.getConfig().getInt("Integers.Cooldowns.Suicide-Command");
                clearInvCommandCooldown = config.getConfig().getInt("Integers.Cooldowns.ClearInv-Command");
                spawnCommandCountdown = config.getConfig().getInt("Integers.Countdowns.Spawn-Command");
                serverName = config.getConfig().getString("Strings.Server.Name");
                serverWorld = config.getConfig().getString("Strings.Server.World");
                serverWebsite = config.getConfig().getString("Strings.Server.Website");
                serverMOTD = config.getConfig().getString("Strings.Server.MOTD");
                rules = instance.getConfigManager().getConfig(ConfigManager.ConfigType.RULES_DATA).getConfig().getStringList("Rules");
            }
        }.runTaskAsynchronously(instance);
    }

    public final boolean isFunThrowingTNT() {
        return funThrowingTNT;
    }

    public final boolean isFunThrowingWeb() {
        return funThrowingWeb;
    }

    public final boolean isFunThrowingPearl() {
        return funThrowingPearl;
    }

    public final boolean isGamePortals() {
        return gamePortals;
    }

    public final boolean isGameMobs() {
        return gameMobs;
    }

    public final boolean isGamePvP() {
        return gamePvP;
    }

    public final boolean isGameAlwaysDay() {
        return gameAlwaysDay;
    }

    public final boolean isGameAlwaysNight() {
        return gameAlwaysNight;
    }

    public final boolean isGameWeather() {
        return gameWeather;
    }

    public final boolean isGameSaturation() {
        return gameSaturation;
    }

    public final boolean isGameBreakCrops() {
        return gameBreakCrops;
    }

    public final boolean isGameOwnersVanishOnJoin() {
        return gameOwnersVanishOnJoin;
    }

    public final boolean isSpawnCommandAdminOnly() {
        return spawnCommandAdminOnly;
    }

    public final boolean isSuicideCommandAdminOnly() {
        return suicideCommandAdminOnly;
    }

    public final boolean isClearInvCommandAdminOnly() {
        return clearInvCommandAdminOnly;
    }

    public final boolean isDisableTNT() {
        return disableTNT;
    }

    public final boolean isDisableHopper() {
        return disableHopper;
    }

    public final boolean isDisableAnvil() {
        return disableAnvil;
    }

    public final boolean isDisableDroppers() {
        return disableDroppers;
    }

    public final boolean isDisableDispensers() {
        return disableDispensers;
    }

    public final boolean isDisableBrewingStands() {
        return disableBrewingStands;
    }

    public final boolean isDisableEnderChests() {
        return disableEnderChests;
    }

    public final boolean isDisableTrappedChests() {
        return disableTrappedChests;
    }

    public final boolean isDisableMobSpawners() {
        return disableMobSpawners;
    }

    public final int getGamePvPProtection() {
        return gamePvPProtection;
    }

    public final int getMaxPlayerSlots() {
        return maxPlayerSlots;
    }

    public final int getSpawnCommandCooldown() {
        return spawnCommandCooldown;
    }

    public final int getBackCommandCooldown() {
        return backCommandCooldown;
    }

    public final int getAnnounceCommandCooldown() {
        return announceCommandCooldown;
    }

    public final int getSupportCommandCooldown() {
        return supportCommandCooldown;
    }

    public final int getSuicideCommandCooldown() {
        return suicideCommandCooldown;
    }

    public final int getClearInvCommandCooldown() {
        return clearInvCommandCooldown;
    }

    public final int getSpawnCommandCountdown() {
        return spawnCommandCountdown;
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

    public final List<String> getRules() {
        return rules;
    }
}