package me.trae.core.database.commands;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class ReloadCommand extends Command {

    public ReloadCommand(final Main instance) {
        super(instance, "creload", new String[]{}, Rank.OWNER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            getInstance().getRepository().reload(getInstance(), getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating());
            UtilMessage.message(player, "Database", "Reloaded " + (getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating() ? "All Configurations" : "Main Configuration") + ".");
        }
    }

    @Override
    public void help(final Player player) {

    }
}