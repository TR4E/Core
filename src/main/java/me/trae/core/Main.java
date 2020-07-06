package me.trae.core;

import me.trae.core.client.ClientRepository;
import me.trae.core.client.ClientUtilities;
import me.trae.core.client.commands.*;
import me.trae.core.client.commands.help.*;
import me.trae.core.client.commands.staff.*;
import me.trae.core.client.commands.teleport.SetSpawnCommand;
import me.trae.core.client.commands.teleport.SpawnCommand;
import me.trae.core.client.listeners.ConnectionListener;
import me.trae.core.command.CommandCenter;
import me.trae.core.command.CommandManager;
import me.trae.core.database.ConfigManager;
import me.trae.core.database.Repository;
import me.trae.core.database.commands.ReloadCommand;
import me.trae.core.effect.EffectManager;
import me.trae.core.effect.commands.ProtectionCommand;
import me.trae.core.fishing.FishingListener;
import me.trae.core.gamer.GamerManager;
import me.trae.core.gamer.GamerRepository;
import me.trae.core.gamer.GamerUtilities;
import me.trae.core.module.TitleManager;
import me.trae.core.module.recharge.RechargeManager;
import me.trae.core.module.update.Updater;
import me.trae.core.utility.UtilMessage;
import me.trae.core.world.ChatListener;
import me.trae.core.world.ItemListener;
import me.trae.core.world.ServerListener;
import me.trae.core.world.WorldListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class Main extends JavaPlugin {

    private boolean started, chat;

    private ConfigManager configManager;
    private Repository repository;
    private ClientRepository clientRepository;
    private GamerRepository gamerRepository;
    private ClientUtilities clientUtilities;
    private GamerUtilities gamerUtilities;
    private CommandManager commandManager;
    private RechargeManager rechargeManager;
    private EffectManager effectManager;
    private TitleManager titleManager;

    @Override
    public void onEnable() {
        this.started = false;
        this.chat = true;
        this.configManager = new ConfigManager(this);
        this.repository = new Repository(this);
        this.clientRepository = new ClientRepository(this);
        this.gamerRepository = new GamerRepository(this);
        this.clientUtilities = new ClientUtilities(this);
        this.gamerUtilities = new GamerUtilities(this);
        this.commandManager = new CommandManager(this);
        this.rechargeManager = new RechargeManager(this);
        this.effectManager = new EffectManager(this);
        this.titleManager = new TitleManager(this);
        new Updater(this);
        setupServer();
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
        Bukkit.getOnlinePlayers().forEach(o -> o.kickPlayer(ChatColor.WHITE + "Server might be restarting or stopping!"));
        UtilMessage.log("Core", ChatColor.RED + "Plugin Disabled!");
    }

    private void setupServer() {
        int entities = 0;
        for (final World world : Bukkit.getWorlds()) {
            UtilMessage.log("World", "Found " + ChatColor.YELLOW + world.getEntities().stream().filter(e -> e instanceof Item).count() + ChatColor.GRAY + " items.");
            final List<Entity> collect = world.getEntities().stream().filter(e -> e instanceof Item).collect(Collectors.toList());
            while (collect.size() > 1000) {
                collect.get(0).remove();
                collect.remove(0);
            }
            if (world.getEntities().size() > 0) {
                for (final Entity entity : world.getEntities()) {
                    if (entity instanceof Player || entity instanceof ArmorStand || entity instanceof ItemFrame || entity instanceof Item) {
                        continue;
                    }
                    entities++;
                    entity.remove();
                }
            }
        }
        UtilMessage.log("World", "Removed " + ChatColor.YELLOW + entities + ChatColor.GRAY + " entities.");
        entities = 0;
    }

    private void registerEvents() {
        new ConnectionListener(this);
        new CommandCenter(this);
        new EffectManager(this);
        new FishingListener(this);
        new GamerManager(this);
        new ChatListener(this);
        new ItemListener(this);
        new ServerListener(this);
        new WorldListener(this);
    }

    private void registerCommands() {
        getCommand("gamemode").setExecutor(new GamemodeCommand(this));
        getCommandManager().addCommand(new DiscordCommand(this));
        getCommandManager().addCommand(new HelpCommand(this));
        getCommandManager().addCommand(new PingCommand(this));
        getCommandManager().addCommand(new StoreCommand(this));
        getCommandManager().addCommand(new VoteCommand(this));
        getCommandManager().addCommand(new WebsiteCommand(this));
        getCommandManager().addCommand(new AnnounceCommand(this));
        getCommandManager().addCommand(new BroadcastCommand(this));
        getCommandManager().addCommand(new CheckAltsCommand(this));
        getCommandManager().addCommand(new ClearChatCommand(this));
        getCommandManager().addCommand(new ClearCooldownsCommand(this));
        getCommandManager().addCommand(new ClientCommand(this));
        getCommandManager().addCommand(new FeedCommand(this));
        getCommandManager().addCommand(new FlyCommand(this));
        getCommandManager().addCommand(new GodCommand(this));
        getCommandManager().addCommand(new SkullCommand(this));
        getCommandManager().addCommand(new HealCommand(this));
        getCommandManager().addCommand(new MoreCommand(this));
        getCommandManager().addCommand(new ObserverCommand(this));
        getCommandManager().addCommand(new OpenInvCommand(this));
        getCommandManager().addCommand(new PlayerCountCommand(this));
        getCommandManager().addCommand(new StaffChatCommand(this));
        getCommandManager().addCommand(new TimeCommand(this));
        getCommandManager().addCommand(new ToggleChatCommand(this));
        getCommandManager().addCommand(new VanishCommand(this));
        getCommandManager().addCommand(new SpawnCommand(this));
        getCommandManager().addCommand(new SetSpawnCommand(this));
        getCommandManager().addCommand(new ClearInvCommand(this));
        getCommandManager().addCommand(new DyeCommand(this));
        getCommandManager().addCommand(new IgnoreCommand(this));
        getCommandManager().addCommand(new KDRCommand(this));
        getCommandManager().addCommand(new ListCommand(this));
        getCommandManager().addCommand(new MessageCommand(this));
        getCommandManager().addCommand(new ReplyCommand(this));
        getCommandManager().addCommand(new ReportCommand(this));
        getCommandManager().addCommand(new SuicideCommand(this));
        getCommandManager().addCommand(new SupportCommand(this));
        getCommandManager().addCommand(new TrackCommand(this));
        getCommandManager().addCommand(new ReloadCommand(this));
        getCommandManager().addCommand(new ProtectionCommand(this));
    }

    public final boolean hasStarted() {
        return started;
    }

    public final boolean isChat() {
        return chat;
    }

    public void setChat(final boolean chat) {
        this.chat = chat;
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

    public final RechargeManager getRechargeManager() {
        return rechargeManager;
    }

    public final EffectManager getEffectManager() {
        return effectManager;
    }

    public final TitleManager getTitleManager() {
        return titleManager;
    }
}