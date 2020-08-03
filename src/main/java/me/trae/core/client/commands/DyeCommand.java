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
        if (!(isCorrectItem(i))) {
            UtilMessage.message(player, "Dye", "You cannot dye this item!");
            UtilMessage.message(player, "Dye", "Available Items: " + "[" + ChatColor.WHITE + "Wool" + ChatColor.GRAY + ", " + ChatColor.WHITE + "Glass" + ChatColor.GRAY + ", " + ChatColor.WHITE + "Clay" + ChatColor.GRAY + ", " + ChatColor.WHITE + "Leather Armour" + ChatColor.GRAY + "] ");
            return;
        }
        DyeColor dye = null;
        final String name = args[0];
        if (!(name.equalsIgnoreCase("Reset") || name.equalsIgnoreCase("Default"))) {
            dye = searchDye(player, name);
            if (dye == null) {
                return;
            }
        } else {
            if (isCorrectItem(i)) {
                if (i.getType().name().toLowerCase().startsWith("leather_")) {
                    player.getInventory().setItemInHand(getInstance().getItemManager().updateNames(new ItemStack(i.getType())));
                } else if (i.getType() == Material.STAINED_GLASS_PANE || i.getType() == Material.THIN_GLASS) {
                    player.getInventory().setItemInHand(getInstance().getItemManager().updateNames(new ItemStack(Material.THIN_GLASS)));
                } else if (i.getType().name().toLowerCase().contains("glass") && !(i.getType().name().toLowerCase().contains("bottle"))) {
                    i.setType(Material.GLASS);
                } else if (i.getType().name().toLowerCase().endsWith("_clay")) {
                    i.setType(Material.HARD_CLAY);
                } else {
                    i.setType(i.getType());
                    i.setDurability((short) 0);
                    i.getData().setData((byte) 0);
                }
                UtilMessage.message(player, "Dye", "You dyed " + ChatColor.YELLOW + UtilFormat.cleanString(i.getType().name()) + ChatColor.GRAY + " to " + ChatColor.GREEN + "Reset" + ChatColor.GRAY + ".");
            }
            return;
        }
        if (i.getType() == Material.WOOL || i.getType() == Material.CARPET || i.getType() == Material.STAINED_CLAY) {
            i.setDurability(dye.getWoolData());
        } else if (i.getType().name().toLowerCase().startsWith("leather_")) {
            final LeatherArmorMeta m = (LeatherArmorMeta) i.getItemMeta();
            m.setColor(dye.getColor());
            i.setItemMeta(m);
        } else if (i.getType() == Material.STAINED_GLASS_PANE || i.getType() == Material.THIN_GLASS) {
            i.setType(Material.STAINED_GLASS_PANE);
            i.setDurability(dye.getData());
        } else if (i.getType().name().toLowerCase().contains("glass") && !(i.getType().name().toLowerCase().contains("bottle"))) {
            i.setType(Material.STAINED_GLASS);
            i.setDurability(dye.getData());
        } else if (i.getType().name().toLowerCase().endsWith("_clay")) {
            i.setType(Material.STAINED_CLAY);
            i.setDurability(dye.getData());
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
        final List<String> colors = Arrays.stream(DyeColor.values()).filter(dyeColor -> dyeColor.name().toLowerCase().replaceAll("_", "").contains(color.toLowerCase())).map(Enum::name).collect(Collectors.toList());
        if (colors.size() == 1) {
            return DyeColor.valueOf(colors.get(0));
        } else if (!colors.isEmpty()) {
            UtilMessage.message(player, "Dye Search", ChatColor.YELLOW.toString() + colors.size() + ChatColor.GRAY + " Matches found [" + ChatColor.WHITE + "Reset" + ChatColor.GRAY + ", " + ChatColor.YELLOW + colors.stream().map(dyeColor -> ChatColor.YELLOW + UtilFormat.cleanString(dyeColor).replaceAll(" ", "")).collect(Collectors.joining(ChatColor.GRAY + ", " + ChatColor.YELLOW)) + ChatColor.GRAY + "].");
            return null;
        }
        UtilMessage.message(player, "Dye Search", ChatColor.YELLOW + "0" + ChatColor.GRAY + " Matches found. [" + ChatColor.YELLOW + color + ChatColor.GRAY + "].");
        return null;
    }

    private boolean isCorrectItem(final ItemStack item) {
        return (item.getType() == Material.WOOL || item.getType() == Material.CARPET || item.getType() == Material.STAINED_CLAY || item.getType().name().toLowerCase().startsWith("leather_") || (item.getType().name().toLowerCase().contains("glass") && !(item.getType().name().toLowerCase().contains("bottle"))) || item.getType().name().toLowerCase().endsWith("_pane") || item.getType().name().toLowerCase().endsWith("_clay"));
    }
}