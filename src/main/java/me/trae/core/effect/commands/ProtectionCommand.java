package me.trae.core.effect.commands;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.effect.Effect;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProtectionCommand extends Command {

    private final Map<UUID, Long> lastUpdate = new HashMap<>();

    public ProtectionCommand(final Main instance) {
        super(instance, "protection", new String[]{"prot", "pvptimer"}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            if (!(getInstance().getEffectManager().hasProtection(player))) {
                UtilMessage.message(player, "Protection", "You do not have protection.");
                return;
            }
            getInstance().getEffectManager().removeEffect(player, Effect.EffectType.PROTECTION);
            UtilMessage.message(player, "Protection", "You no longer have pvp protection, be careful!");
            return;
        }
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(player.isOp() || client.hasRank(Rank.ADMIN, false))) {
            return;
        }
    }

    @Override
    public void help(final Player player) {

    }

    private void addCommand(final Player player, final String[] args) {
        final Player target = UtilPlayer.searchPlayer(player, args[1], true);
        if (target == null) {
            return;
        }
        final long duration = (Integer.parseInt(args[2]) * 1000L);
        if (getInstance().getEffectManager().hasProtection(player)) {
            getInstance().getEffectManager().addEffect(target, Effect.EffectType.PROTECTION, duration);
            UtilMessage.message(player, "Protection", "You added extra " + ChatColor.GREEN + UtilTime.getTime(duration, UtilTime.TimeUnit.BEST, 1) + ChatColor.GRAY + " of protection to " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
            UtilMessage.message(target, "Protection", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " gave you extra " + ChatColor.GREEN + UtilTime.getTime(duration, UtilTime.TimeUnit.BEST, 1) + ChatColor.GRAY + " of protection.");
            getInstance().getClientUtilities().messageStaff("Protection", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " added extra " + ChatColor.GREEN + UtilTime.getTime(duration, UtilTime.TimeUnit.BEST, 1) + ChatColor.GRAY + " of protection.", Rank.OWNER, new UUID[]{player.getUniqueId(), target.getUniqueId()});
            return;
        }
        getInstance().getEffectManager().addEffect(target, Effect.EffectType.PROTECTION, duration);
        UtilMessage.message(player, "Protection", "You gave " + ChatColor.GREEN + UtilTime.getTime(duration, UtilTime.TimeUnit.BEST, 1) + ChatColor.GRAY + " of protection to " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
        UtilMessage.message(target, "Protection", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " gave you " + ChatColor.GREEN + UtilTime.getTime(duration, UtilTime.TimeUnit.BEST, 1) + ChatColor.GRAY + " of protection.");
        getInstance().getClientUtilities().messageStaff("Protection", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " gave " + ChatColor.GREEN + UtilTime.getTime(duration, UtilTime.TimeUnit.BEST, 1) + ChatColor.GRAY + " of protection.", Rank.OWNER, new UUID[]{player.getUniqueId(), target.getUniqueId()});
    }

    private void removeCommand(final Player player, final String[] args) {
        final Player target = UtilPlayer.searchPlayer(player, args[1], true);
        if (target == null) {
            return;
        }
        if (getInstance().getEffectManager().hasProtection(target)) {
            getInstance().getEffectManager().removeEffect(target, Effect.EffectType.PROTECTION);
            UtilMessage.message(player, "Protection", "You removed " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "'s protection.");
            UtilMessage.message(target, "Protection", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " removed your protection.");
            getInstance().getClientUtilities().messageStaff("Protection", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " removed " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "'s protection.", Rank.OWNER, new UUID[]{player.getUniqueId(), target.getUniqueId()});
        }
    }

    @EventHandler
    public void onUpdate(final UpdateEvent e) {
        if (e.getUpdateType() == Updater.UpdateType.FAST) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (getInstance().getEffectManager().getEffects(player).isEmpty()) {
                    continue;
                }
                for (final Effect effect : getInstance().getEffectManager().getEffects(player)) {
                    if (effect != null) {
                        if (effect.getType() == Effect.EffectType.PROTECTION) {
                            final long duration = (effect.getDuration() - System.currentTimeMillis());
                            if (!(lastUpdate.containsKey(player.getUniqueId()))) {
                                lastUpdate.put(player.getUniqueId(), System.currentTimeMillis());
                            }
                            if (duration > 300000L) {
                                if (UtilTime.elapsed(lastUpdate.get(player.getUniqueId()), 300000L)) {
                                    UtilMessage.message(player, "Protection", "You currently have " + ChatColor.GREEN + UtilTime.getTime(duration, UtilTime.TimeUnit.BEST, 1) + ChatColor.GRAY + " left of pvp protection.");
                                    UtilMessage.message(player, "Protection", "If you want to pvp, type '" + ChatColor.AQUA + "/protection" + ChatColor.GRAY + "' to remove your pvp protection.");
                                    UtilPlayer.sound(player, Sound.ORB_PICKUP);
                                    lastUpdate.put(player.getUniqueId(), System.currentTimeMillis());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}