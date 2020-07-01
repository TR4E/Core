package me.trae.core.module.update;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class UpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Updater.UpdateType updateType;

    public UpdateEvent(final Updater.UpdateType updateType) {
        this.updateType = updateType;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Updater.UpdateType getUpdateType() {
        return this.updateType;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
