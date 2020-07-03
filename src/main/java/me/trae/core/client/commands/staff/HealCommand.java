package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HealCommand extends Command {

    public HealCommand(final Main instance) {
        super(instance, "heal", new String[]{}, Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            UtilMessage.message(player, "Heal", "You have been healed.");
            getInstance().getClientUtilities().messageAdmins("Heal", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " healed themself.", new UUID[]{player.getUniqueId()});
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            if (target == player) {
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
                UtilMessage.message(player, "Heal", "You have been healed.");
                getInstance().getClientUtilities().messageAdmins("Heal", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " healed themself.", new UUID[]{player.getUniqueId()});
                return;
            }
            target.setHealth(target.getMaxHealth());
            target.setFoodLevel(20);
            UtilMessage.message(player, "Heal", "You healed " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
            UtilMessage.message(target, "Heal", "You have been healed by " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".");
            getInstance().getClientUtilities().messageAdmins("Heal", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " healed " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId(), target.getUniqueId()});
        }
    }

    @Override
    public void help(final Player player) {

    }
}