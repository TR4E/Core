package me.trae.core.utility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UtilMessage {

    public static void message(final Player player, final String prefix, final String message) {
        player.sendMessage(ChatColor.BLUE + prefix + "> " + ChatColor.GRAY + message);
    }

    public static void message(final Player player, final String message) {
        player.sendMessage(message);
    }

    public static void broadcast(final String prefix, final String message) {
        Bukkit.getOnlinePlayers().forEach(o -> o.sendMessage(ChatColor.BLUE + prefix + "> " + ChatColor.GRAY + message));
    }

    public static void broadcast(final String message) {
        Bukkit.getOnlinePlayers().forEach(o -> o.sendMessage(message));
    }

    public static void log(final String prefix, final String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + prefix + "> " + ChatColor.GRAY + message);
    }

    public static void log(final String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }
}