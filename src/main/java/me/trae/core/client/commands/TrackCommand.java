package me.trae.core.client.commands;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.module.update.UpdateEvent;
import me.trae.core.module.update.Updater;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrackCommand extends Command {

    private final Map<UUID, UUID> tracker = new HashMap<>();

    public TrackCommand(final Main instance) {
        super(instance, "track", new String[]{"track", "locate"}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            final Client targetC = getInstance().getClientUtilities().getOnlineClient(target.getUniqueId());
            if (targetC == null) {
                return;
            }
            if (target == player) {
                UtilMessage.message(player, "Track", "You cannot track yourself.");
                return;
            }
            if (target.getWorld() != player.getWorld()) {
                UtilMessage.message(player, "Track", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " is not in this World!");
                return;
            }
            if (getInstance().getEffectManager().isVanished(target)) {
                UtilMessage.message(player, "Player Search", ChatColor.YELLOW + "0" + ChatColor.GRAY + " matches found [" + ChatColor.YELLOW + args[0] + ChatColor.GRAY + "]");
                return;
            }
            if (targetC.isAdministrating() || targetC.isObserving()) {
                UtilMessage.message(player, "Track", "You cannot track this player at this time!");
                return;
            }
            tracker.put(player.getUniqueId(), target.getUniqueId());
            UtilMessage.message(player, "Track", "Compass pointing to location of " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
        }
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Track", "Usage: " + ChatColor.AQUA + "/track <player>");
    }

    @EventHandler
    public void onUpdate(final UpdateEvent e) {
        if (e.getUpdateType() == Updater.UpdateType.TICK_50) {
            for (final UUID uuid : tracker.keySet()) {
                if (Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(tracker.get(uuid)) != null) {
                    Bukkit.getPlayer(uuid).setCompassTarget(Bukkit.getPlayer(tracker.get(uuid)).getLocation());
                }
            }
        }
    }
}