package me.trae.core.utility;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public final class UtilBlock {

    public static Block getBlockUnder(final Location loc) {
        loc.setY(loc.getY() - 1);
        return loc.getBlock();
    }

    public static Block getBlockUnder(final Location loc, final int amount) {
        loc.setY(loc.getY() - amount);
        return loc.getBlock();
    }

    public static boolean isInWater(final Player player) {
        final Location loc = player.getLocation();
        loc.setY(loc.getY() - 1);
        return (loc.getBlock().getType() == Material.WATER || loc.getBlock().getType() == Material.STATIONARY_WATER);
    }

    public static boolean isInLava(final Player player) {
        final Location loc = player.getLocation();
        loc.setY(loc.getY() - 1);
        return (loc.getBlock().getType() == Material.LAVA || loc.getBlock().getType() == Material.STATIONARY_LAVA);
    }

    public static boolean isInLiquid(final Player player) {
        return (isInWater(player) || isInLava(player));
    }
}