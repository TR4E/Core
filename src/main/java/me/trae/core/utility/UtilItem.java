package me.trae.core.utility;

import me.trae.core.database.Repository;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class UtilItem {

    public static ItemStack updateNames(final ItemStack item) {
        final ItemMeta meta = item.getItemMeta();
        if (!(Repository.isGameEnchantments())) {
            if (meta.hasEnchants()) {
                meta.getEnchants().keySet().forEach(meta::removeEnchant);
                item.setItemMeta(meta);
            }
        }
        if (meta.hasDisplayName() || (item.hasItemMeta() && meta.hasLore() && !(meta.getLore().isEmpty()))) {
            return item;
        }
        final List<String> lore = new ArrayList<>();
        if (item.getType() == Material.LAPIS_BLOCK) {
            meta.setDisplayName(ChatColor.YELLOW + "Water Block");
            lore.add(ChatColor.WHITE + "This Block will turn into Water when placed.");
        } else if (item.getType() == Material.TNT) {
            meta.setDisplayName(ChatColor.YELLOW + "TNT");
        } else if (item.getType() == Material.POTATO_ITEM) {
            meta.setDisplayName(ChatColor.YELLOW + "Potato");
        } else if (item.getType() == Material.CARROT_ITEM) {
            meta.setDisplayName(ChatColor.YELLOW + "Carrot");
        } else if (item.getType() == Material.INK_SACK) {
            if (item.getDurability() == 1) {
                meta.setDisplayName(ChatColor.YELLOW + "Rose Red");
            } else if (item.getDurability() == 2) {
                meta.setDisplayName(ChatColor.YELLOW + "Cactus Green");
            } else if (item.getDurability() == 3) {
                meta.setDisplayName(ChatColor.YELLOW + "Cocoa Beans");
            } else if (item.getDurability() == 4) {
                meta.setDisplayName(ChatColor.YELLOW + "Lapis Lazuli");
            } else if (item.getDurability() == 5) {
                meta.setDisplayName(ChatColor.YELLOW + "Purple Dye");
            } else if (item.getDurability() == 6) {
                meta.setDisplayName(ChatColor.YELLOW + "Cyan Dye");
            } else if (item.getDurability() == 7) {
                meta.setDisplayName(ChatColor.YELLOW + "Light Gray Dye");
            } else if (item.getDurability() == 8) {
                meta.setDisplayName(ChatColor.YELLOW + "Gray Dye");
            } else if (item.getDurability() == 9) {
                meta.setDisplayName(ChatColor.YELLOW + "Pink Dye");
            } else if (item.getDurability() == 10) {
                meta.setDisplayName(ChatColor.YELLOW + "Lime Dye");
            } else if (item.getDurability() == 11) {
                meta.setDisplayName(ChatColor.YELLOW + "Dandelion Yellow");
            } else if (item.getDurability() == 12) {
                meta.setDisplayName(ChatColor.YELLOW + "Light Blue Dye");
            } else if (item.getDurability() == 13) {
                meta.setDisplayName(ChatColor.YELLOW + "Magenta Dye");
            } else if (item.getDurability() == 14) {
                meta.setDisplayName(ChatColor.YELLOW + "Orange Dye");
            } else if (item.getDurability() == 15) {
                meta.setDisplayName(ChatColor.YELLOW + "Bone Meal");
            }
        } else {
            meta.setDisplayName(ChatColor.YELLOW + UtilFormat.cleanString(item.getType().name()));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}