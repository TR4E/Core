package me.trae.core.client.commands;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KDRCommand extends Command {

    public KDRCommand(final Main instance) {
        super(instance, "kdr", new String[]{"kills", "deaths", "ratio"}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            final Gamer gamer = getInstance().getGamerUtilities().getGamer(player.getUniqueId());
            if (gamer == null) {
                return;
            }
            UtilMessage.message(player, "KDR", "Your Statistics:");
            UtilMessage.message(player, ChatColor.GREEN + "Kills: " + ChatColor.WHITE + gamer.getKills());
            UtilMessage.message(player, ChatColor.GREEN + "Deaths: " + ChatColor.WHITE + gamer.getDeaths());
            UtilMessage.message(player, ChatColor.GREEN + "Ratio: " + ChatColor.WHITE + gamer.getRatio());
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            if (getInstance().getEffectManager().isVanished(player)) {
                UtilMessage.message(player, "Player Search", ChatColor.YELLOW + "0" + ChatColor.GRAY + " matches found [" + ChatColor.YELLOW + args[0] + ChatColor.GRAY + "]");
                return;
            }
            final Gamer gamer = getInstance().getGamerUtilities().getGamer(target.getUniqueId());
            if (gamer == null) {
                return;
            }
            UtilMessage.message(player, "KDR", ((player != target) ? ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "'s" : "Your") + " KDR Stats:");
            UtilMessage.message(player, ChatColor.GREEN + "Kills: " + ChatColor.WHITE + gamer.getKills());
            UtilMessage.message(player, ChatColor.GREEN + "Deaths: " + ChatColor.WHITE + gamer.getDeaths());
            UtilMessage.message(player, ChatColor.GREEN + "Ratio: " + ChatColor.WHITE + gamer.getRatio());
        }
    }

    @Override
    public void help(final Player player) {

    }
}