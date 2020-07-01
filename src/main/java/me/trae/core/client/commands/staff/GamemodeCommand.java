package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class GamemodeCommand implements CommandExecutor {

    private final Main instance;

    public GamemodeCommand(final Main instance) {
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
        if (!(client.hasRank(Rank.ADMIN, true))) {
            return false;
        }
        if (label.equalsIgnoreCase("gamemode") || label.equalsIgnoreCase("gm")) {
            if (args == null || args.length == 0) {
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    player.setGameMode(GameMode.CREATIVE);
                } else if (player.getGameMode() == GameMode.CREATIVE) {
                    player.setGameMode(GameMode.SURVIVAL);
                } else if (player.getGameMode() == GameMode.ADVENTURE) {
                    player.setGameMode(GameMode.SURVIVAL);
                } else if (player.getGameMode() == GameMode.SPECTATOR) {
                    player.setGameMode(GameMode.CREATIVE);
                }
                UtilMessage.message(player, "Gamemode", "You updated your game mode to " + ChatColor.GREEN + UtilFormat.cleanString(player.getGameMode().name()) + ChatColor.GRAY + ".");
                return false;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survival")) {
                    player.setGameMode(GameMode.SURVIVAL);
                } else if (args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creative")) {
                    player.setGameMode(GameMode.CREATIVE);
                } else if (args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("adventure")) {
                    player.setGameMode(GameMode.ADVENTURE);
                } else if (args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spectator")) {
                    player.setGameMode(GameMode.SPECTATOR);
                }
                UtilMessage.message(player, "Gamemode", "You updated your game mode to " + ChatColor.GREEN + UtilFormat.cleanString(player.getGameMode().name()) + ChatColor.GRAY + ".");
                return false;
            }
            if (args.length == 2) {
                final Player target = UtilPlayer.searchPlayer(player, args[1], true);
                if (target == null) {
                    return false;
                }
                final Client targetC = instance.getClientUtilities().getOnlineClient(target.getUniqueId());
                if (targetC == null) {
                    return false;
                }
                if (targetC.getRank().ordinal() >= client.getRank().ordinal()) {
                    UtilMessage.message(player, "Client", "You do not outrank " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
                    return false;
                }
                if (args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survival")) {
                    target.setGameMode(GameMode.SURVIVAL);
                } else if (args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creative")) {
                    target.setGameMode(GameMode.CREATIVE);
                } else if (args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("adventure")) {
                    target.setGameMode(GameMode.ADVENTURE);
                } else if (args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spectator")) {
                    target.setGameMode(GameMode.SPECTATOR);
                }
                if (target == player) {
                    UtilMessage.message(player, "Gamemode", "You updated your game mode to " + ChatColor.GREEN + UtilFormat.cleanString(player.getGameMode().name()) + ChatColor.GRAY + ".");
                } else {
                    UtilMessage.message(player, "Gamemode", "You updated " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "'s game mode to " + ChatColor.GREEN + UtilFormat.cleanString(target.getGameMode().name()) + ChatColor.GRAY + ".");
                    UtilMessage.message(target, "Gamemode", "Your game mode has been updated to " + ChatColor.GREEN + UtilFormat.cleanString(target.getGameMode().name()) + ChatColor.GRAY + " by " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".");
                }
            }
        } else if (label.equalsIgnoreCase("gms")) {
            if (args == null || args.length == 0) {
                player.setGameMode(GameMode.SURVIVAL);
                UtilMessage.message(player, "Gamemode", "You updated your game mode to " + ChatColor.GREEN + UtilFormat.cleanString(player.getGameMode().name()) + ChatColor.GRAY + ".");
                return false;
            }
            if (args.length == 1) {
                final Player target = UtilPlayer.searchPlayer(player, args[0], true);
                if (target == null) {
                    return false;
                }
                final Client targetC = instance.getClientUtilities().getOnlineClient(target.getUniqueId());
                if (targetC == null) {
                    return false;
                }
                if (targetC.getRank().ordinal() >= client.getRank().ordinal()) {
                    UtilMessage.message(player, "Client", "You do not outrank " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
                    return false;
                }
                target.setGameMode(GameMode.SURVIVAL);
                if (target == player) {
                    UtilMessage.message(player, "Gamemode", "You updated your game mode to " + ChatColor.GREEN + UtilFormat.cleanString(player.getGameMode().name()) + ChatColor.GRAY + ".");
                } else {
                    UtilMessage.message(player, "Gamemode", "You updated " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "'s game mode to " + ChatColor.GREEN + UtilFormat.cleanString(target.getGameMode().name()) + ChatColor.GRAY + ".");
                    UtilMessage.message(target, "Gamemode", "Your game mode has been updated to " + ChatColor.GREEN + UtilFormat.cleanString(target.getGameMode().name()) + ChatColor.GRAY + " by " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".");
                }
            }
        } else if (label.equalsIgnoreCase("gmc")) {
            if (args == null || args.length == 0) {
                player.setGameMode(GameMode.CREATIVE);
                UtilMessage.message(player, "Gamemode", "You updated your game mode to " + ChatColor.GREEN + UtilFormat.cleanString(player.getGameMode().name()) + ChatColor.GRAY + ".");
                return false;
            }
            if (args.length == 1) {
                final Player target = UtilPlayer.searchPlayer(player, args[0], true);
                if (target == null) {
                    return false;
                }
                final Client targetC = instance.getClientUtilities().getOnlineClient(target.getUniqueId());
                if (targetC == null) {
                    return false;
                }
                if (targetC.getRank().ordinal() >= client.getRank().ordinal()) {
                    UtilMessage.message(player, "Client", "You do not outrank " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
                    return false;
                }
                target.setGameMode(GameMode.CREATIVE);
                if (target == player) {
                    UtilMessage.message(player, "Gamemode", "You updated your game mode to " + ChatColor.GREEN + UtilFormat.cleanString(player.getGameMode().name()) + ChatColor.GRAY + ".");
                } else {
                    UtilMessage.message(player, "Gamemode", "You updated " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "'s game mode to " + ChatColor.GREEN + UtilFormat.cleanString(target.getGameMode().name()) + ChatColor.GRAY + ".");
                    UtilMessage.message(target, "Gamemode", "Your game mode has been updated to " + ChatColor.GREEN + UtilFormat.cleanString(target.getGameMode().name()) + ChatColor.GRAY + " by " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".");
                }
            }
        } else if (label.equalsIgnoreCase("gma")) {
            if (args == null || args.length == 0) {
                player.setGameMode(GameMode.ADVENTURE);
                UtilMessage.message(player, "Gamemode", "You updated your game mode to " + ChatColor.GREEN + UtilFormat.cleanString(player.getGameMode().name()) + ChatColor.GRAY + ".");
                return false;
            }
            if (args.length == 1) {
                final Player target = UtilPlayer.searchPlayer(player, args[0], true);
                if (target == null) {
                    return false;
                }
                final Client targetC = instance.getClientUtilities().getOnlineClient(target.getUniqueId());
                if (targetC == null) {
                    return false;
                }
                if (targetC.getRank().ordinal() >= client.getRank().ordinal()) {
                    UtilMessage.message(player, "Client", "You do not outrank " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
                    return false;
                }
                target.setGameMode(GameMode.ADVENTURE);
                if (target == player) {
                    UtilMessage.message(player, "Gamemode", "You updated your game mode to " + ChatColor.GREEN + UtilFormat.cleanString(player.getGameMode().name()) + ChatColor.GRAY + ".");
                } else {
                    UtilMessage.message(player, "Gamemode", "You updated " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "'s game mode to " + ChatColor.GREEN + UtilFormat.cleanString(target.getGameMode().name()) + ChatColor.GRAY + ".");
                    UtilMessage.message(target, "Gamemode", "Your game mode has been updated to " + ChatColor.GREEN + UtilFormat.cleanString(target.getGameMode().name()) + ChatColor.GRAY + " by " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".");
                }
            }
        } else if (label.equalsIgnoreCase("gmsp")) {
            if (args == null || args.length == 0) {
                player.setGameMode(GameMode.SPECTATOR);
                UtilMessage.message(player, "Gamemode", "You updated your game mode to " + ChatColor.GREEN + UtilFormat.cleanString(player.getGameMode().name()) + ChatColor.GRAY + ".");
                return false;
            }
            if (args.length == 1) {
                final Player target = UtilPlayer.searchPlayer(player, args[0], true);
                if (target == null) {
                    return false;
                }
                final Client targetC = instance.getClientUtilities().getOnlineClient(target.getUniqueId());
                if (targetC == null) {
                    return false;
                }
                if (targetC.getRank().ordinal() >= client.getRank().ordinal()) {
                    UtilMessage.message(player, "Client", "You do not outrank " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
                    return false;
                }
                target.setGameMode(GameMode.SPECTATOR);
                if (target == player) {
                    UtilMessage.message(player, "Gamemode", "You updated your game mode to " + ChatColor.GREEN + UtilFormat.cleanString(player.getGameMode().name()) + ChatColor.GRAY + ".");
                } else {
                    UtilMessage.message(player, "Gamemode", "You updated " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "'s game mode to " + ChatColor.GREEN + UtilFormat.cleanString(target.getGameMode().name()) + ChatColor.GRAY + ".");
                    UtilMessage.message(target, "Gamemode", "Your game mode has been updated to " + ChatColor.GREEN + UtilFormat.cleanString(target.getGameMode().name()) + ChatColor.GRAY + " by " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".");
                }
            }
        }
        return false;
    }
}