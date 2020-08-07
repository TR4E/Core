package me.trae.core.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class CustomDamageEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Entity damagee, damager;
    private final Projectile projectile;
    private final EntityDamageEvent.DamageCause cause;
    private double damage;
    private boolean knockback, durability, cancelled;

    public CustomDamageEvent(final Entity damagee, final Entity damager, final Projectile projectile, final double damage, final EntityDamageEvent.DamageCause cause) {
        this.damagee = damagee;
        this.damager = damager;
        this.projectile = projectile;
        this.damage = damage;
        this.cause = cause;
    }

    public CustomDamageEvent(final Entity damagee, final Projectile projectile, final double damage, final EntityDamageEvent.DamageCause cause) {
        this.damagee = damagee;
        this.damager = null;
        this.projectile = projectile;
        this.damage = damage;
        this.cause = cause;
    }

    public CustomDamageEvent(final Entity damagee, final double damage, final EntityDamageEvent.DamageCause cause) {
        this.damagee = damagee;
        this.damager = null;
        this.projectile = null;
        this.damage = damage;
        this.cause = cause;
    }

    public CustomDamageEvent(final Entity damagee, final Entity damager, final double damage) {
        this.damagee = damagee;
        this.damager = null;
        this.projectile = null;
        this.damage = damage;
        this.cause = null;
    }

    public CustomDamageEvent(final Entity damagee, final double damage) {
        this.damagee = damagee;
        this.damager = null;
        this.projectile = null;
        this.damage = damage;
        this.cause = null;
    }

    public CustomDamageEvent(final Entity damagee, final Entity damager, final double damage, final EntityDamageEvent.DamageCause cause) {
        this.damagee = damagee;
        this.damager = damager;
        this.projectile = null;
        this.damage = damage;
        this.cause = cause;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public final Entity getDamagee() {
        return damagee;
    }

    public final Entity getDamager() {
        return damager;
    }

    public final Projectile getProjectile() {
        return projectile;
    }

    public final double getDamage() {
        return damage;
    }

    public void setDamage(final double damage) {
        this.damage = damage;
    }

    public final EntityDamageEvent.DamageCause getCause() {
        return cause;
    }

    public final boolean isKnockback() {
        return knockback;
    }

    public void setKnockback(final boolean knockback) {
        this.knockback = knockback;
    }

    public final boolean isDurability() {
        return durability;
    }

    public void setDurability(final boolean durability) {
        this.durability = durability;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
}