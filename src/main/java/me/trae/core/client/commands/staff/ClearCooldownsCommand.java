package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClearCooldownsCommand extends Command {

    public ClearCooldownsCommand(final Main instance) {
        super(instance, "clearcooldown", new String[]{"clearcd", "clearrecharge"}, Rank.OWNER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            if (!(getInstance().getRechargeManager().removeCooldowns(target))) {
                UtilMessage.message(player, "Cooldowns", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " does not have any active cooldowns.");
                return;
            }
            if (player == target) {
                UtilMessage.message(player, "Cooldowns", "You removed all active cooldowns from yourself.");
                return;
            }
            UtilMessage.message(player, "Cooldowns", "You removed all active cooldowns from " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
            UtilMessage.message(target, "Cooldowns", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " removed all your active cooldowns.");
            getInstance().getClientUtilities().messageAdmins("Cooldowns", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " removed all active cooldowns from " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId(), target.getUniqueId()});
        }
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Cooldowns", "Usage: " + ChatColor.AQUA + "/clearcd <player>");
    }
}