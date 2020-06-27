package me.trae.core.utility;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class UtilPlayer {

    public static String getIP(final Player player) {
        return player.getAddress().getAddress().getHostAddress();
    }

    public static int getPing(final Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    public static boolean isInventoryEmpty(final Player player) {
        return (player.getInventory().firstEmpty() == -1);
    }

    public static void clearInventory(final Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
    }

    public static void sound(final Player player, final Sound sound) {
        player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
    }

    public static void sound(final Sound sound) {
        Bukkit.getOnlinePlayers().forEach(o -> o.playSound(o.getLocation(), sound, 1.0F, 1.0F));
    }
}