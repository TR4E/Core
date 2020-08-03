package me.trae.core.client;

import me.trae.core.Main;
import me.trae.core.database.Config;
import me.trae.core.database.ConfigManager;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public final class ClientRepository {

    private final Config config;

    public ClientRepository(final Main instance) {
        this.config = instance.getConfigManager().getConfig(ConfigManager.ConfigType.CLIENTS_DATA);
        loadClients(instance, true);
    }

    public void saveClient(final Client client) {
        config.loadFile();
        config.getConfig().set(client.getUUID().toString() + ".Name.Current", client.getName());
        config.getConfig().set(client.getUUID().toString() + ".Name.Previous", client.getOldName());
        config.getConfig().set(client.getUUID().toString() + ".IP", new ArrayList<>(client.getIPAddresses()));
        config.getConfig().set(client.getUUID().toString() + ".Rank", client.getRank().name());
        config.getConfig().set(client.getUUID().toString() + ".First-Joined", client.getFirstJoined());
        config.getConfig().set(client.getUUID().toString() + ".Last-Online", client.getLastOnline());
        config.getConfig().set(client.getUUID().toString() + ".Joined-Amount", client.getJoinedAmount());
        config.saveFile();
    }

    public void updateName(final Client client) {
        config.loadFile();
        config.getConfig().set(client.getUUID().toString() + ".Name.Current", client.getName());
        config.saveFile();
    }

    public void updateOldName(final Client client) {
        config.loadFile();
        config.getConfig().set(client.getUUID().toString() + ".Name.Previous", client.getOldName());
        config.saveFile();
    }

    public void updateIP(final Client client) {
        config.loadFile();
        config.getConfig().set(client.getUUID().toString() + ".IP", new ArrayList<>(client.getIPAddresses()));
        config.saveFile();
    }

    public void updateRank(final Client client) {
        config.loadFile();
        config.getConfig().set(client.getUUID().toString() + ".Rank", client.getRank().name());
        config.saveFile();
    }

    public void updateFirstJoined(final Client client) {
        config.loadFile();
        config.getConfig().set(client.getUUID().toString() + ".First-Joined", client.getFirstJoined());
        config.saveFile();
    }

    public void updateLastOnline(final Client client) {
        config.loadFile();
        config.getConfig().set(client.getUUID().toString() + ".Last-Online", client.getLastOnline());
        config.saveFile();
    }

    public void updateJoinedAmount(final Client client) {
        config.loadFile();
        config.getConfig().set(client.getUUID().toString() + ".Joined-Amount", client.getJoinedAmount());
        config.saveFile();
    }

    public void loadClients(final Main instance, final boolean inform) {
        config.loadFile();
        final YamlConfiguration yml = config.getConfig();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (final String str : yml.getKeys(false)) {
                    final Client client = new Client(UUID.fromString(str));
                    client.setName(yml.getString(str + ".Name.Current"));
                    client.setOldName(yml.getString(str + ".Name.Previous"));
                    client.getIPAddresses().addAll(new HashSet<>(yml.getStringList(str + ".IP")));
                    client.setRank(Rank.valueOf(yml.getString(str + ".Rank")));
                    client.setFirstJoined(yml.getLong(str + ".First-Joined"));
                    client.setLastOnline(yml.getLong(str + ".Last-Online"));
                    client.setJoinedAmount(yml.getInt(str + ".Joined-Amount"));
                    instance.getClientUtilities().addClient(client);
                }
                if (inform) {
                    UtilMessage.log("Database", "Loaded: " + ChatColor.YELLOW + instance.getClientUtilities().getClients().size() + ChatColor.GRAY + " Clients.");
                }
            }
        }.runTaskAsynchronously(instance);
    }
}