package me.trae.core.world;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.gamer.Gamer;
import me.trae.core.module.CoreListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class ChatListener extends CoreListener implements Listener {

    public ChatListener(final Main instance) {
        super(instance);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerChat(final AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        final Player player = e.getPlayer();
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (client.isStaffChat()) {
            getInstance().getClientUtilities().messageStaff(ChatColor.RED.toString() + ChatColor.BOLD + player.getName() + "> " + ChatColor.WHITE.toString() + ChatColor.BOLD + e.getMessage(), Rank.HELPER, null);
            return;
        }
        final String rank = (client.getRank() != Rank.PLAYER ? client.getRank().getTag(true) + " " : "");
        final String message = (client.isAdministrating() ? ChatColor.translateAlternateColorCodes('&', e.getMessage()) : e.getMessage());
        for (final Player online : Bukkit.getOnlinePlayers()) {
            final Gamer gamer = getInstance().getGamerUtilities().getGamer(online.getUniqueId());
            if (gamer != null && gamer.getIgnored() != null && gamer.getIgnored().contains(player.getUniqueId())) {
                continue;
            }
            online.sendMessage(rank + ChatColor.YELLOW + player.getName() + ChatColor.WHITE + ": " + message);
        }
    }
}