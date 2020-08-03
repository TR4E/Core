package me.trae.core.client.commands.teleport;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeleportCommand implements CommandExecutor {

    private final Main instance;

    public TeleportCommand(final Main instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Unknown command. Type \"/help\" for help.");
            return false;
        }
        final Player player = (Player) sender;
        final Client client = instance.getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return false;
        }
        if (label.equalsIgnoreCase("teleport") || label.equalsIgnoreCase("tp")) {
            if (player.isOp() || client.hasRank(Rank.HEADMOD, true)) {
                tpCommand(player, args);
            }
        } else if (label.equalsIgnoreCase("tpo")) {
            if (player.isOp() || client.hasRank(Rank.ADMIN, true)) {
                tpoCommand(player, args);
            }
        } else if (label.equalsIgnoreCase("tppos")) {
            if (player.isOp() || client.hasRank(Rank.ADMIN, true)) {
                tpPosCommand(player, args);
            }
        } else if (label.equalsIgnoreCase("tphere")) {
            if (player.isOp() || client.hasRank(Rank.ADMIN, true)) {
                tpHereCommand(player, args);
            }
        } else if (label.equalsIgnoreCase("tpall")) {
            if (player.isOp() || client.hasRank(Rank.OWNER, true)) {
                tpAllCommand(player, args);
            }
        }
        return false;
    }

    private void tpPosCommand(final Player player, final String[] args) {
        if (args == null || args.length <= 2) {
            UtilMessage.message(player, "Teleport", "Usage: " + ChatColor.AQUA + "/tppos <x> <y> <z>");
            return;
        }
        final Location loc = new Location(player.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        player.teleport(loc);
        UtilMessage.message(player, "Teleport", "You teleported to " + UtilLocation.locToString(loc) + ChatColor.GRAY + ".");
        instance.getClientUtilities().messageStaff("Teleport", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported to " + UtilLocation.locToString(loc) + ChatColor.GRAY + ".", Rank.OWNER, new UUID[]{player.getUniqueId()});
    }

    private void tpHereCommand(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            UtilMessage.message(player, "Teleport", "Usage: " + ChatColor.AQUA + "/tppos <x> <y> <z>");
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            target.teleport(player);
            UtilMessage.message(player, "Teleport", "You teleported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to You.");
            if (player != target) {
                UtilMessage.message(target, "Teleport", "You have been teleported to " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".");
            }
            instance.getClientUtilities().messageStaff("Teleport", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".", Rank.ADMIN, new UUID[]{player.getUniqueId(), target.getUniqueId()});
        }
    }

    private void tpAllCommand(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            Bukkit.getOnlinePlayers().forEach(o -> o.teleport(player));
            UtilMessage.broadcast("Teleport", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported everyone to " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".");
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            Bukkit.getOnlinePlayers().forEach(o -> o.teleport(target));
            UtilMessage.broadcast("Teleport", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported everyone to " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
        }
    }

    private void tpCommand(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            UtilMessage.message(player, "Teleport", "Usage: " + ChatColor.AQUA + "/tp <player> <target>");
            UtilMessage.message(player, "Teleport", "Usage: " + ChatColor.AQUA + "/tp <player>");
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            player.teleport(target);
            UtilMessage.message(player, "Teleport", "You teleported to " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
            if (target != player && (target.isOp() || instance.getClientUtilities().getOnlineClient(target.getUniqueId()).hasRank(Rank.ADMIN, false))) {
                UtilMessage.message(target, "Teleport", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported to You.");
            }
            instance.getClientUtilities().messageStaff("Teleport", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported to " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".", Rank.ADMIN, new UUID[]{player.getUniqueId(), target.getUniqueId()});
            return;
        }
        if (!(player.isOp() || instance.getClientUtilities().getOnlineClient(player.getUniqueId()).hasRank(Rank.ADMIN, true))) {
            return;
        }
        if (args.length == 2) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            final Player target2 = UtilPlayer.searchPlayer(player, args[1], true);
            if (target2 == null) {
                return;
            }
            target.teleport(target2);
            UtilMessage.message(player, "Teleport", "You teleported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + ChatColor.YELLOW + target2.getName() + ChatColor.GRAY + ".");
            if (player != target && player != target2) {
                UtilMessage.message(target, "Teleport", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported you to " + ChatColor.YELLOW + target2.getName() + ChatColor.GRAY + ".");
                if ((target.isOp() || instance.getClientUtilities().getOnlineClient(target.getUniqueId()).hasRank(Rank.ADMIN, false))) {
                    UtilMessage.message(target2, "Teleport", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to You.");
                }
            }
            instance.getClientUtilities().messageStaff("Teleport", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + ChatColor.YELLOW + target2.getName() + ChatColor.GRAY + ".", Rank.ADMIN, new UUID[]{player.getUniqueId(), target.getUniqueId(), target2.getUniqueId()});
        }
    }


    private void tpoCommand(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            UtilMessage.message(player, "Teleport", "Usage: " + ChatColor.AQUA + "/tpo <player> <target>");
            UtilMessage.message(player, "Teleport", "Usage: " + ChatColor.AQUA + "/tpo <player>");
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            player.teleport(target);
            UtilMessage.message(player, "Teleport", "You teleported to " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
            instance.getClientUtilities().messageStaff("Teleport", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported to " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".", Rank.OWNER, new UUID[]{player.getUniqueId(), target.getUniqueId()});
            return;
        }
        if (!(player.isOp() || instance.getClientUtilities().getOnlineClient(player.getUniqueId()).hasRank(Rank.ADMIN, true))) {
            return;
        }
        if (args.length == 2) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            final Player target2 = UtilPlayer.searchPlayer(player, args[1], true);
            if (target2 == null) {
                return;
            }
            target.teleport(target2);
            UtilMessage.message(player, "Teleport", "You teleported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + ChatColor.YELLOW + target2.getName() + ChatColor.GRAY + ".");
            instance.getClientUtilities().messageStaff("Teleport", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + ChatColor.YELLOW + target2.getName() + ChatColor.GRAY + ".", Rank.OWNER, new UUID[]{player.getUniqueId(), target.getUniqueId(), target2.getUniqueId()});
        }
    }
}