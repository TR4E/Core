package me.trae.core.database;

import me.trae.core.Main;

import java.util.HashMap;

public final class ConfigManager {

    private final HashMap<ConfigType, Config> configHashMap = new HashMap<>();

    public ConfigManager(final Main instance) {
        for (final ConfigType configType : ConfigType.values()) {
            final Config config = new Config(instance.getDataFolder(), configType.name);
            if (!(config.fileExists())) {
                if (configType.equals(ConfigType.MAIN_CONFIG)) {
                    config.getConfig().set("Booleans.Settings.Fun-Features.Throwing-TNT", true);
                    config.getConfig().set("Booleans.Settings.Fun-Features.Throwing-Web", true);
                    config.getConfig().set("Booleans.Settings.Fun-Features.Throwing-Pearl", true);
                    config.getConfig().set("Booleans.Settings.Game.Enchantments", true);
                    config.getConfig().set("Booleans.Settings.Game.Portals", true);
                    config.getConfig().set("Booleans.Settings.Game.Mobs", true);
                    config.getConfig().set("Booleans.Settings.Game.Always-Day", false);
                    config.getConfig().set("Booleans.Settings.Game.Always-Night", false);
                    config.getConfig().set("Booleans.Settings.Game.Weather", true);
                    config.getConfig().set("Booleans.Settings.Game.Saturation", true);
                    config.getConfig().set("Booleans.Settings.Game.Break-Crops", true);
                    config.getConfig().set("Booleans.Settings.Game.Owners-Vanish-On-Join", true);
                    config.getConfig().set("Booleans.Settings.Admin-Commands.Spawn", true);
                    config.getConfig().set("Booleans.Settings.Admin-Commands.Suicide", true);
                    config.getConfig().set("Booleans.Settings.Admin-Commands.Clear-Inv", true);
                    config.getConfig().set("Booleans.Settings.Disable-Blocks.TNT", false);
                    config.getConfig().set("Booleans.Settings.Disable-Blocks.Mob-Spawners", false);
                    config.getConfig().set("Integers.Server.Max-Player-Slots", 100);
                    config.getConfig().set("Integers.Game.PvP-Protection", 60);
                    config.getConfig().set("Integers.Cooldowns.Spawn-Command", 300);
                    config.getConfig().set("Integers.Cooldowns.Announce-Command", 300);
                    config.getConfig().set("Integers.Cooldowns.Support-Command", 120);
                    config.getConfig().set("Integers.Cooldowns.Suicide-Command", 120);
                    config.getConfig().set("Integers.Cooldowns.ClearInv-Command", 120);
                    config.getConfig().set("Integers.Countdowns.Spawn-Command", 10);
                    config.getConfig().set("Strings.Server.Name", "Trae's Server");
                    config.getConfig().set("Strings.Server.World", "world");
                    config.getConfig().set("Strings.Server.Website", "https://example.com");
                    config.getConfig().set("Strings.Server.MOTD", "&6&lTrae's Server &8» &aAustralian Minecraft Server &7[1.8]\n&fVisit our Website at &ehttps://example.com");
                }
                config.getConfig().options().copyDefaults(true);
                config.createFile();
            }
            config.loadFile();
            config.saveFile();
            configHashMap.put(configType, config);
        }
    }

    public final Config getConfig(final ConfigType configType) {
        return configHashMap.get(configType);
    }

    public enum ConfigType {
        MAIN_CONFIG("config"),
        CLIENTS_DATA("clients"),
        GAMERS_DATA("gamers");
        String name;

        ConfigType(final String name) {
            this.name = name;
        }
    }
}