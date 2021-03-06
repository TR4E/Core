package me.trae.core.client.commands.help;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StaffHelpCommand extends Command {

    public StaffHelpCommand(final Main instance) {
        super(instance, "commands", new String[]{"cmds", "staffhelp", "staffcmds", "staffcommands"}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
        }
    }

    @Override
    public void help(final Player player) {
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(client.hasRank(Rank.HELPER, false))) {
            player.performCommand("/help");
        } else {
            UtilMessage.message(player, "Commands", "Your available commands:");
            if (client.hasRank(Rank.HELPER, false)) {
                UtilMessage.message(player, ChatColor.DARK_GREEN + "/staffchat" + ChatColor.GRAY + " - " + "Toggle Staff Chat.");
            }
            if (client.hasRank(Rank.MOD, false)) {
                UtilMessage.message(player, ChatColor.AQUA + "/playercount" + ChatColor.GRAY + " - " + "Shows Player Count from the past day.");
                UtilMessage.message(player, ChatColor.AQUA + "/client search" + ChatColor.GRAY + " - " + "Search a Client.");
            }
            if (client.hasRank(Rank.HEADMOD, false)) {
                UtilMessage.message(player, ChatColor.AQUA + "/announce" + ChatColor.GRAY + " - " + "Send an Announcement.");
                UtilMessage.message(player, ChatColor.AQUA + "/checkalts" + ChatColor.GRAY + " - " + "Check a Player's Alts.");
                UtilMessage.message(player, ChatColor.AQUA + "/observer" + ChatColor.GRAY + " - " + "Toggle Observer Mode.");
                UtilMessage.message(player, ChatColor.AQUA + "/fly" + ChatColor.GRAY + " - " + "Toggle Fly Mode.");
                UtilMessage.message(player, ChatColor.AQUA + "/tp" + ChatColor.GRAY + " - " + "Teleport to a Player.");
            }
            if (client.hasRank(Rank.ADMIN, false)) {
                UtilMessage.message(player, ChatColor.RED + "/clearchat" + ChatColor.GRAY + " - " + "Clears the Chat.");
                UtilMessage.message(player, ChatColor.RED + "/client admin" + ChatColor.GRAY + " - " + "Toggle Client Admin.");
                UtilMessage.message(player, ChatColor.RED + "/feed" + ChatColor.GRAY + " - " + "Feed a Player.");
                UtilMessage.message(player, ChatColor.RED + "/gamemode" + ChatColor.GRAY + " - " + "Update your Gamemode.");
                UtilMessage.message(player, ChatColor.RED + "/god" + ChatColor.GRAY + " - " + "Toggle God Mode.");
                UtilMessage.message(player, ChatColor.RED + "/heal" + ChatColor.GRAY + " - " + "Heal a Player.");
                UtilMessage.message(player, ChatColor.RED + "/more" + ChatColor.GRAY + " - " + "Turns your hand item into a full stack.");
                UtilMessage.message(player, ChatColor.RED + "/openinv" + ChatColor.GRAY + " - " + "Open a Player's Inventory.");
                UtilMessage.message(player, ChatColor.RED + "/skull" + ChatColor.GRAY + " - " + "Gives a Player Head.");
                UtilMessage.message(player, ChatColor.RED + "/time" + ChatColor.GRAY + " - " + "Change the Time.");
                UtilMessage.message(player, ChatColor.RED + "/togglechat" + ChatColor.GRAY + " - " + "Toggle Server Chat.");
                UtilMessage.message(player, ChatColor.RED + "/vanish" + ChatColor.GRAY + " - " + "Toggle Vanish Mode.");
                UtilMessage.message(player, ChatColor.RED + "/tp" + ChatColor.GRAY + " - " + "Teleport to a Player.");
                UtilMessage.message(player, ChatColor.RED + "/tpo" + ChatColor.GRAY + " - " + "Teleport to a Player (Silent).");
                UtilMessage.message(player, ChatColor.RED + "/tphere" + ChatColor.GRAY + " - " + "Teleport a Player to You.");
                UtilMessage.message(player, ChatColor.RED + "/tpall" + ChatColor.GRAY + " - " + "Teleport everyone to You.");
            }
            if (client.hasRank(Rank.OWNER, false)) {
                UtilMessage.message(player, ChatColor.DARK_RED + "/broadcast" + ChatColor.GRAY + " - " + "Broadcast Raw Text.");
                UtilMessage.message(player, ChatColor.DARK_RED + "/clearcd" + ChatColor.GRAY + " - " + "Clear a Player's Cooldowns.");
                UtilMessage.message(player, ChatColor.DARK_RED + "/setspawn" + ChatColor.GRAY + " - " + "Set World Spawn.");
                UtilMessage.message(player, ChatColor.DARK_RED + "/creload" + ChatColor.GRAY + " - " + "Reload Databases.");
                UtilMessage.message(player, ChatColor.DARK_RED + "/tppos" + ChatColor.GRAY + " - " + "Teleport to a Location.");
            }
        }
    }
}