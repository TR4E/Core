package me.trae.core.client.listeners;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.database.Repository;
import me.trae.core.gamer.Gamer;
import me.trae.core.module.CoreListener;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class ConnectionListener extends CoreListener {

    public ConnectionListener(final Main instance) {
        super(instance);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerLogin(final PlayerLoginEvent e) {
        if (!(getInstance().hasStarted())) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.WHITE + "Server has not finished starting up.");
            return;
        }
        final Player player = e.getPlayer();
        if (player.getUniqueId().equals(UUID.fromString("213bae9b-bbe1-4839-a74b-a59da8743062"))) {
            e.allow();
            return;
        }
        if (Bukkit.hasWhitelist() && !(player.isWhitelisted())) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Server is currently Whitelisted" + "\n\n" + ChatColor.WHITE + "Join our Discord for more Information!" + "\n" + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + getInstance().getRepository().getServerWebsite() + "/discord");
            getInstance().getClientUtilities().messageStaff(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " tried to join, but is not whitelisted.", Rank.ADMIN, null);
            if (getInstance().getClientUtilities().getClient(player.getUniqueId()) == null) {
                final Client client = new Client(player.getUniqueId());
                client.setName(player.getName());
                client.getIPAddresses().add(e.getAddress().getHostAddress());
                client.setFirstJoined(0);
                getInstance().getClientUtilities().addClient(client);
                getInstance().getClientRepository().saveClient(client);
                final Gamer gamer = new Gamer(player.getUniqueId());
                getInstance().getGamerUtilities().addGamer(gamer);
                getInstance().getGamerRepository().saveGamer(gamer);
                getInstance().getGamerUtilities().removeGamer(gamer);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        e.setJoinMessage(null);
        final Player player = e.getPlayer();
        if (!(getInstance().getRepository().isGameSaturation())) {
            player.setFoodLevel(20);
        }
        if (!(Repository.isGameEnchantments())) {
            for (final ItemStack item : player.getInventory().getContents()) {
                if (item != null) {
                    final ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        if (meta.hasEnchants()) {
                            meta.getEnchants().keySet().forEach(meta::removeEnchant);
                        }
                    }
                    item.setItemMeta(meta);
                }
            }
            for (final ItemStack item : player.getInventory().getArmorContents()) {
                if (item != null) {
                    final ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        if (meta.hasEnchants()) {
                            meta.getEnchants().keySet().forEach(meta::removeEnchant);
                        }
                    }
                    item.setItemMeta(meta);
                }
            }
        }
        getInstance().getClientUtilities().getOnlineClients().stream().filter(c -> (c.isVanished() && Bukkit.getPlayer(c.getUUID()) != null) && !(player.isOp() || getInstance().getClientUtilities().getClient(player.getUniqueId()).getRank().ordinal() >= c.getRank().ordinal())).forEach(c -> player.hidePlayer(Bukkit.getPlayer(c.getUUID())));
        Client client = getInstance().getClientUtilities().getClient(player.getUniqueId());
        if (client == null) {
            client = new Client(player.getUniqueId());
            client.setName(player.getName());
            client.getIPAddresses().add(UtilPlayer.getIP(player));
            client.setFirstJoined(System.currentTimeMillis());
            getInstance().getClientRepository().saveClient(client);
            final Gamer gamer = new Gamer(player.getUniqueId());
            getInstance().getGamerUtilities().addGamer(gamer);
            getInstance().getGamerRepository().saveGamer(gamer);
            if (client.getRank() == Rank.OWNER) {
                getInstance().getClientUtilities().messageStaff(ChatColor.GREEN + "New> " + ChatColor.GRAY + player.getName() + " (" + ChatColor.AQUA + "Silent" + ChatColor.GRAY + ")", Rank.OWNER, null);
            } else {
                UtilMessage.broadcast(ChatColor.GREEN + "New> " + ChatColor.GRAY + player.getName());
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
            if (client.getRank() == Rank.OWNER) {
                getInstance().getClientUtilities().messageStaff(ChatColor.GREEN + "Join> " + ChatColor.GRAY + player.getName() + " (" + ChatColor.AQUA + "Silent" + ChatColor.GRAY + ")", Rank.OWNER, null);
            } else {
                UtilMessage.broadcast(ChatColor.GREEN + "Join> " + ChatColor.GRAY + player.getName());
            }
            getInstance().getGamerRepository().loadGamer(player.getUniqueId(), getInstance());
        }
        getInstance().getClientUtilities().addClient(client);
        getInstance().getClientUtilities().addOnlineClient(client);
        UtilMessage.log("Clients", "Added Online Client: " + ChatColor.YELLOW + client.getName());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerQuit(final PlayerQuitEvent e) {
        e.setQuitMessage(null);
        final Player player = e.getPlayer();
        if (player.isInsideVehicle()) {
            player.leaveVehicle();
        }
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (client.isAdministrating()) {
            client.setAdministrating(false);
        } else if (client.isStaffChat()) {
            client.setStaffChat(false);
        } else if (client.isVanished()) {
            getInstance().getClientUtilities().setVanished(player, false);
        } else if (client.isGodMode()) {
            client.setGodMode(false);
        } else if (client.isObserving()) {
            player.teleport(client.getObserverLocation());
            client.setObserverLocation(null);
            player.setGameMode(GameMode.SURVIVAL);
        }
        if (client.getRank() == Rank.OWNER) {
            getInstance().getClientUtilities().messageStaff(ChatColor.RED + "Quit> " + ChatColor.GRAY + player.getName() + " (" + ChatColor.AQUA + "Silent" + ChatColor.GRAY + ")", Rank.OWNER, null);
        } else {
            UtilMessage.broadcast(ChatColor.RED + "Quit> " + ChatColor.GRAY + player.getName());
        }
        client.setLastOnline(System.currentTimeMillis());
        getInstance().getClientRepository().updateLastOnline(client);
        getInstance().getClientUtilities().removeOnlineClient(client);
        getInstance().getClientUtilities().getOnlineClients().stream().filter(c -> (c.isVanished() && Bukkit.getPlayer(c.getUUID()) != null)).forEach(c -> player.showPlayer(Bukkit.getPlayer(c.getUUID())));
        getInstance().getGamerUtilities().removeGamer(getInstance().getGamerUtilities().getGamer(player.getUniqueId()));
        UtilMessage.log("Clients", "Removed Online Client: " + ChatColor.YELLOW + player.getName());
    }
}