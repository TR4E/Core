package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FlyCommand extends Command {

    public FlyCommand(final Main instance) {
        super(instance, "flymode", new String[]{"fly"}, Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (args == null || args.length == 0) {
            player.setAllowFlight(!(player.getAllowFlight()));
            UtilMessage.message(player, "Fly", "Fly Mode: " + (player.getAllowFlight() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
            getInstance().getClientUtilities().messageStaff("Fly", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + (player.getAllowFlight() ? " is now Flying." : " is no longer Flying."), Rank.ADMIN, new UUID[]{player.getUniqueId()});
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            if (target == player) {
                player.setAllowFlight(!(player.getAllowFlight()));
                UtilMessage.message(player, "Fly", "Fly Mode: " + (player.getAllowFlight() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
                getInstance().getClientUtilities().messageStaff("Fly", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + (player.getAllowFlight() ? " is now Flying." : " is no longer Flying."), Rank.ADMIN, new UUID[]{player.getUniqueId()});
                return;
            }
            final Client targetC = getInstance().getClientUtilities().getOnlineClient(target.getUniqueId());
            if (targetC == null) {
                return;
            }
            if (!(player.isOp())) {
                if (targetC.getRank().ordinal() >= client.getRank().ordinal()) {
                    UtilMessage.message(player, "Fly", "You cannot toggle Fly mode for this Player!");
                    return;
                }
            }
            target.setAllowFlight(!(target.getAllowFlight()));
            UtilMessage.message(target, "Fly", "Fly Mode: " + (target.getAllowFlight() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
            getInstance().getClientUtilities().messageStaff("Fly", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + (target.getAllowFlight() ? " is now Flying" : " is no longer Flying") + " by " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".", Rank.ADMIN, new UUID[]{target.getUniqueId()});
        }
    }

    @Override
    public void help(final Player player) {

    }
}