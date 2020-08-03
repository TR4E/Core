package me.trae.core.database.commands;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReloadCommand extends Command {

    public ReloadCommand(final Main instance) {
        super(instance, "creload", new String[]{}, Rank.OWNER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            UtilMessage.message(player, "Database", "Reloaded " + (getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating() ? "All Configurations" : "Main Configuration") + ".");
            getInstance().getClientUtilities().messageStaff("Database", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " reloaded " + (getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating() ? "All Configurations" : "Main Configuration") + ".", Rank.OWNER, new UUID[]{player.getUniqueId()});
            if (getInstance().getRepository().isServerConsoleDebugOutput()) {
                UtilMessage.log("Database", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " reloaded " + (getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating() ? "All Configurations" : "Main Configuration") + ".");
            }
            getInstance().getRepository().reload(getInstance(), player, getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating());
        }
    }

    @Override
    public void help(final Player player) {

    }
}