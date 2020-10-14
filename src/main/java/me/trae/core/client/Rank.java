package me.trae.core.client;

import org.bukkit.ChatColor;

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
        for (final Rank rank : values()) {
            if (rank.ordinal() == ordinal) {
                return rank;
            }
        }
        return null;
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