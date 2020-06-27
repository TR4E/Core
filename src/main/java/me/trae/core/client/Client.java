package me.trae.core.client;

import me.trae.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class Client {

    private final UUID uuid;
    private final Set<String> ipAddresses;
    private String name, oldName;
    private Rank rank;
    private long firstJoined, lastOnline;
    private Location observer;
    private boolean administrating, staffchat, vanished;

    public Client(final UUID uuid) {
        this.uuid = uuid;
        this.ipAddresses = new HashSet<>();
        this.rank = Rank.PLAYER;
        this.administrating = false;
        this.staffchat = false;
        this.vanished = false;
    }

    public final UUID getUUID() {
        return uuid;
    }

    public final String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public final String getOldName() {
        return oldName;
    }

    public void setOldName(final String oldName) {
        this.oldName = oldName;
    }

    public final Set<String> getIPAddresses() {
        return ipAddresses;
    }

    public final Rank getRank() {
        return rank;
    }

    public void setRank(final Rank rank) {
        this.rank = rank;
    }

    public final boolean hasRank(final Rank rank, final boolean inform) {
        if (!(getRank().ordinal() >= rank.ordinal())) {
            if (inform) {
                if (Bukkit.getPlayer(getUUID()) != null) {
                    UtilMessage.message(Bukkit.getPlayer(getUUID()), "Permissions", "This requires Permission Rank [" + rank.getTag(false) + ChatColor.GRAY + "].");
                }
            }
            return false;
        }
        return true;
    }

    public final long getFirstJoined() {
        return firstJoined;
    }

    public void setFirstJoined(final long firstJoined) {
        this.firstJoined = firstJoined;
    }

    public final long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(final long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public final Location getObserverLocation() {
        return observer;
    }

    public void setObserverLocation(final Location observer) {
        this.observer = observer;
    }

    public final boolean isObserving() {
        return observer != null;
    }

    public final boolean isAdministrating() {
        return administrating;
    }

    public void setAdministrating(final boolean administrating) {
        this.administrating = administrating;
    }

    public final boolean isStaffChat() {
        return staffchat;
    }

    public void setStaffChat(final boolean staffchat) {
        this.staffchat = staffchat;
    }

    public final boolean isVanished() {
        return vanished;
    }

    public void setVanished(final boolean vanished) {
        this.vanished = vanished;
    }
}