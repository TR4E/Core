package me.trae.core.client.commands.help;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PingCommand extends Command {

    public PingCommand(final Main instance) {
        super(instance, "ping", new String[]{}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            UtilMessage.message(player, "Ping", "Your ping is " + ChatColor.GREEN + UtilPlayer.getPing(player) + "ms" + ChatColor.GRAY + ".");
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            if (target == player) {
                UtilMessage.message(player, "Ping", "Your ping is " + ChatColor.GREEN + UtilPlayer.getPing(player) + "ms" + ChatColor.GRAY + ".");
                return;
            }
            if (getInstance().getEffectManager().isVanished(target)) {
                UtilMessage.message(player, "Player Search", ChatColor.YELLOW + "0" + ChatColor.GRAY + " matches found [" + ChatColor.YELLOW + args[0] + ChatColor.GRAY + "]");
                return;
            }
            UtilMessage.message(player, "Ping", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "'s ping is " + ChatColor.GREEN + UtilPlayer.getPing(target) + "ms" + ChatColor.GRAY + ".");
        }
    }

    @Override
    public void help(final Player player) {

    }
}