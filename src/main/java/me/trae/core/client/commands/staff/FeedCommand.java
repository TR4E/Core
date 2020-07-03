package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FeedCommand extends Command {

    public FeedCommand(final Main instance) {
        super(instance, "feed", new String[]{}, Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            player.setFoodLevel(20);
            UtilMessage.message(player, "Feed", "You have been fed.");
            getInstance().getClientUtilities().messageAdmins("Feed", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " fed themself.", new UUID[]{player.getUniqueId()});
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            if (target == player) {
                player.setFoodLevel(20);
                UtilMessage.message(player, "Feed", "You have been fed.");
                getInstance().getClientUtilities().messageAdmins("Feed", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " fed themself.", new UUID[]{player.getUniqueId()});
                return;
            }
            target.setHealth(target.getMaxHealth());
            target.setFoodLevel(20);
            UtilMessage.message(player, "Feed", "You fed " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
            UtilMessage.message(target, "Feed", "You have been fed by " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".");
            getInstance().getClientUtilities().messageAdmins("Feed", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " fed " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId(), target.getUniqueId()});
        }
    }

    @Override
    public void help(final Player player) {

    }
}