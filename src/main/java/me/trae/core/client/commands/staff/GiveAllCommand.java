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

public class GiveAllCommand extends Command {

    public GiveAllCommand(final Main instance) {
        super(instance, "giveall", new String[]{}, Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
            return;
        }
        Material material = Material.matchMaterial(args[0]);
        if (material == null) {
            material = Bukkit.getUnsafe().getMaterialFromInternalName(args[0]);
        }
        if (material == Material.AIR || material == Material.WATER || material == Material.STATIONARY_WATER || material == Material.LAVA || material == Material.STATIONARY_LAVA || material == Material.PORTAL || material == Material.ENDER_PORTAL) {
            UtilMessage.message(player, "Give", "There is no Item called " + ChatColor.YELLOW + args[1] + ChatColor.GRAY + ".");
            return;
        }
        int amount = 1;
        short data = 0;
        if (args.length >= 2) {
            if (!(UtilFormat.isNumeric(args[1]))) {
                UtilMessage.message(player, "Give", "You did not input a valid Integer.");
                return;
            }
            amount = Integer.parseInt(args[1]);
            if (args.length >= 3) {
                if (!(UtilFormat.isNumeric(args[2]))) {
                    UtilMessage.message(player, "Give", "You did not input a valid Integer.");
                    return;
                }
                data = Short.parseShort(args[2]);
            }
        }
        final ItemStack item = new ItemStack(material, amount, data);
        Bukkit.getOnlinePlayers().forEach(o -> UtilItem.insert(o, item));
        UtilPlayer.sound(Sound.ITEM_PICKUP);
        UtilMessage.broadcast("Give", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " gave everyone " + ChatColor.GREEN + amount + "x" + ChatColor.GRAY + " of " + ChatColor.GREEN + UtilFormat.cleanString(item.getType().name()) + ChatColor.GRAY + ".");
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Give", "Usage: " + ChatColor.AQUA + "/giveall <item> <amount> <data>");
    }
}