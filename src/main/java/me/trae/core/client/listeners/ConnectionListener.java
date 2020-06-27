package me.trae.core.client.listeners;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.module.CoreListener;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class ConnectionListener extends CoreListener {

    public ConnectionListener(final Main instance) {
        super(instance);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerLogin(final PlayerLoginEvent e) {
        if (getInstance().hasStarted()) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.WHITE + "Server has not finished starting up.");
            return;
        }
        final Player player = e.getPlayer();
        if (player.isOp()) {
            e.allow();
            return;
        }
        if (Bukkit.hasWhitelist() && !(player.isWhitelisted())) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Server is currently Whitelisted" + "\n\n" + ChatColor.WHITE + "Join our Discord for more Information!" + "\n" + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + ChatColor.STRIKETHROUGH + "https://example.com/discord");
            getInstance().getClientUtilities().messageStaff(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " tried to join, but is not whitelisted.", Rank.ADMIN, null);
            if (getInstance().getClientUtilities().getClient(player.getUniqueId()) == null) {
                final Client client = new Client(player.getUniqueId());
                client.setName(player.getName());
                client.getIPAddresses().add(e.getAddress().getHostAddress());
                getInstance().getClientUtilities().addClient(client);
                getInstance().getClientRepository().saveClient(client);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        Client client = getInstance().getClientUtilities().getClient(player.getUniqueId());
        if (client == null) {
            client = new Client(player.getUniqueId());
            client.setName(client.getName());
            client.getIPAddresses().add(UtilPlayer.getIP(player));
            client.setFirstJoined(System.currentTimeMillis());
            getInstance().getClientRepository().saveClient(client);
            if (client.getRank() == Rank.OWNER) {
                getInstance().getClientUtilities().messageStaff(ChatColor.GREEN + "Join> " + ChatColor.GRAY + player.getName(), Rank.OWNER, null);
            } else {
                UtilMessage.broadcast(ChatColor.GREEN + "Join> " + ChatColor.GRAY + player.getName());
            }
        } else {
            if (!(client.getName().equals(player.getName()))) {
                client.setOldName(client.getName());
                getInstance().getClientRepository().updateOldName(client);
                client.setName(player.getName());
                getInstance().getClientRepository().updateName(client);
            }
            if (client.getIPAddresses().stream().noneMatch(c -> c.equals(UtilPlayer.getIP(player)))) {
                client.getIPAddresses().add(UtilPlayer.getIP(player));
                getInstance().getClientRepository().updateIP(client);
            }
        }
        getInstance().getClientUtilities().addClient(client);
        if (e.getPlayer() != null) {
            getInstance().getClientUtilities().addOnlineClient(client);
            UtilMessage.log("Clients", "Added Online Client: " + ChatColor.YELLOW + client.getName());
        }
    }
}