package me.trae.core.utility;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class UtilItem {

    public static ItemStack updateNames(final ItemStack item) {
        if (item.hasItemMeta()) {
            return item;
        }
        final ItemMeta meta = item.getItemMeta();
        item.setItemMeta(meta);
        return item;
    }
}