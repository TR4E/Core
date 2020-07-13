package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand extends Command {

    public GiveCommand(final Main instance) {
        super(instance, "give", new String[]{}, Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
            return;
        }
        final Player target = UtilPlayer.searchPlayer(player, args[0], true);
        if (target == null) {
            return;
        }
        Material material = Material.matchMaterial(args[1]);
        if (material == null) {
            material = Bukkit.getUnsafe().getMaterialFromInternalName(args[1]);
        }
        if (material == Material.AIR || material == Material.WATER || material == Material.STATIONARY_WATER || material == Material.LAVA || material == Material.STATIONARY_LAVA || material == Material.PORTAL || material == Material.ENDER_PORTAL) {
            UtilMessage.message(player, "Give", "There is no Item called " + ChatColor.YELLOW + args[1] + ChatColor.GRAY + ".");
            return;
        }
        int amount = 1;
        short data = 0;
        if (args.length >= 3) {
            if (!(UtilFormat.isNumeric(args[2]))) {
                UtilMessage.message(player, "Give", "You did not input a valid Integer.");
                return;
            }
            amount = Integer.parseInt(args[2]);
            if (args.length >= 4) {
                if (!(UtilFormat.isNumeric(args[3]))) {
                    UtilMessage.message(player, "Give", "You did not input a valid Integer.");
                    return;
                }
                data = Short.parseShort(args[3]);
            }
        }
        final ItemStack item = new ItemStack(material, amount, data);
        UtilItem.insert(player, item);
        UtilPlayer.sound(player, Sound.ITEM_PICKUP);
        UtilMessage.broadcast("Give", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " gave " + ChatColor.YELLOW + target.getName() + " " + ChatColor.GREEN + amount + "x" + ChatColor.GRAY + " of " + ChatColor.GREEN + UtilFormat.cleanString(item.getType().name()) + ChatColor.GRAY + ".");
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Give", "Usage: " + ChatColor.AQUA + "/give <player> <item> <amount> <data>");
    }
}