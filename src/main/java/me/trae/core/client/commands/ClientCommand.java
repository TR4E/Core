package me.trae.core.client.commands;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import me.trae.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientCommand extends Command {

    public ClientCommand(final Main instance) {
        super(instance, "client", new String[]{}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
            return;
        }
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (args[0].equalsIgnoreCase("admin")) {
            if (player.isOp() || client.hasRank(Rank.ADMIN, true)) {
                adminCommand(player);
            }
        } else if (args[0].equalsIgnoreCase("search")) {
            if (player.isOp() || client.hasRank(Rank.MOD, true)) {
                searchCommand(player, args);
            }
        } else if (args[0].equalsIgnoreCase("promote")) {
            if (player.isOp() || client.hasRank(Rank.OWNER, true)) {
                promoteCommand(player, args);
            }
        } else if (args[0].equalsIgnoreCase("demote")) {
            if (player.isOp() || client.hasRank(Rank.OWNER, true)) {
                demoteCommand(player, args);
            }
        } else {
            if (player.isOp() || client.hasRank(Rank.MOD, true)) {
                help(player);
            }
        }
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Client", "Command List:");
        if (player.isOp() || getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).hasRank(Rank.ADMIN, false)) {
            UtilMessage.message(player, ChatColor.AQUA + "/client admin" + ChatColor.GRAY + " - " + "Toggle Client Admin.");
        }
        if (player.isOp() || getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).hasRank(Rank.MOD, false)) {
            UtilMessage.message(player, ChatColor.AQUA + "/client search <client>" + ChatColor.GRAY + " - " + "Search a Client.");
        }
        if (player.isOp() || getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).hasRank(Rank.OWNER, false)) {
            UtilMessage.message(player, ChatColor.AQUA + "/client promote <client>" + ChatColor.GRAY + " - " + "Promote a Client.");
            UtilMessage.message(player, ChatColor.AQUA + "/client demote <client>" + ChatColor.GRAY + " - " + "Demote a Client.");
        }
    }

    private void adminCommand(final Player player) {
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        client.setAdministrating(!(client.isAdministrating()));
        UtilMessage.message(player, "Client", "Admin Mode: " + (client.isAdministrating() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
        getInstance().getClientUtilities().messageStaff("Client", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + (client.isAdministrating() ? " is now Administrating" : "is no longer Administrating") + ".", Rank.ADMIN, new UUID[]{player.getUniqueId()});
    }

    private void searchCommand(final Player player, final String[] args) {
        if (args.length == 1) {
            UtilMessage.message(player, "Client", "Usage: " + ChatColor.AQUA + "/client search <client>");
            return;
        }
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        final Client target = getInstance().getClientUtilities().searchClient(player, args[1], true);
        if (target == null) {
            return;
        }
        UtilMessage.message(player, "Client", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " Information:");
        UtilMessage.message(player, ChatColor.GREEN + "UUID: " + ChatColor.WHITE + target.getUUID());
        if (target.getOldName() != null) {
            UtilMessage.message(player, ChatColor.GREEN + "Previous Name: " + ChatColor.WHITE + target.getOldName());
        }
        final List<String> ips = new ArrayList<>(target.getIPAddresses());
        if (Bukkit.getPlayer(target.getUUID()) != null) {
            UtilMessage.message(player, ChatColor.GREEN + "IP Address: " + ChatColor.WHITE + UtilPlayer.getIP(Bukkit.getPlayer(target.getUUID())));
            ips.remove(UtilPlayer.getIP(Bukkit.getPlayer(target.getUUID())));
        }
        if (!(ips.isEmpty())) {
            UtilMessage.message(player, ChatColor.GREEN + "IP Alias: " + ChatColor.WHITE + ips);
        }
        UtilMessage.message(player, ChatColor.GREEN + "Rank: " + ChatColor.WHITE + UtilFormat.cleanString(target.getRank().getPrefix()));
        UtilMessage.message(player, ChatColor.GREEN + "First Joined: " + ChatColor.WHITE + UtilTime.getTime(System.currentTimeMillis() - target.getFirstJoined(), UtilTime.TimeUnit.BEST, 1));
        if (Bukkit.getPlayer(target.getUUID()) == null) {
            UtilMessage.message(player, ChatColor.GREEN + "Last Online: " + ChatColor.WHITE + UtilTime.getTime(System.currentTimeMillis() - target.getLastOnline(), UtilTime.TimeUnit.BEST, 1));
        }
        if (Bukkit.getPlayer(target.getUUID()) != null) {
            UtilMessage.message(player, ChatColor.GREEN + "Admin Mode: " + ChatColor.WHITE + (target.isAdministrating() ? "Enabled" : "Disabled"));
            UtilMessage.message(player, ChatColor.GREEN + "Vanish Mode: " + ChatColor.WHITE + (target.isVanished() ? "Enabled" : "Disabled"));
            UtilMessage.message(player, ChatColor.GREEN + "God Mode: " + ChatColor.WHITE + (target.isGodMode() ? "Enabled" : "Disabled"));
        }
    }

    private void promoteCommand(final Player player, final String[] args) {
        if (args.length == 1) {
            UtilMessage.message(player, "Client", "Usage: " + ChatColor.AQUA + "/client promote <client>");
            return;
        }
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        final Client target = getInstance().getClientUtilities().searchClient(player, args[1], true);
        if (target == null) {
            return;
        }
        if (target.getRank() == Rank.OWNER) {
            UtilMessage.message(player, "Client", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " cannot be promoted any further.");
            return;
        }
        target.setRank(Rank.getRank(target.getRank().ordinal() + 1));
        getInstance().getClientRepository().updateRank(target);
        UtilMessage.broadcast("Client", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " promoted " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + target.getRank().getTag(true) + ChatColor.GRAY + ".");
    }

    private void demoteCommand(final Player player, final String[] args) {
        if (args.length == 1) {
            UtilMessage.message(player, "Client", "Usage: " + ChatColor.AQUA + "/client demote <client>");
            return;
        }
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        final Client target = getInstance().getClientUtilities().searchClient(player, args[1], true);
        if (target == null) {
            return;
        }
        if (target.getRank() == Rank.PLAYER) {
            UtilMessage.message(player, "Client", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " cannot be demoted any further.");
            return;
        }
        target.setRank(Rank.getRank(target.getRank().ordinal() - 1));
        getInstance().getClientRepository().updateRank(target);
        if (target.getRank() == Rank.HEADMOD) {
            target.setAdministrating(false);
            target.setVanished(false);
            target.setGodMode(false);
            if (target.isObserving()) {
                if (Bukkit.getPlayer(target.getUUID()) != null) {
                    Bukkit.getPlayer(target.getUUID()).teleport(target.getObserverLocation());
                    target.setObserverLocation(null);
                    Bukkit.getPlayer(target.getUUID()).setGameMode(GameMode.SURVIVAL);
                }
            }
        } else if (target.getRank() == Rank.HELPER) {
            target.setStaffChat(false);
        }
        UtilMessage.broadcast("Client", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " demoted " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + target.getRank().getTag(true) + ChatColor.GRAY + ".");
    }
}