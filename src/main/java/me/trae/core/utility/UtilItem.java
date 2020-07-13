package me.trae.core.utility;

import me.trae.core.database.Repository;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

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
            lore.add(ChatColor.GRAY + "Transformation: " + ChatColor.GREEN + "Water Source");
        } else if (item.getType() == Material.ENDER_PEARL) {
            meta.setDisplayName(ChatColor.YELLOW + "Ethereal Pearl");
            lore.add(ChatColor.GRAY + "Left Click: " + ChatColor.GREEN + "Ride Pearl");
            lore.add(ChatColor.GRAY + "Right Click: " + ChatColor.GREEN + "Remove Negative Effects");
        } else if (item.getType() == Material.WEB) {
            meta.setDisplayName(ChatColor.YELLOW + "Throwing Web");
            lore.add(ChatColor.GRAY + "Left Click: " + ChatColor.GREEN + "Throw Web");
        } else if (item.getType().name().contains("SPONGE")) {
            meta.setDisplayName(ChatColor.YELLOW + (item.getData().getData() == (byte) 0 ? "" : "Wet ") + "Sponge");
            lore.add(ChatColor.GRAY + "Stand and right click on the " + ChatColor.YELLOW + "Sponge" + ChatColor.GRAY + " to be launched.");
        } else if (item.getType() == Material.TNT) {
            meta.setDisplayName(ChatColor.YELLOW + "TNT");
        } else if (item.getType() == Material.COMPASS) {
            meta.setDisplayName(ChatColor.YELLOW + "Tracking Device");
            lore.add(ChatColor.GRAY + "Type '" + ChatColor.AQUA + "/track <player>" + ChatColor.GRAY + "' to track a player.");
        } else if (item.getType() == Material.POTATO_ITEM) {
            meta.setDisplayName(ChatColor.YELLOW + "Potato");
        } else if (item.getType() == Material.CARROT_ITEM) {
            meta.setDisplayName(ChatColor.YELLOW + "Carrot");
        } else if (item.getType() == Material.SKULL_ITEM) {
            if (item.getDurability() == 3) {
                final SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                meta.setDisplayName(ChatColor.YELLOW + "Skull of " + skullMeta.getOwner());
            }
        } else if (item.getType() == Material.INK_SACK) {
            if (item.getDurability() == 0) {
                meta.setDisplayName(ChatColor.YELLOW + "Ink Sack");
            } else if (item.getDurability() == 1) {
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

    public static boolean contains(final Player player, final Material item, final byte data, int required) {
        for (final int i : player.getInventory().all(item).keySet()) {
            if (required <= 0) {
                return true;
            }
            final ItemStack stack = player.getInventory().getItem(i);
            if (stack != null && stack.getAmount() > 0 && (stack.getData() == null || stack.getData().getData() == data)) {
                required -= stack.getAmount();
            }
        }
        return (required <= 0);
    }

    public static boolean remove(final Player player, final Material item, final byte data, int toRemove) {
        if (player.getGameMode() == GameMode.CREATIVE || !(contains(player, item, data, toRemove))) {
            return false;
        }
        for (final int i : player.getInventory().all(item).keySet()) {
            if (toRemove <= 0) {
                continue;
            }
            final ItemStack stack = player.getInventory().getItem(i);
            if (stack.getData() == null || stack.getData().getData() == data) {
                final int foundAmount = stack.getAmount();
                if (toRemove >= foundAmount) {
                    toRemove -= foundAmount;
                    player.getInventory().setItem(i, null);
                    continue;
                }
                stack.setAmount(foundAmount - toRemove);
                player.getInventory().setItem(i, stack);
                toRemove = 0;
            }
        }
        player.updateInventory();
        return true;
    }

    public static void insert(final Player player, final ItemStack item) {
        if (UtilPlayer.isInventoryEmpty(player)) {
            player.getInventory().addItem(UtilItem.updateNames(item));
        } else {
            player.getWorld().dropItemNaturally(player.getLocation(), UtilItem.updateNames(item));
        }
    }

    public static ItemStack getSkull(final String name) {
        final ItemStack skull = new ItemStack(Material.SKULL_ITEM);
        skull.setDurability((short) 3);
        final SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(name);
        skull.setItemMeta(meta);
        return UtilItem.updateNames(skull);
    }
}