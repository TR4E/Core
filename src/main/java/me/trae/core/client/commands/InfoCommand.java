package me.trae.core.client.commands;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import me.trae.core.utility.UtilTime;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class InfoCommand extends Command {

    public InfoCommand(final Main instance) {
        super(instance, "information", new String[]{"info", "statistics", "stats"}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            giveInformationMsg(player, player);
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            giveInformationMsg(player, target);
        }
    }

    @Override
    public void help(final Player player) {

    }

    private void giveInformationMsg(final Player player, final Player target) {
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        final Client targetC = getInstance().getClientUtilities().getOnlineClient(target.getUniqueId());
        if (targetC == null) {
            return;
        }
        final Gamer gamer = getInstance().getGamerUtilities().getGamer(player.getUniqueId());
        if (gamer == null) {
            return;
        }
        final Gamer targetG = getInstance().getGamerUtilities().getGamer(target.getUniqueId());
        if (targetG == null) {
            return;
        }
        UtilMessage.message(player, "Statistics", "Viewing " + (player != target ? ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "'s " : "your ") + ChatColor.GRAY + "Statistics:");
        UtilMessage.message(player, ChatColor.DARK_GREEN + "Time Played (Total): " + ChatColor.WHITE + UtilTime.getTime(System.currentTimeMillis() - targetC.getFirstJoined(), UtilTime.TimeUnit.BEST, 1));
        UtilMessage.message(player, ChatColor.DARK_GREEN + "Time Played (Today): " + ChatColor.WHITE + UtilTime.getTime(System.currentTimeMillis() - targetC.getLastJoined(), UtilTime.TimeUnit.BEST, 1));
        if (targetC.getLastOnline() != 0) {
            UtilMessage.message(player, ChatColor.DARK_GREEN + "Last Played: " + ChatColor.WHITE + UtilTime.getTime(System.currentTimeMillis() - targetC.getLastOnline(), UtilTime.TimeUnit.BEST, 1));
        }
        UtilMessage.message(player, ChatColor.DARK_GREEN + "Joined Amount: " + ChatColor.WHITE + targetC.getJoinedAmount());
        UtilMessage.message(player, ChatColor.DARK_GREEN + "Blocks Broken: " + ChatColor.WHITE + targetG.getBlocksBroken());
        UtilMessage.message(player, ChatColor.DARK_GREEN + "Blocks Placed: " + ChatColor.WHITE + targetG.getBlocksPlaced());
        UtilMessage.message(player, ChatColor.DARK_GREEN + "KDR: " + ChatColor.WHITE + targetG.getKills() + ChatColor.GRAY + ", " + ChatColor.DARK_GREEN + "Deaths: " + ChatColor.WHITE + targetG.getDeaths() + ChatColor.GRAY + ", " + ChatColor.DARK_GREEN + "Ratio: " + ChatColor.WHITE + targetG.getRatio());
    }
}