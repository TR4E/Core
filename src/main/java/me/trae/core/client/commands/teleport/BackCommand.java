package me.trae.core.client.commands.teleport;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackCommand extends Command {

    private final Map<UUID, Location> map;

    public BackCommand(final Main instance) {
        super(instance, "back", new String[]{}, Rank.PLAYER);
        this.map = new HashMap<>();
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            if (!(map.containsKey(player.getUniqueId())) || (map.containsKey(player.getUniqueId()) && map.get(player.getUniqueId()) == null)) {
                map.remove(player.getUniqueId());
                UtilMessage.message(player, "Back", "You do not have a last death location to teleport back to.");
                return;
            }
            if (getInstance().getRechargeManager().add(player, "Back Command", (getInstance().getRepository().getBackCommandCooldown() * 1000L), true)) {
                player.teleport(map.get(player.getUniqueId()));
                map.remove(player.getUniqueId());
                UtilPlayer.sound(player, Sound.ENDERMAN_TELEPORT, 2.0F, 1.0F);
                UtilMessage.message(player, "Back", "You teleported to your last death location.");
            }
        }
    }

    @Override
    public void help(final Player player) {

    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e) {
        final Player player = e.getEntity();
        map.put(player.getUniqueId(), player.getLocation());
        UtilMessage.message(player, "Back", "You can teleport back to your last death location with " + ChatColor.AQUA + "/back" + ChatColor.GRAY + ".");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (map.containsKey(player.getUniqueId()) && map.get(player.getUniqueId()) != null) {
                    map.remove(player.getUniqueId());
                }
            }
        }.runTaskLater(getInstance(), 300000L);
    }
}