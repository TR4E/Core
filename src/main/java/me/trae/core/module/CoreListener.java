package me.trae.core.module;

import me.trae.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class CoreListener implements Listener {

    private final Main instance;

    public CoreListener(final Main instance) {
        this.instance = instance;
        Bukkit.getServer().getPluginManager().registerEvents(this, instance);
    }

    public final Main getInstance() {
        return instance;
    }
}