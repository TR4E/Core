package me.trae.core.gamer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class Gamer {

    private final UUID uuid;

    private final Set<UUID> ignored;
    private int kills, deaths, blocksBroken, blocksPlaced;
    private UUID invInspecting, reply;

    public Gamer(final UUID uuid) {
        this.uuid = uuid;
        this.ignored = new HashSet<>();
        this.kills = 0;
        this.deaths = 0;
        this.blocksBroken = 0;
        this.blocksPlaced = 0;
        this.invInspecting = null;
        this.reply = null;
    }

    public final UUID getUUID() {
        return uuid;
    }

    public final Set<UUID> getIgnored() {
        return ignored;
    }

    public final List<String> getIgnoredList() {
        return ignored.stream().map(UUID::toString).collect(Collectors.toList());
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
        return (kills == 0 && deaths == 0 ? "0.0" : (double) kills / deaths + "");
    }

    public final int getBlocksBroken() {
        return blocksBroken;
    }

    public void setBlocksBroken(final int blocksBroken) {
        this.blocksBroken = blocksBroken;
    }

    public final int getBlocksPlaced() {
        return blocksPlaced;
    }

    public void setBlocksPlaced(final int blocksPlaced) {
        this.blocksPlaced = blocksPlaced;
    }

    public final UUID getInvInspecting() {
        return invInspecting;
    }

    public void setInvInspecting(final UUID invInspecting) {
        this.invInspecting = invInspecting;
    }

    public final UUID getReply() {
        return reply;
    }

    public void setReply(final UUID reply) {
        this.reply = reply;
    }
}