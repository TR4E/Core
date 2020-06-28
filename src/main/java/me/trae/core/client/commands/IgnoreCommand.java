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
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        final Gamer gamer = getInstance().getGamerUtilities().getGamer(player.getUniqueId());
        if (gamer == null) {
            return;
        }
        if (args == null || args.length == 0) {
            help(player);
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
                    UtilMessage.message(player, "Ignore", "You cannot ignore this player!");
                    return;
                }
            }
            if (gamer.getIgnored().contains(target.getUniqueId())) {
                gamer.getIgnored().remove(target.getUniqueId());
            } else {
                gamer.getIgnored().add(target.getUniqueId());
            }
            UtilMessage.message(player, "Ignore", "You are now ignoring " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
            getInstance().getClientUtilities().messageAdmins("Ignore", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " is now ignoring " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".", null);
        }
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Ignore", "Usage: " + ChatColor.AQUA + "/ignore <player>");
    }
}