package me.trae.core.client.commands.help;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StoreCommand extends Command {

    public StoreCommand(final Main instance) {
        super(instance, "store", new String[]{}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
        }
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Store", "Link: " + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + getInstance().getRepository().getServerWebsite().replace("://", "://store."));
    }
}