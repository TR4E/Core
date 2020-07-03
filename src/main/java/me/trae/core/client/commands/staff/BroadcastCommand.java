package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BroadcastCommand extends Command {

    public BroadcastCommand(final Main instance) {
        super(instance, "broadcast", new String[]{"bc"}, Rank.OWNER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
            return;
        }
        UtilMessage.broadcast(ChatColor.translateAlternateColorCodes('&', UtilFormat.getFinalArg(args, 0)));
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Broadcast", "Usage: " + ChatColor.AQUA + "/broadcast <message>");
    }
}