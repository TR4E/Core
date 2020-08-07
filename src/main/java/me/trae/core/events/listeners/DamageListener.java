package me.trae.core.events.listeners;

import me.trae.core.Main;
import me.trae.core.events.CustomDamageEvent;
import me.trae.core.module.CoreListener;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.PlayerInventory;

public class DamageListener extends CoreListener {

    public DamageListener(final Main instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(final EntityDamageEvent e) {
        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(e.getEntity(), e.getDamage(), e.getCause()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Projectile) {
            Bukkit.getPluginManager().callEvent(new CustomDamageEvent(e.getEntity(), e.getDamager(), e.getDamage()));
            return;
        }
        if (e.getDamager() instanceof LivingEntity) {
            e.setDamage(handleDamage((LivingEntity) e.getDamager()));
            ((LivingEntity) e.getEntity()).setLastDamage(e.getDamage());
            UtilMessage.broadcast(e.getDamage() + "");
        }
        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(e.getEntity(), e.getDamager(), e.getDamage(), e.getCause()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCustomDamage(final CustomDamageEvent e) {
        if (e.isKnockback()) {
            e.getDamagee().setVelocity(e.getDamagee().getLocation().getDirection().multiply(-3.5D));
        }
        if (e.isDurability()) {
            if (e.getDamagee() instanceof Player) {
                final Player damagee = (Player) e.getDamagee();
                final PlayerInventory inv = damagee.getInventory();
                inv.getHelmet().setDurability((short) (inv.getHelmet().getDurability() + 1));
                inv.getChestplate().setDurability((short) (inv.getChestplate().getDurability() + 1));
                inv.getLeggings().setDurability((short) (inv.getLeggings().getDurability() + 1));
                inv.getBoots().setDurability((short) (inv.getBoots().getDurability() + 1));
                damagee.updateInventory();
            }
        }
    }

    @EventHandler
    public void onCustomDamageEvent(final CustomDamageEvent e) {
        if (e.getDamager() instanceof Player) {
            if (e.getDamagee() instanceof Villager) {
                e.setKnockback(true);
            }
            if (e.getDamagee() instanceof Zombie) {
                ((Zombie) e.getDamagee()).setLastDamage(5.0);
            }
            if (e.getDamagee() instanceof Cow) {
                e.setCancelled(true);
            }
            if (e.getDamagee() instanceof Player) {
                e.setDurability(false);
            }
            e.setKnockback(false);
            UtilMessage.broadcast("Damage", "Damage Information:");
            UtilMessage.broadcast(ChatColor.DARK_GREEN + "Damager: " + ChatColor.WHITE + e.getDamager().getName());
            UtilMessage.broadcast(ChatColor.DARK_GREEN + "Damagee: " + ChatColor.WHITE + (e.getDamagee() == null ? "None" : e.getDamagee().getName()));
            UtilMessage.broadcast(ChatColor.DARK_GREEN + "Cause: " + ChatColor.WHITE + (e.getCause() == null ? "None" : e.getCause().name()));
            UtilMessage.broadcast(ChatColor.DARK_GREEN + "Damage: " + ChatColor.WHITE + e.getDamage());
            UtilMessage.broadcast(ChatColor.DARK_GREEN + "Projectile: " + ChatColor.WHITE + (e.getProjectile() == null ? "None" : e.getProjectile()));
            UtilMessage.broadcast(ChatColor.DARK_GREEN + "Knockback: " + ChatColor.WHITE + e.isKnockback());
            UtilMessage.broadcast(ChatColor.DARK_GREEN + "Durability: " + ChatColor.WHITE + e.isDurability());
            UtilMessage.broadcast(ChatColor.DARK_GREEN + "Cancelled: " + ChatColor.WHITE + e.isCancelled());

        }
    }

    private double handleDamage(final LivingEntity e) {
        double damage = 0.5D;
        if (e.getEquipment().getItemInHand().getType() == Material.GOLD_SWORD) {
            damage += 6.0D;
        } else if (e.getEquipment().getItemInHand().getType() == Material.DIAMOND_SWORD) {
            damage += 5.0D;
        } else if (e.getEquipment().getItemInHand().getType() == Material.IRON_SWORD) {
            damage += 4.0D;
        } else if (e.getEquipment().getItemInHand().getType() == Material.STONE_SWORD) {
            damage += 3.0D;
        } else if (e.getEquipment().getItemInHand().getType() == Material.WOOD_SWORD) {
            damage += 2.0D;
        }
        return damage;
    }
}