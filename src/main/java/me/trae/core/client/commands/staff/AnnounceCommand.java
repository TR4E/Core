package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class AnnounceCommand extends Command {

    public AnnounceCommand(final Main instance) {
        super(instance, "announce", new String[]{"say", "me", "shout"}, Rank.HEADMOD);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
            return;
        }
        UtilPlayer.sound(Sound.NOTE_PLING);
        UtilMessage.broadcast(ChatColor.AQUA.toString() + ChatColor.BOLD + player.getName() + "> " + ChatColor.WHITE.toString() + ChatColor.BOLD + UtilFormat.getFinalArg(args, 0));
        getInstance().getTitleManager().sendBroadcast(ChatColor.AQUA.toString() + ChatColor.BOLD + "Announcement", ChatColor.WHITE + "Look in Chat", 2);
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Announce", "Usage: " + ChatColor.AQUA + "/announce <message>");
    }
}