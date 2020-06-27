package me.trae.core.client;

import org.bukkit.ChatColor;

import java.util.Arrays;

public enum Rank {

    PLAYER("Player", ChatColor.YELLOW),
    MEDIA("Media", ChatColor.LIGHT_PURPLE),
    HELPER("Helper", ChatColor.DARK_GREEN),
    MOD("Mod", ChatColor.AQUA),
    HEADMOD("Head Mod", ChatColor.AQUA),
    ADMIN("Admin", ChatColor.RED),
    OWNER("Owner", ChatColor.DARK_RED);

    private final String prefix;
    private final ChatColor color;

    Rank(final String prefix, final ChatColor color) {
        this.prefix = prefix;
        this.color = color;
    }

    public static Rank getRank(final int ordinal) {
        return Arrays.stream(values()).filter(r -> r.ordinal() == ordinal).findFirst().orElse(null);
    }

    public final String getPrefix() {
        return prefix;
    }

    public final ChatColor getColor() {
        return color;
    }

    public final String getTag(final boolean bold) {
        return getColor() + (bold ? ChatColor.BOLD.toString() : "") + getPrefix();
    }
}