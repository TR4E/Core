package me.trae.core.client.commands;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StaffChatCommand extends Command {

    public StaffChatCommand(final Main instance) {
        super(instance, "staffchat", new String[]{"sc"}, Rank.HELPER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (args == null || args.length == 0) {
            client.setStaffChat(!(client.isStaffChat()));
            UtilMessage.message(player, "Client", "Staff Chat: " + (client.isStaffChat() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
            return;
        }
        getInstance().getClientUtilities().messageStaff(ChatColor.RED.toString() + ChatColor.BOLD + player.getName() + "> " + ChatColor.WHITE.toString() + ChatColor.BOLD + UtilFormat.getFinalArg(args, 0), Rank.HELPER, null);
    }

    @Override
    public void help(final Player player) {

    }
}