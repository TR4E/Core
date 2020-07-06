package me.trae.core.world;

import me.trae.core.Main;
import me.trae.core.effect.Effect;
import me.trae.core.module.CoreListener;
import me.trae.core.module.update.UpdateEvent;
import me.trae.core.module.update.Updater;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ItemListener extends CoreListener {

    private final List<Item> items;

    public ItemListener(final Main instance) {
        super(instance);
        this.items = new ArrayList<>();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(final PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        if (player.getInventory().getItemInHand() != null) {
            if (player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL) {
                if (!(getInstance().getRepository().isFunThrowingPearl())) {
                    return;
                }
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    e.setCancelled(true);
                    if (getInstance().getRechargeManager().add(player, "Consume Ethereal Pearl", 500L, false)) {
                        for (final PotionEffect effect : player.getActivePotionEffects()) {
                            if (effect.getType() == PotionEffectType.POISON || effect.getType() == PotionEffectType.BLINDNESS || effect.getType() == PotionEffectType.SLOW || effect.getType() == PotionEffectType.CONFUSION || effect.getType() == PotionEffectType.WEAKNESS || effect.getType() == PotionEffectType.WITHER) {
                                player.removePotionEffect(effect.getType());
                            }
                        }
                        player.getWorld().playSound(player.getLocation(), Sound.EAT, 2.0F, 1.0F);
                        UtilItem.remove(player, Material.ENDER_PEARL, (byte) 0, 1);
                        UtilMessage.message(player, "Ethereal Pearl", "You removed all negative effects!");
                    }
                    return;
                }
                if (e.getAction() == Action.LEFT_CLICK_AIR) {
                    if (UtilBlock.isInLiquid(player)) {
                        UtilMessage.message(player, "Item", "You cannot use " + ChatColor.GREEN + "Ethereal Pearl" + ChatColor.GRAY + " while in liquid.");
                        return;
                    }
                    if (player.getVehicle() == null) {
                        if (getInstance().getRechargeManager().add(player, "Ethereal Pearl", 15000, true)) {
                            final Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.ENDER_PEARL));
                            UtilItem.remove(player, Material.ENDER_PEARL, (byte) 0, 1);
                            item.setPickupDelay(Integer.MAX_VALUE);
                            item.setVelocity(player.getLocation().getDirection().multiply(1.8D));
                            item.setPassenger(player);
                            items.add(item);
                            getInstance().getEffectManager().addEffect(player, Effect.EffectType.NO_FALL, 5000);
                        }
                    }
                }
            }
        } else if (player.getInventory().getItemInHand().getType() == Material.TNT) {
            if (!(getInstance().getRepository().isFunThrowingTNT())) {
                return;
            }
            if (getInstance().getRechargeManager().add(player, "Throwing TNT", 20000, true)) {
                final TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation().add(0.0D, 0.5D, 0.0D), TNTPrimed.class);
                UtilItem.remove(player, Material.TNT, (byte) 0, 1);
                tnt.setFuseTicks(100);
                tnt.setVelocity(player.getLocation().getDirection().multiply(2));
            }
        }
    }

    @EventHandler
    public void onUpdate(final UpdateEvent e) {
        if (e.getUpdateType() == Updater.UpdateType.TICK) {
            final Iterator<Item> it = items.iterator();
            if (it.hasNext()) {
                final Item item = it.next();
                if (item != null) {
                    if (item.getItemStack().getType() == Material.ENDER_PEARL) {
                        final Location itemLoc = item.getLocation();
                        itemLoc.setY(itemLoc.getY() - 0.10);
                        if (itemLoc.getBlock().getType() != Material.AIR || item.getPassenger() == null || item.getLocation().getBlock().isLiquid()) {
                            if (item.getPassenger() != null) {
                                if (item.getPassenger() instanceof Player) {
                                    final Player player = (Player) item.getPassenger();
                                    getInstance().getTitleManager().sendActionBar(player, " ");
                                    final Location loc = player.getLocation();
                                    loc.setY(loc.getY() + 1);
                                    player.teleport(loc);
                                    player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0F, 1.0F);
                                }
                            }
                            item.remove();
                            it.remove();
                        }
                    }
                }
            }
        }
    }
}