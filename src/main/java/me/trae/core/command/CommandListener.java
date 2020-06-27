package me.trae.core.command;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.module.CoreListener;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener extends CoreListener {

    public CommandListener(final Main instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandChat(final PlayerCommandPreprocessEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(player.isOp())) {
            if (e.getMessage().startsWith("/?")) {
                e.setMessage("/help");
            }
            if (e.getMessage().startsWith("/") && e.getMessage().contains(":") && !(e.getMessage().contains(" "))) {
                e.setCancelled(true);
                return;
            }
            if (e.getMessage().equalsIgnoreCase("/pl") || e.getMessage().toLowerCase().startsWith("/plugins")) {
                e.setCancelled(true);
                UtilMessage.message(player, ChatColor.WHITE + "Plugins (3): " + ChatColor.GREEN + "Core", ChatColor.WHITE + ", " + ChatColor.GREEN + "WorldEdit" + ChatColor.WHITE + ", " + ChatColor.GREEN + "Buycraft");
                return;
            }
        }
        if (e.getMessage().equalsIgnoreCase("/x")) {
            e.setMessage("/client admin");
        }
        String cmd = e.getMessage().substring(1);
        String[] args = null;
        if (cmd.contains(" ")) {
            cmd = cmd.split(" ")[0];
            args = e.getMessage().substring(e.getMessage().indexOf(' ') + 1).split(" ");
        }
        if (cmd == null) {
            return;
        }
        final Command command = getInstance().getCommandManager().getCommand(cmd);
        if (command != null) {
            if (player.isOp() || client.hasRank(command.getRequiredRank(), true)) {
                e.setCancelled(true);
                command.execute(player, args);
                return;
            }
            e.setCancelled(true);
        } else {
            e.setCancelled(true);
            UtilMessage.message(player, ChatColor.WHITE + "Unknown command. Type \"/help\" for help.");
        }
    }
}