package me.trae.core.world;

import me.trae.core.Main;
import me.trae.core.module.CoreListener;
import me.trae.core.module.update.UpdateEvent;
import me.trae.core.module.update.Updater;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
                e.setCancelled(true);
                if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                    UtilMessage.message(player, "Ethereal Pearl", "You need to left click to use " + ChatColor.YELLOW + "Ethereal Pearl" + ChatColor.GRAY + ".");
                    return;
                }
                if (e.getAction() == Action.LEFT_CLICK_AIR) {
                    if (player.getVehicle() == null) {
                        if (getInstance().getRechargeManager().add(player, "Ethereal Pearl", 15000, true)) {
                            final Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.ENDER_PEARL));
                            UtilItem.remove(player, Material.ENDER_PEARL, (byte) 0, 1);
                            item.setPickupDelay(Integer.MAX_VALUE);
                            item.setVelocity(player.getLocation().getDirection().multiply(1.8D));
                            item.setPassenger(player);
                            items.add(item);
                            // TODO: 3/07/2020 - getInstance().getEffectManager().addEffect(player, EffectType.NO_FALL, 5000);

                        }
                    }
                }
            } else if (player.getInventory().getItemInHand().getType() == Material.WEB) {
                if (!(getInstance().getRepository().isFunThrowingWeb())) {
                    return;
                }
                e.setCancelled(true);
                if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                    UtilMessage.message(player, "Throwing Web", "You need to left click to use " + ChatColor.YELLOW + "Throwing Web" + ChatColor.GRAY + ".");
                    return;
                }
                if (e.getAction() == Action.LEFT_CLICK_AIR) {
                    if (getInstance().getRechargeManager().add(player, "Throwing Web", 10000, true)) {
                        final Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.WEB));
                        UtilItem.remove(player, Material.WEB, (byte) 0, 1);
                        item.setPickupDelay(Integer.MAX_VALUE);
                        item.setVelocity(player.getLocation().getDirection().multiply(1.8D));
                        items.add(item);
                    }
                }
            } else if (player.getInventory().getItemInHand().getType() == Material.TNT) {
                if (!(getInstance().getRepository().isFunThrowingTNT())) {
                    return;
                }
                e.setCancelled(true);
                if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                    UtilMessage.message(player, "Throwing TNT", "You need to left click to use " + ChatColor.YELLOW + "Throwing TNT" + ChatColor.GRAY + ".");
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
    }

    @EventHandler
    public void onUpdate(final UpdateEvent e) {
        if (e.getUpdateType() == Updater.UpdateType.TICK) {
            final Iterator<Item> it = items.iterator();
            if (it.hasNext()) {
                final Item item = it.next();
                if (item != null) {
                    if (item.getItemStack().getType() == Material.ENDER_PEARL) {
                        if (item.isOnGround()) {
                            item.remove();
                            it.remove();
                        }
                    }
                }
            }
        }
    }
}