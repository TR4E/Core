package me.trae.core.client.commands.teleport;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.module.update.UpdateEvent;
import me.trae.core.module.update.Updater;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import me.trae.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpawnCommand extends Command {

    private final Map<UUID, Long> timer;

    public SpawnCommand(final Main instance) {
        super(instance, "spawn", new String[]{}, (instance.getRepository().isSpawnCommandAdminOnly() ? Rank.ADMIN : Rank.PLAYER));
        this.timer = new HashMap<>();
    }

    @Override
    public void execute(final Player player, final String[] args) {
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (args == null || args.length == 0) {
            if (client.isAdministrating() || getInstance().getRepository().getSpawnCommandCountdown() == 0) {
                player.teleport(Bukkit.getWorld(getInstance().getRepository().getServerWorld()).getSpawnLocation());
                UtilPlayer.sound(player, Sound.ENDERMAN_TELEPORT);
                UtilMessage.message(player, "Spawn", "You teleported to " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + ".");
                getInstance().getClientUtilities().messageAdmins("Spawn", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported to " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
                return;
            }
            if (timer.containsKey(player.getUniqueId())) {
                UtilMessage.message(player, "Spawn", "You are already teleporting to spawn.");
                return;
            }
            if (!(getInstance().getRechargeManager().isCooling(player, "Spawn Command", true))) {
                timer.put(player.getUniqueId(), System.currentTimeMillis() + (getInstance().getRepository().getSpawnCommandCountdown() * 1000L));
                UtilMessage.message(player, "Spawn", "Teleporting to " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + " in " + ChatColor.GREEN + UtilTime.getTime(getInstance().getRepository().getSpawnCommandCountdown() * 1000L, UtilTime.TimeUnit.BEST, 1) + ChatColor.GRAY + ", do not move!");
                return;
            }
            return;
        }
        if (!(player.isOp() || client.hasRank(Rank.ADMIN, true))) {
            return;
        }
        final Player target = UtilPlayer.searchPlayer(player, args[0], true);
        if (target == null) {
            return;
        }
        if (target == player) {
            player.teleport(Bukkit.getWorld(getInstance().getRepository().getServerWorld()).getSpawnLocation());
            UtilPlayer.sound(player, Sound.ENDERMAN_TELEPORT);
            UtilMessage.message(player, "Spawn", "You teleported to " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + ".");
            getInstance().getClientUtilities().messageAdmins("Spawn", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported to " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
            return;
        }
        target.teleport(Bukkit.getWorld(getInstance().getRepository().getServerWorld()).getSpawnLocation());
        UtilPlayer.sound(target, Sound.ENDERMAN_TELEPORT);
        UtilMessage.message(target, "Spawn", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported you to " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + ".");
        UtilMessage.message(player, "Spawn", "You teleported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + ".");
        getInstance().getClientUtilities().messageAdmins("Spawn", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId(), target.getUniqueId()});
    }

    @Override
    public void help(final Player player) {

    }

    @EventHandler
    public void onUpdate(final UpdateEvent e) {
        if (e.getUpdateType() == Updater.UpdateType.TICK_50) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (timer.containsKey(player.getUniqueId())) {
                    if (timer.get(player.getUniqueId()) <= System.currentTimeMillis()) {
                        timer.remove(player.getUniqueId());
                        getInstance().getTitleManager().sendPlayer(player, "", "", 1);
                        player.teleport(Bukkit.getWorld(getInstance().getRepository().getServerWorld()).getSpawnLocation());
                        UtilPlayer.sound(player, Sound.ENDERMAN_TELEPORT);
                        UtilMessage.message(player, "Spawn", "You teleported to " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + ".");
                        getInstance().getClientUtilities().messageAdmins("Spawn", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported to " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
                    }
                }
                if (timer.containsKey(player.getUniqueId())) {
                    if (timer.get(player.getUniqueId()) > System.currentTimeMillis()) {
                        if (timer.get(player.getUniqueId()) == ((getInstance().getRepository().getSpawnCommandCountdown() * 1000L) / 2)) {
                            UtilMessage.message(player, "Spawn", "Teleporting to " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + " in " + ChatColor.GREEN + UtilTime.getTime(getInstance().getRepository().getSpawnCommandCountdown() * 1000L, UtilTime.TimeUnit.BEST, 1) + ChatColor.GRAY + ", do not move!");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent e) {
        final Player player = e.getPlayer();
        if ((e.getTo().getX() != e.getFrom().getX()) || (e.getTo().getZ() != e.getFrom().getZ())) {
            if (timer.containsKey(player.getUniqueId())) {
                timer.remove(player.getUniqueId());
                UtilMessage.message(player, "Spawn", "Teleporting to " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + " got cancelled due to moving.");
            }
        }
    }
}