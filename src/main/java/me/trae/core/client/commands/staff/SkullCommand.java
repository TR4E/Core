package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SkullCommand extends Command {

    public SkullCommand(final Main instance) {
        super(instance, "skull", new String[]{"head", "playerhead", "playerskull"}, Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            if (!(UtilPlayer.isInventoryEmpty(player))) {
                player.getWorld().dropItemNaturally(player.getLocation(), UtilItem.getSkull(player.getName()));
            } else {
                player.getInventory().addItem(UtilItem.getSkull(player.getName()));
                player.updateInventory();
            }
            UtilMessage.message(player, "Skull", "You received a Skull of your Player Skin.");
            return;
        }
        if (args.length == 1) {
            if (!(UtilPlayer.isInventoryEmpty(player))) {
                player.getWorld().dropItemNaturally(player.getLocation(), UtilItem.getSkull(args[0]));
            } else {
                player.getInventory().addItem(UtilItem.getSkull(args[0]));
                player.updateInventory();
            }
            UtilMessage.message(player, "Skull", "You received a Skull of " + ChatColor.GREEN + args[0] + ChatColor.GRAY + ".");
            return;
        }
        if (args.length == 2 || args.length == 3) {
            final Player target = UtilPlayer.searchPlayer(player, args[1], true);
            if (target == null) {
                return;
            }
            int amount = 1;
            if (args.length == 3) {
                amount = Integer.parseInt(args[2]);
            }
            final ItemStack skull = UtilItem.getSkull(args[0]);
            skull.setAmount(amount);
            if (!(UtilPlayer.isInventoryEmpty(target))) {
                target.getWorld().dropItemNaturally(target.getLocation(), skull);
            } else {
                target.getInventory().addItem(skull);
                target.updateInventory();
            }
            UtilMessage.message(player, "Skull", "You gave " + ChatColor.GREEN + amount + "x" + ChatColor.GRAY + " Skull of " + ChatColor.GREEN + args[0] + ChatColor.GRAY + " to " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
            UtilMessage.message(target, "Skull", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " gave you " + ChatColor.GREEN + amount + "x" + ChatColor.GRAY + " Skull of " + ChatColor.GREEN + args[0] + ChatColor.GRAY + ".");
            getInstance().getClientUtilities().messageStaff("Skull", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " gave " + ChatColor.GREEN + amount + "x" + ChatColor.GRAY + " of " + ChatColor.GREEN + args[0] + ChatColor.GRAY + " to " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".", Rank.OWNER, new UUID[]{player.getUniqueId(), target.getUniqueId()});
        }
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Skull", "Usage: " + ChatColor.AQUA + "/skull <player name>");
    }
}