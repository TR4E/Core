package me.trae.core.client.commands;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DyeCommand extends Command {

    public DyeCommand(final Main instance) {
        super(instance, "dye", new String[]{"color", "colour", "changecolor", "changecolour"}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
            return;
        }
        final ItemStack i = player.getInventory().getItemInHand();
        if (i == null) {
            UtilMessage.message(player, "Dye", "You cannot dye Air!");
            return;
        }
        final DyeColor dye = searchDye(player, args[0]);
        if (dye == null) {
            return;
        }
        if (i.getType() == Material.WOOL || i.getType() == Material.CARPET || i.getType() == Material.STAINED_CLAY) {
            i.setDurability(dye.getWoolData());
        } else if (i.getType().name().toLowerCase().startsWith("leather_")) {
            final LeatherArmorMeta m = (LeatherArmorMeta) i.getItemMeta();
            m.setColor(dye.getColor());
            i.setItemMeta(m);
        } else if (i.getType().name().toLowerCase().contains("glass") && !i.getType().name().toLowerCase().contains("bottle")) {
            i.setType(Material.STAINED_GLASS);
            i.setDurability(dye.getData());
        } else if (i.getType().name().toLowerCase().endsWith("_pane")) {
            i.setType(Material.STAINED_GLASS_PANE);
            i.setDurability(dye.getData());
        } else if (i.getType().name().toLowerCase().endsWith("_clay")) {
            i.setType(Material.HARD_CLAY);
            i.setDurability(dye.getData());
        } else if (i.getType() == Material.AIR) {
            UtilMessage.message(player, "Dye", "You cannot dye Air!");
            return;
        } else {
            UtilMessage.message(player, "Dye", "You cannot dye this item!");
            return;
        }
        UtilMessage.message(player, "Dye", "You dyed " + ChatColor.YELLOW + UtilFormat.cleanString(i.getType().name()) + ChatColor.GRAY + " to " + ChatColor.GREEN + UtilFormat.cleanString(dye.name()) + ChatColor.GRAY + ".");
    }

    @Override
    public void help(final Player player) {
        final List<DyeColor> color = Arrays.asList(DyeColor.values());
        final String colors = color.stream().map(c -> UtilFormat.cleanString(c.name())).collect(Collectors.joining(ChatColor.GRAY + ", " + ChatColor.YELLOW + ""));
        UtilMessage.message(player, "Dye", "You did not input a dye color!");
        UtilMessage.message(player, "Dye", "Available Colors: " + "[" + ChatColor.YELLOW + colors + ChatColor.GRAY + "].");
    }

    private DyeColor searchDye(final Player player, final String color) {
        if (Arrays.stream(DyeColor.values()).anyMatch(dyeColor -> dyeColor.name().toLowerCase().equals(color.toLowerCase()))) {
            return Arrays.stream(DyeColor.values()).filter(dyeColor -> dyeColor.name().replaceAll("_", "").toLowerCase().equals(color.toLowerCase())).findFirst().get();
        }
        final List<DyeColor> colors = new ArrayList<>();
        Arrays.stream(DyeColor.values()).filter(dyeColor -> dyeColor.name().toLowerCase().replaceAll("_", "").contains(color.toLowerCase())).collect(Collectors.toList()).forEach(cc -> colors.add(cc));
        if (colors.size() == 1) {
            return colors.get(0);
        } else if (!colors.isEmpty()) {
            UtilMessage.message(player, "Dye Search", ChatColor.YELLOW.toString() + colors.size() + ChatColor.GRAY + " Matches found [" + ChatColor.YELLOW + colors.stream().map(dyeColor -> UtilFormat.cleanString(dyeColor.name()).replaceAll(" ", "")).collect(Collectors.joining(ChatColor.GRAY + ", " + ChatColor.YELLOW)) + ChatColor.GRAY + "]");
            return null;
        }
        UtilMessage.message(player, "Dye Search", ChatColor.YELLOW + "0" + ChatColor.GRAY + " Matches found. [" + ChatColor.YELLOW + color + ChatColor.GRAY + "].");
        return null;
    }
}