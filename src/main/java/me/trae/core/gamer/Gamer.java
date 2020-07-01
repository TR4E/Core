package me.trae.core.gamer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class Gamer {

    private final UUID uuid;

    private final Set<UUID> ignored;
    private int kills;
    private int deaths;
    private UUID invInspecting;

    public Gamer(final UUID uuid) {
        this.uuid = uuid;
        this.ignored = new HashSet<>();
        this.kills = 0;
        this.deaths = 0;
        this.invInspecting = null;
    }

    public final UUID getUUID() {
        return uuid;
    }

    public final Set<UUID> getIgnored() {
        return ignored;
    }

    public final int getKills() {
        return kills;
    }

    public void setKills(final int kills) {
        this.kills = kills;
    }

    public final int getDeaths() {
        return deaths;
    }

    public void setDeaths(final int deaths) {
        this.deaths = deaths;
    }

    public final String getKDR() {
        return (kills == 0 && deaths == 0 ? "0.0" : (float) kills / deaths + "");
    }

    public final UUID getInvInspecting() {
        return invInspecting;
    }

    public void setInvInspecting(final UUID invInspecting) {
        this.invInspecting = invInspecting;
    }
}