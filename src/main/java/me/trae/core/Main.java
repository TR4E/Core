package me.trae.core;

import me.trae.core.client.ClientRepository;
import me.trae.core.client.ClientUtilities;
import me.trae.core.client.commands.ClientCommand;
import me.trae.core.client.commands.ObserverCommand;
import me.trae.core.client.commands.StaffChatCommand;
import me.trae.core.client.listeners.ConnectionListener;
import me.trae.core.command.CommandCenter;
import me.trae.core.command.CommandManager;
import me.trae.core.database.ConfigManager;
import me.trae.core.database.Repository;
import me.trae.core.database.commands.ReloadCommand;
import me.trae.core.gamer.GamerRepository;
import me.trae.core.gamer.GamerUtilities;
import me.trae.core.module.CoreListener;
import me.trae.core.utility.UtilMessage;
import me.trae.core.world.ChatListener;
import me.trae.core.world.ServerListener;
import me.trae.core.world.WorldListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

    private boolean started;

    private ConfigManager configManager;
    private Repository repository;
    private ClientRepository clientRepository;
    private GamerRepository gamerRepository;
    private ClientUtilities clientUtilities;
    private GamerUtilities gamerUtilities;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        started = false;
        this.configManager = new ConfigManager(this);
        this.repository = new Repository(this);
        this.clientRepository = new ClientRepository(this);
        this.gamerRepository = new GamerRepository(this);
        this.clientUtilities = new ClientUtilities(this);
        this.gamerUtilities = new GamerUtilities(this);
        this.commandManager = new CommandManager(this);
        new CoreListener(this);
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

    private void registerEvents() {
        new ConnectionListener(this);
        new CommandCenter(this);
        new ChatListener(this);
        new ServerListener(this);
        new WorldListener(this);
    }

    private void registerCommands() {
        getCommandManager().addCommand(new ClientCommand(this));
        getCommandManager().addCommand(new ObserverCommand(this));
        getCommandManager().addCommand(new StaffChatCommand(this));
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

    public final GamerRepository getGamerRepository() {
        return gamerRepository;
    }

    public final ClientUtilities getClientUtilities() {
        return clientUtilities;
    }

    public final GamerUtilities getGamerUtilities() {
        return gamerUtilities;
    }

    public final CommandManager getCommandManager() {
        return commandManager;
    }
}