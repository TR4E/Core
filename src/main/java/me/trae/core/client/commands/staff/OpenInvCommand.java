package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class OpenInvCommand extends Command {

    public OpenInvCommand(final Main instance) {
        super(instance, "openinventory", new String[]{"openinv", "invsee"}, Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
            return;
        }
        if (args.length == 1) {
            final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
            if (client == null) {
                return;
            }
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            if (target == player) {
                UtilMessage.message(player, "Inventory", "You cannot open your own Inventory.");
                return;
            }
            final Client targetC = getInstance().getClientUtilities().getOnlineClient(target.getUniqueId());
            if (targetC == null) {
                return;
            }
            if (!(player.isOp())) {
                if (targetC.getRank().ordinal() >= client.getRank().ordinal()) {
                    UtilMessage.message(player, "Inventory", "You cannot open this player's Inventory.");
                    return;
                }
            }
            player.openInventory(target.getInventory());
            getInstance().getGamerUtilities().getGamer(player.getUniqueId()).setInvInspecting(target.getUniqueId());
            UtilMessage.message(player, "Inventory", "You opened " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "'s Inventory.");
            getInstance().getClientUtilities().messageStaff("Inventory", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " opened " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "'s Inventory.", Rank.OWNER, new UUID[]{player.getUniqueId(), target.getUniqueId()});
        }
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Inventory", "Usage: " + ChatColor.AQUA + "/openinv <player>");
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player) {
            final Player player = (Player) e.getPlayer();
            if (player != null) {
                if (getInstance().getGamerUtilities().getGamer(player.getUniqueId()).getInvInspecting() != null) {
                    getInstance().getGamerUtilities().getGamer(player.getUniqueId()).setInvInspecting(null);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        for (final Gamer gamer : getInstance().getGamerUtilities().getGamers()) {
            if (gamer.getInvInspecting() != null) {
                if (gamer.getInvInspecting().equals(player.getUniqueId())) {
                    gamer.setInvInspecting(null);
                    if (Bukkit.getPlayer(gamer.getUUID()) != null) {
                        Bukkit.getPlayer(gamer.getUUID()).closeInventory();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e) {
        final Player player = e.getEntity().getPlayer();
        for (final Gamer gamer : getInstance().getGamerUtilities().getGamers()) {
            if (gamer.getInvInspecting() != null) {
                if (gamer.getInvInspecting().equals(player.getUniqueId())) {
                    gamer.setInvInspecting(null);
                    if (Bukkit.getPlayer(gamer.getUUID()) != null) {
                        Bukkit.getPlayer(gamer.getUUID()).closeInventory();
                    }
                }
            }
        }
    }
}