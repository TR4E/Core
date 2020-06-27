package me.trae.core;

import me.trae.core.client.ClientRepository;
import me.trae.core.client.ClientUtilities;
import me.trae.core.command.CommandManager;
import me.trae.core.database.ConfigManager;
import me.trae.core.database.Repository;
import me.trae.core.database.commands.ReloadCommand;
import me.trae.core.module.CoreListener;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

    private boolean started;

    private ConfigManager configManager;
    private Repository repository;
    private ClientRepository clientRepository;
    private ClientUtilities clientUtilities;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        started = false;
        this.configManager = new ConfigManager(this);
        this.repository = new Repository(this);
        this.clientRepository = new ClientRepository(this);
        this.clientUtilities = new ClientUtilities(this);
        this.commandManager = new CommandManager(this);
        initialize();
        registerEvents();
        registerCommands();
        new BukkitRunnable() {
            @Override
            public void run() {
                started = true;
                UtilMessage.log("Core", ChatColor.GREEN + "Plugin Enabled!");
            }
        }.runTaskLater(this, 20L);
    }

    @Override
    public void onDisable() {
        UtilMessage.log("Core", ChatColor.RED + "Plugin Disabled!");
    }

    private void initialize() {
        new ConfigManager(this);
        new Repository(this);
        new ClientRepository(this);
    }

    private void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new CoreListener(this), this);
    }

    private void registerCommands() {
        getCommandManager().addCommand(new ReloadCommand(this));
    }

    public final boolean hasStarted() {
        return started;
    }

    public final ConfigManager getConfigManager() {
        return configManager;
    }

    public final Repository getRepository() {
        return repository;
    }

    public final ClientRepository getClientRepository() {
        return clientRepository;
    }

    public final ClientUtilities getClientUtilities() {
        return clientUtilities;
    }

    public final CommandManager getCommandManager() {
        return commandManager;
    }
}