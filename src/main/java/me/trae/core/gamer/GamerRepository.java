package me.trae.core.gamer;

import me.trae.core.Main;
import me.trae.core.database.Config;
import me.trae.core.database.ConfigManager;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public final class GamerRepository {

    private final Main instance;
    private final Config config;

    public GamerRepository(final Main instance) {
        this.instance = instance;
        this.config = instance.getConfigManager().getConfig(ConfigManager.ConfigType.GAMERS_DATA);
    }

    public void saveGamer(final Gamer g) {
        config.loadFile();
        config.getConfig().set(g.getUUID().toString() + ".Ignored", new ArrayList<>(g.getIgnored()));
        config.getConfig().set(g.getUUID().toString() + ".Kills", g.getKills());
        config.getConfig().set(g.getUUID().toString() + ".Deaths", g.getDeaths());
        config.saveFile();
    }

    public void updateIgnored(final Gamer g) {
        config.loadFile();
        config.getConfig().set(g.getUUID().toString() + ".Ignored", new ArrayList<>(g.getIgnored()));
        config.saveFile();
    }

    public void updateKills(final Gamer g) {
        config.loadFile();
        config.getConfig().set(g.getUUID().toString() + ".Kills", g.getKills());
        config.saveFile();
    }

    public void updateDeaths(final Gamer g) {
        config.loadFile();
        config.getConfig().set(g.getUUID().toString() + ".Deaths", g.getDeaths());
        config.saveFile();
    }

    public void loadGamer(final UUID uuid, final Main instance) {
        config.loadFile();
        final YamlConfiguration yml = this.config.getConfig();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (final String str : yml.getKeys(false)) {
                    final Gamer gamer = new Gamer(UUID.fromString(str));
                    yml.getStringList(str + ".Ignored").forEach(i -> gamer.getIgnored().add(UUID.fromString(i)));
                    gamer.setKills(yml.getInt(str + ".Kills"));
                    gamer.setDeaths(yml.getInt(str + ".Deaths"));
                    instance.getGamerUtilities().addGamer(gamer);
                }
                UtilMessage.log("Gamers", "Loaded Gamer: " + ChatColor.YELLOW + instance.getClientUtilities().getOnlineClient(uuid).getName());
            }
        }.runTaskAsynchronously(instance);
    }
}