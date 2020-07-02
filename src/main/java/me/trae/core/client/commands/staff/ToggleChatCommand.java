package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ToggleChatCommand extends Command {

    public ToggleChatCommand(final Main instance) {
        super(instance, "togglechat", new String[]{"tc"}, Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            getInstance().setChat(!(getInstance().isChat()));
            UtilMessage.broadcast("Server", (getInstance().isChat() ? ChatColor.RED + "The Chat has been silenced!" : ChatColor.GREEN + "The Chat is no longer silenced!"));
        }
    }

    @Override
    public void help(final Player player) {

    }
}