package me.trae.core.database;

import me.trae.core.Main;

import java.util.HashMap;

public final class ConfigManager {

    private final HashMap<ConfigType, Config> configHashMap = new HashMap<>();

    public ConfigManager(final Main instance) {
        for (final ConfigType configType : ConfigType.values()) {
            final Config config = new Config(instance.getDataFolder(), configType.name);
            if (!config.fileExists()) {
                if (configType.equals(ConfigType.MAIN_CONFIG)) {
                    config.getConfig().set("Booleans.Settings.Disable-Blocks.TNT", false);
                    config.getConfig().set("Booleans.Settings.Disable-Blocks.Mob-Spawners", false);
                    config.getConfig().set("Strings.Server.Name", "Trae's Server");
                    config.getConfig().set("Strings.Server.Website", "https://example.com");
                    config.getConfig().set("Strings.Server.MOTD", "&6&lTrae's Server &8> &aAustralian Minecraft Server &8[1.8.9]\n&fVisit our Website at &ehttps://clansau.net");
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