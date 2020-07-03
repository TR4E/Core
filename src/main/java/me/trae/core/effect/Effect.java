package me.trae.core.effect;

import java.util.UUID;

public final class Effect {

    private final UUID uuid;
    private final EffectType type;
    private final long duration;
    private int level;

    public Effect(final UUID uuid, final EffectType type, final long duration) {
        this.uuid = uuid;
        this.type = type;
        this.duration = duration;
    }

    public Effect(final UUID uuid, final EffectType type, final long duration, final int level) {
        this.uuid = uuid;
        this.type = type;
        this.duration = duration;
        this.level = level;
    }

    public final UUID getUUID() {
        return uuid;
    }

    public final EffectType getType() {
        return type;
    }

    public final long getDuration() {
        return duration;
    }

    public final int getLevel() {
        return level;
    }

    public final boolean hasExpired() {
        return (duration - System.currentTimeMillis() <= 0);
    }


    public enum EffectType {
        NO_FALL, PROTECTION, GOD_MODE, VANISHED
    }
}