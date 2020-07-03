package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.effect.Effect;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public class GodCommand extends Command {

    public GodCommand(final Main instance) {
        super(instance, "godmode", new String[]{"god"}, Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (args == null || args.length == 0) {
            if (getInstance().getEffectManager().hasGodMode(player)) {
                getInstance().getEffectManager().removeEffect(player, Effect.EffectType.GOD_MODE);
            } else {
                getInstance().getEffectManager().addEffect(player, Effect.EffectType.GOD_MODE);
            }
            UtilMessage.message(player, "God", "God Mode: " + (getInstance().getEffectManager().hasGodMode(player) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
            getInstance().getClientUtilities().messageStaff("God", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + (getInstance().getEffectManager().hasGodMode(player) ? " is now Invincible." : " is no longer Invincible."), Rank.ADMIN, new UUID[]{player.getUniqueId()});
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            if (target == player) {
                if (getInstance().getEffectManager().hasGodMode(player)) {
                    getInstance().getEffectManager().removeEffect(player, Effect.EffectType.GOD_MODE);
                } else {
                    getInstance().getEffectManager().addEffect(player, Effect.EffectType.GOD_MODE);
                }
                UtilMessage.message(player, "God", "God Mode: " + (getInstance().getEffectManager().hasGodMode(player) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
                getInstance().getClientUtilities().messageStaff("God", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + (getInstance().getEffectManager().hasGodMode(player) ? " is now Invincible." : " is no longer Invincible."), Rank.ADMIN, new UUID[]{player.getUniqueId()});
                return;
            }
            final Client targetC = getInstance().getClientUtilities().getOnlineClient(target.getUniqueId());
            if (targetC == null) {
                return;
            }
            if (!(player.isOp())) {
                if (targetC.getRank().ordinal() >= client.getRank().ordinal()) {
                    UtilMessage.message(player, "God", "You cannot toggle God mode for this Player!");
                    return;
                }
            }
            if (getInstance().getEffectManager().hasGodMode(target)) {
                getInstance().getEffectManager().removeEffect(target, Effect.EffectType.GOD_MODE);
            } else {
                getInstance().getEffectManager().addEffect(target, Effect.EffectType.GOD_MODE);
            }
            UtilMessage.message(target, "God", "God Mode: " + (getInstance().getEffectManager().hasGodMode(target) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
            getInstance().getClientUtilities().messageStaff("God", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + (getInstance().getEffectManager().hasGodMode(target) ? " is now Invincible " : " is no longer Invincible ") + " by " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".", Rank.ADMIN, new UUID[]{target.getUniqueId()});
        }
    }

    @Override
    public void help(final Player player) {

    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player player = (Player) e.getEntity();
            if (getInstance().getEffectManager().hasGodMode(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDamageByPlayer(final EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player player = (Player) e.getEntity();
            if (getInstance().getEffectManager().hasGodMode(player)) {
                e.setCancelled(true);
            }
        }
    }
}