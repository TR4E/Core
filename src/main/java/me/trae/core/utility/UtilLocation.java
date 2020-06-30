package me.trae.core.utility;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public final class UtilLocation {

    public static String locToString(final Location loc) {
        return ChatColor.GRAY + "(" + ChatColor.YELLOW + ((int) loc.getX()) + ChatColor.GRAY + ", " + ChatColor.YELLOW + ((int) loc.getY()) + ChatColor.GRAY + ", " + ChatColor.YELLOW + ((int) loc.getZ()) + ChatColor.GRAY + ")";
    }
}