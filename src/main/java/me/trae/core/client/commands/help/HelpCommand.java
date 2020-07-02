package me.trae.core.client.commands.help;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpCommand extends Command {

    public HelpCommand(final Main instance) {
        super(instance, "help", new String[]{}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        help(player);
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, ChatColor.YELLOW + "----- Help -----");
        UtilMessage.message(player, ChatColor.AQUA + "/report <player> <reason>" + ChatColor.GRAY + " - " + "Report a Player.");
        UtilMessage.message(player, ChatColor.AQUA + "/ignore <player>" + ChatColor.GRAY + " - " + "Ignore a Player.");
        UtilMessage.message(player, ChatColor.AQUA + "/a <message>" + ChatColor.GRAY + " - " + "Request Staff Assistance.");
        UtilMessage.message(player, ChatColor.AQUA + "/list" + ChatColor.GRAY + " - " + "List Online Players.");
        UtilMessage.message(player, ChatColor.AQUA + "/clearinv" + ChatColor.GRAY + " - " + "Clear Inventory.");
        UtilMessage.message(player, ChatColor.AQUA + "/spawn" + ChatColor.GRAY + " - " + "Teleport to Spawn.");
        UtilMessage.message(player, ChatColor.AQUA + "/kdr <player>" + ChatColor.GRAY + " - " + "View Kill Death Ratio.");
    }
}