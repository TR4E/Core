package me.trae.core.effect;

import me.trae.core.Main;
import me.trae.core.module.CoreListener;
import me.trae.core.module.update.UpdateEvent;
import me.trae.core.module.update.Updater;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EffectManager extends CoreListener {

    private final Set<Effect> effects = new HashSet<>();

    public EffectManager(final Main instance) {
        super(instance);
    }

    public final Set<Effect> getEffects() {
        return effects;
    }

    public void addEffect(final Player player, final Effect.EffectType type) {
        addEffect(player, type, Integer.MAX_VALUE, 1);
    }

    public void addEffect(final Player player, final Effect.EffectType type, final long duration) {
        addEffect(player, type, duration, 1);
    }

    public void addEffect(final Player player, final Effect.EffectType type, final int level) {
        addEffect(player, type, Integer.MAX_VALUE, level);
    }

    public void addEffect(final Player player, final Effect.EffectType type, final long duration, final int level) {
        if (hasEffect(player, type)) {
            removeEffect(player, type);
        }
        if (type == Effect.EffectType.GOD_MODE) {
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
        } else if (type == Effect.EffectType.VANISHED) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 2));
        }
        getEffects().add(new Effect(player.getUniqueId(), type, System.currentTimeMillis() + duration, level));
    }

    public void removeEffect(final Player player, final Effect.EffectType type) {
        if (type == Effect.EffectType.VANISHED) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
        getEffects().removeIf(e -> (e.getUUID().equals(player.getUniqueId()) && e.getType() == type));
    }

    public void clearEffects(final Player player) {
        getEffects(player).forEach(e -> removeEffect(player, e.getType()));
    }

    public final Set<Effect> getEffects(final Player player) {
        return effects.stream().filter(e -> e.getUUID().equals(player.getUniqueId())).collect(Collectors.toSet());
    }

    public final Effect getEffect(final Player player, final Effect.EffectType type) {
        return getEffects(player).stream().filter(e -> e.getType() == type).findFirst().orElse(null);
    }

    public final boolean hasEffect(final Player player, final Effect.EffectType type) {
        return effects.stream().anyMatch(e -> e.getUUID().equals(player.getUniqueId()) && e.getType() == type);
    }

    public final boolean hasNoFall(final Player player) {
        return hasEffect(player, Effect.EffectType.NO_FALL);
    }

    public final boolean hasProtection(final Player player) {
        return hasEffect(player, Effect.EffectType.PROTECTION);
    }

    public final boolean hasGodMode(final Player player) {
        return hasEffect(player, Effect.EffectType.GOD_MODE);
    }

    public final boolean isVanished(final Player player) {
        return hasEffect(player, Effect.EffectType.VANISHED);
    }

    @EventHandler
    public void onPlayerDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player player = (Player) e.getEntity();
            if (hasProtection(player) || hasGodMode(player)) {
                e.setCancelled(true);
            } else if (hasNoFall(player)) {
                if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamageByPlayer(final EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player player = (Player) e.getEntity();
            if (hasGodMode(player)) {
                e.setCancelled(true);
            } else if (hasProtection(player)) {
                e.setCancelled(true);
                if (e.getDamager() instanceof Player) {
                    final Player damager = (Player) e.getDamager();
                    if (!(hasProtection(damager))) {
                        UtilMessage.message(damager, "Protection", "This is a new player and currently has protection!");
                    }
                }
            }
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            final Player damager = (Player) e.getDamager();
            if (hasProtection(damager)) {
                e.setCancelled(true);
                UtilMessage.message(damager, "Protection", "You cannot damage other players while you have protection!");
                UtilMessage.message(damager, "Protection", "Type '" + ChatColor.AQUA + "/protection" + ChatColor.GRAY + "' to remove your protection.");
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e) {
        if (getEffects(e.getEntity()) != null) {
            for (final Effect effect : getEffects(e.getEntity())) {
                if (effect.getType() == Effect.EffectType.GOD_MODE || effect.getType() == Effect.EffectType.VANISHED) {
                    continue;
                }
                removeEffect(e.getEntity(), effect.getType());
            }
        }
    }

    @EventHandler
    public void onUpdate(final UpdateEvent e) {
        if (e.getUpdateType() == Updater.UpdateType.TICK_50) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                for (final Effect effect : getEffects(player)) {
                    if (effect != null) {
                        if (effect.hasExpired()) {
                            removeEffect(player, effect.getType());
                            if (effect.getType() == Effect.EffectType.PROTECTION) {
                                player.getWorld().playSound(player.getLocation(), Sound.VILLAGER_HIT, 1.0F, 1.0F);
                                UtilMessage.message(player, "Protection", "You no longer have protection, be careful!");
                            }
                        }
                    }
                }
            }
        }
    }
}