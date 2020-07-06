package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TimeCommand extends Command {

    public TimeCommand(final Main instance) {
        super(instance, "time", new String[]{"settime"}, Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
            return;
        }
        if (args.length == 1) {
            long time = 0;
            if (args[0].equalsIgnoreCase("day")) {
                time = 1000;
            } else if (args[0].equalsIgnoreCase("night")) {
                time = 13000;
            } else if (args[0].equalsIgnoreCase("sunrise")) {
                time = 23000;
            } else if (args[0].equalsIgnoreCase("sunset")) {
                time = 12000;
            } else if (args[0].equalsIgnoreCase("noon")) {
                time = 6000;
            } else {
                help(player);
                return;
            }
            player.getWorld().setTime(time);
            UtilMessage.message(player, "Time", "You changed the time to " + ChatColor.GREEN + UtilFormat.cleanString(args[0]) + ChatColor.GRAY + " in " + ChatColor.YELLOW + player.getWorld().getName() + ChatColor.GRAY + ".");
            if (getInstance().getEffectManager().isVanished(player)) {
                getInstance().getClientUtilities().messageStaff("Time", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " changed the time to " + ChatColor.GREEN + UtilFormat.cleanString(args[0]) + ChatColor.GRAY + ".", Rank.ADMIN, new UUID[]{player.getUniqueId()});
            } else {
                UtilMessage.broadcast("Time", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " changed the time to " + ChatColor.GREEN + UtilFormat.cleanString(args[0]) + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
            }
        }
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Time", "Usage: " + ChatColor.AQUA + "/time <type>");
        UtilMessage.message(player, "Time", "Types: [" + ChatColor.YELLOW + "Day" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "Night" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "Sunrise" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "Sunset" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "Noon" + ChatColor.GRAY + "]");
    }
}