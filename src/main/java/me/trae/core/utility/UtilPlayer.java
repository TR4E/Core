package me.trae.core.utility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public final class UtilPlayer {

    public static String getIP(final Player player) {
        return player.getAddress().getAddress().getHostAddress();
    }

    public static int getPing(final Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    public static boolean isInventoryEmpty(final Player player) {
        return (player.getInventory().firstEmpty() != -1);
    }

    public static void clearInventory(final Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
    }

    public static void sound(final Player player, final Sound sound) {
        player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
    }

    public static void sound(final Player player, final Sound sound, final float var2, final float var3) {
        player.playSound(player.getLocation(), sound, var2, var3);
    }

    public static void sound(final Sound sound) {
        Bukkit.getOnlinePlayers().forEach(o -> o.playSound(o.getLocation(), sound, 1.0F, 1.0F));
    }

    public static Player searchPlayer(final Player player, final String name, final boolean inform) {
        if (Bukkit.getOnlinePlayers().stream().anyMatch(p -> p.getName().toLowerCase().equals(name.toLowerCase()))) {
            return Bukkit.getOnlinePlayers().stream().filter(p -> p.getName().toLowerCase().contains(name.toLowerCase())).findFirst().get();
        }
        final List<Player> playerList = Bukkit.getOnlinePlayers().parallelStream().filter(p -> p.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        if (playerList.size() == 1) {
            return playerList.get(0);
        } else if (inform) {
            UtilMessage.message(player, "Player Search", ChatColor.YELLOW.toString() + playerList.size() + ChatColor.GRAY + " matches found [" + ((playerList.size() == 0) ? ChatColor.YELLOW + name : playerList.stream().map(p -> ChatColor.YELLOW + p.getName()).collect(Collectors.joining(ChatColor.GRAY + ", "))) + ChatColor.GRAY + "].");
        }
        return null;
    }
}