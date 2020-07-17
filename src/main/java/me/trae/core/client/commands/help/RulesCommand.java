package me.trae.core.client.commands.help;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RulesCommand extends Command {

    public RulesCommand(final Main instance) {
        super(instance, "rules", new String[]{}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
        }
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Rules", "List of Server Rules:");
        getInstance().getRepository().getRules().forEach(msg -> UtilMessage.message(player, ChatColor.translateAlternateColorCodes('&', msg)));
    }
}