package me.trae.core.database;

import me.trae.core.Main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class ConfigManager {

    private final HashMap<ConfigType, Config> configHashMap = new HashMap<>();

    private final Main instance;

    public ConfigManager(final Main instance) {
        this.instance = instance;
        Arrays.stream(ConfigType.values()).forEach(this::setup);
    }

    public void setup(final ConfigType configType) {
        final Config config = new Config(instance.getDataFolder(), configType.name);
        if (!(config.fileExists())) {
            if (configType.equals(ConfigType.MAIN_CONFIG)) {
                config.getConfig().set("Booleans.Settings.Server.Console-Debug-Outputs", true);
                config.getConfig().set("Booleans.Settings.Game.Enchantments", true);
                config.getConfig().set("Booleans.Settings.Game.Portals", true);
                config.getConfig().set("Booleans.Settings.Game.Mobs", true);
                config.getConfig().set("Booleans.Settings.Game.PvP", true);
                config.getConfig().set("Booleans.Settings.Game.Always-Day", false);
                config.getConfig().set("Booleans.Settings.Game.Always-Night", false);
                config.getConfig().set("Booleans.Settings.Game.Weather", true);
                config.getConfig().set("Booleans.Settings.Game.Saturation", true);
                config.getConfig().set("Booleans.Settings.Game.Break-Crops", true);
                config.getConfig().set("Booleans.Settings.Game.Owners-Vanish-On-Join", true);
                config.getConfig().set("Booleans.Settings.Fun-Features.Throwing-TNT", true);
                config.getConfig().set("Booleans.Settings.Fun-Features.Throwing-Web", true);
                config.getConfig().set("Booleans.Settings.Fun-Features.Throwing-Pearl", true);
                config.getConfig().set("Booleans.Settings.Admin-Commands.Spawn", true);
                config.getConfig().set("Booleans.Settings.Admin-Commands.Suicide", true);
                config.getConfig().set("Booleans.Settings.Admin-Commands.Clear-Inv", true);
                config.getConfig().set("Booleans.Settings.Command-Confirmation.Clear-Inv", true);
                config.getConfig().set("Booleans.Settings.Disable-Blocks.TNT", false);
                config.getConfig().set("Booleans.Settings.Disable-Blocks.Hopper", false);
                config.getConfig().set("Booleans.Settings.Disable-Blocks.Anvil", false);
                config.getConfig().set("Booleans.Settings.Disable-Blocks.Dropper", false);
                config.getConfig().set("Booleans.Settings.Disable-Blocks.Dispenser", false);
                config.getConfig().set("Booleans.Settings.Disable-Blocks.Brewing-Stand", false);
                config.getConfig().set("Booleans.Settings.Disable-Blocks.Ender-Chest", false);
                config.getConfig().set("Booleans.Settings.Disable-Blocks.Trapped-Chest", false);
                config.getConfig().set("Booleans.Settings.Disable-Blocks.Mob-Spawner", false);
                config.getConfig().set("Integers.Server.Max-Player-Slots", 100);
                config.getConfig().set("Integers.Game.PvP-Protection", 60);
                config.getConfig().set("Integers.Cooldowns.Spawn-Command", 300);
                config.getConfig().set("Integers.Cooldowns.Back-Command", 300);
                config.getConfig().set("Integers.Cooldowns.Announce-Command", 300);
                config.getConfig().set("Integers.Cooldowns.Support-Command", 120);
                config.getConfig().set("Integers.Cooldowns.Suicide-Command", 120);
                config.getConfig().set("Integers.Cooldowns.ClearInv-Command", 120);
                config.getConfig().set("Integers.Countdowns.Spawn-Command", 10);
                config.getConfig().set("Strings.Server.Name", "Trae's Server");
                config.getConfig().set("Strings.Server.World", "world");
                config.getConfig().set("Strings.Server.Website", "https://example.com");
                config.getConfig().set("Strings.Server.MOTD", "&6&lTrae's Server &8» &aAustralian Minecraft Server &7[1.8]\n&fVisit our Website at &ehttps://example.com");
            } else if (configType.equals(ConfigType.RULES_DATA)) {
                final String[] rules = new String[]{"&7Do not advertise!", "&7Do not use cheats/hacks!", "&7Do not exploit bugs/glitches!"};
                config.getConfig().set("Rules", Arrays.asList(rules));
            }
            config.getConfig().options().copyDefaults(true);
            config.createFile();
        }
        config.loadFile();
        config.saveFile();
        configHashMap.put(configType, config);
    }

    public final Config getConfig(final ConfigType configType) {
        return configHashMap.get(configType);
    }

    public final Set<ConfigType> getConfigTypes() {
        return new HashSet<>(configHashMap.keySet());
    }

    public enum ConfigType {
        MAIN_CONFIG("config"),
        RULES_DATA("rules"),
        CLIENTS_DATA("clients"),
        GAMERS_DATA("gamers");
        String name;

        ConfigType(final String name) {
            this.name = name;
        }
    }
}