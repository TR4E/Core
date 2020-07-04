package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MoreCommand extends Command {

    public MoreCommand(final Main instance) {
        super(instance, "more", new String[]{"fullstack"}, Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            if (player.getInventory().getItemInHand().getType() != Material.AIR) {
                if (player.getInventory().getItemInHand().getAmount() >= 64) {
                    UtilMessage.message(player, "Inventory", "You already have a full stack of " + ChatColor.GREEN + UtilFormat.cleanString(player.getInventory().getItemInHand().getType().name()) + ChatColor.GRAY + ".");
                    return;
                }
                player.getInventory().getItemInHand().setAmount(64);
                UtilMessage.message(player, "Inventory", "You now have a full stack of " + ChatColor.GREEN + UtilFormat.cleanString(player.getInventory().getItemInHand().getType().name()) + ChatColor.GRAY + ".");
            }
        }
    }

    @Override
    public void help(final Player player) {

    }
}