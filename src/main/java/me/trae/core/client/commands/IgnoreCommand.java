package me.trae.core.client.commands;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class IgnoreCommand extends Command {

    public IgnoreCommand(final Main instance) {
        super(instance, "ignore", new String[]{}, Rank.PLAYER);
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
        final Gamer gamer = getInstance().getGamerUtilities().getGamer(player.getUniqueId());
        if (gamer == null) {
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            if (target == player) {
                UtilMessage.message(player, "Ignore", "You cannot ignore yourself.");
                return;
            }
            final Client targetC = getInstance().getClientUtilities().getOnlineClient(target.getUniqueId());
            if (targetC == null) {
                return;
            }
            if (!(player.isOp())) {
                if (targetC.getRank().ordinal() >= client.getRank().ordinal()) {
                    UtilMessage.message(player, "Ignore", "You cannot ignore this Player!");
                    return;
                }
                if (getInstance().getEffectManager().isVanished(target)) {
                    UtilMessage.message(player, "Player Search", ChatColor.YELLOW + "0" + ChatColor.GRAY + " matches found [" + ChatColor.YELLOW + args[0] + ChatColor.GRAY + "]");
                    return;
                }
            }
            if (gamer.getIgnored().contains(target.getUniqueId())) {
                gamer.getIgnored().remove(target.getUniqueId());
            } else {
                gamer.getIgnored().add(target.getUniqueId());
            }
            getInstance().getGamerRepository().updateIgnored(gamer);
            UtilMessage.message(player, "Ignore", (gamer.getIgnored().contains(target.getUniqueId()) ? "You are now ignoring " : "You are no longer ignoring ") + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
            getInstance().getClientUtilities().messageAdmins("Ignore", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + (gamer.getIgnored().contains(target.getUniqueId()) ? " is now ignoring " : " is no longer ignoring ") + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".", null);
        }
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Ignore", "Usage: " + ChatColor.AQUA + "/ignore <player>");
    }
}