package me.trae.core.world;

import me.trae.core.Main;
import me.trae.core.module.CoreListener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

public final class ServerListener extends CoreListener {

    public ServerListener(final Main instance) {
        super(instance);
    }

    @EventHandler
    public void onServerListPing(final ServerListPingEvent e) {
        e.setMaxPlayers(getInstance().getRepository().getMaxPlayerSlots());
        e.setMotd(ChatColor.translateAlternateColorCodes('&', getInstance().getRepository().getServerMOTD()));
    }
}