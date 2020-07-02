package me.trae.core.client.commands;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReportCommand extends Command {

    public ReportCommand(final Main instance) {
        super(instance, "report", new String[]{}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
            return;
        }
        final Player target = UtilPlayer.searchPlayer(player, args[0], true);
        if (target == null) {
            return;
        }
        if (target == player) {
            UtilMessage.message(player, "Report", "You cannot report yourself.");
            return;
        }
        if (!(player.isOp())) {
            if (getInstance().getClientUtilities().getOnlineClient(target.getUniqueId()).hasRank(Rank.ADMIN, false)) {
                UtilMessage.message(player, "Report", "You cannot report this Player!");
                return;
            }
            if (getInstance().getClientUtilities().getOnlineClient(target.getUniqueId()).isVanished()) {
                UtilMessage.message(player, "Player Search", ChatColor.YELLOW + "0" + ChatColor.GRAY + " matches found [" + ChatColor.YELLOW + args[0] + ChatColor.GRAY + "]");
                return;
            }
        }
        if (args.length == 1) {
            help(player);
            return;
        }
        if (!(getInstance().getClientUtilities().isStaffOnline(true))) {
            UtilMessage.message(player, "Report", "There are currently no staff online to receive this message.");
            return;
        }
        UtilMessage.message(player, "Report", "You reported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " for " + ChatColor.GREEN + UtilFormat.getFinalArg(args, 1) + ChatColor.GRAY + ".");
        getInstance().getClientUtilities().messageStaff("Report", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " reported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " for " + ChatColor.GREEN + UtilFormat.getFinalArg(args, 1) + ChatColor.GRAY + ".", Rank.HELPER, new UUID[]{player.getUniqueId()});
        getInstance().getClientUtilities().soundStaff(Sound.NOTE_PLING, Rank.HELPER, new UUID[]{player.getUniqueId()});
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Report", "Usage: " + ChatColor.AQUA + "/report <player> <reason>");
    }
}