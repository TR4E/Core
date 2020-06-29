package me.trae.core.client.commands;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SupportCommand extends Command {

    public SupportCommand(final Main instance) {
        super(instance, "support", new String[]{"adminchat", "a", "helpop"}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
            return;
        }
        if (!(getInstance().getClientUtilities().isStaffOnline())) {
            UtilMessage.message(player, "Support", "There are currently no staff online to receive this message.");
            return;
        }
        UtilMessage.message(player, ChatColor.GOLD.toString() + ChatColor.BOLD + player.getName() + "> " + ChatColor.YELLOW.toString() + ChatColor.BOLD + UtilFormat.getFinalArg(args, 0));
        getInstance().getClientUtilities().messageStaff(ChatColor.GOLD.toString() + ChatColor.BOLD + player.getName() + "> " + ChatColor.YELLOW.toString() + ChatColor.BOLD + UtilFormat.getFinalArg(args, 0), Rank.HELPER, new UUID[]{player.getUniqueId()});
        getInstance().getClientUtilities().soundStaff(Sound.NOTE_PLING, Rank.HELPER, new UUID[]{player.getUniqueId()});
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Support", "Usage: " + ChatColor.AQUA + "/a <message>");
    }
}