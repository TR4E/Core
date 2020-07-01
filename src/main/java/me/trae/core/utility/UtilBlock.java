package me.trae.core.utility;

import org.bukkit.Location;
import org.bukkit.block.Block;

public final class UtilBlock {

    public static Block getBlockUnder(final Location loc) {
        loc.setY(loc.getY() - 1);
        return loc.getBlock();
    }
}