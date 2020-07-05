package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class PlayerCountCommand extends Command {

    public PlayerCountCommand(final Main instance) {
        super(instance, "playercount", new String[]{}, Rank.MOD);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            final int count = getInstance().getClientUtilities().getClients().stream().filter(c -> c.getLastOnline() > (System.currentTimeMillis() - 86400000)).collect(Collectors.toSet()).size();
            UtilMessage.message(player, "Player Count", "There was " + ChatColor.YELLOW + count + ChatColor.GRAY + " players that joined in the past " + ChatColor.GREEN + "24.0 Hours" + ChatColor.GRAY + ".");
        }
    }

    @Override
    public void help(final Player player) {

    }
}