package me.trae.core.world;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.module.CoreListener;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMessage;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.UUID;

public class WorldListener extends CoreListener {

    public WorldListener(final Main instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRespawn(final PlayerDeathEvent ev) {
        final Player plr = ev.getEntity();
        Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () -> {
            final PacketPlayInClientCommand packet = new PacketPlayInClientCommand();
            try {
                final Field a = PacketPlayInClientCommand.class.getDeclaredField("a");
                a.setAccessible(true);
                a.set(packet, PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN);
            } catch (final NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
                e.printStackTrace();
            }
            ((CraftPlayer) plr).getHandle().playerConnection.a(packet);
        }, 2L);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(final BlockPlaceEvent e) {
        final Player player = e.getPlayer();
        final Block block = e.getBlock();
        if (block == null) {
            return;
        }
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (block.getType() == Material.LAPIS_BLOCK) {
            block.setType(Material.WATER);
            block.getWorld().playSound(block.getLocation(), Sound.SPLASH, 1.0F, 1.0F);
            block.getWorld().playEffect(block.getLocation(), Effect.SPLASH, 0);
            block.getState().update();
        }
        if (!(client.isAdministrating())) {
            if (block.getType() == Material.WOODEN_DOOR
                    || block.getType() == Material.ACACIA_DOOR
                    || block.getType() == Material.SPRUCE_DOOR
                    || block.getType() == Material.BIRCH_DOOR
                    || block.getType() == Material.JUNGLE_DOOR
                    || block.getType() == Material.DARK_OAK_DOOR) {
                e.setCancelled(true);
                player.getInventory().setItemInHand(UtilItem.updateNames(new ItemStack(Material.IRON_DOOR, player.getInventory().getItemInHand().getAmount())));
                UtilMessage.message(player, "Game", "Please use " + ChatColor.YELLOW + "Iron Doors" + ChatColor.GRAY + " (You can right click to open them).");
            } else if (block.getType() == Material.TRAP_DOOR) {
                e.setCancelled(true);
                player.getInventory().setItemInHand(UtilItem.updateNames(new ItemStack(Material.IRON_TRAPDOOR, player.getInventory().getItemInHand().getAmount())));
                UtilMessage.message(player, "Game", "Please use " + ChatColor.YELLOW + "Iron Trap Doors" + ChatColor.GRAY + " (You can right click to open them).");
            } else if (block.getType() == Material.BEDROCK || block.getType() == Material.OBSIDIAN || block.getType() == Material.BARRIER) {
                e.setCancelled(true);
                if (!(player.isOp() || client.hasRank(Rank.ADMIN, false))) {
                    player.getInventory().remove(block.getType());
                    getInstance().getClientUtilities().messageAdmins("Admin Spy", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " tried to place " + ChatColor.GREEN + UtilFormat.cleanString(block.getType().name()) + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
                }
                UtilMessage.message(player, "Game", "You cannot place " + ChatColor.YELLOW + UtilFormat.cleanString(block.getType().name()) + ChatColor.GRAY + ".");
            } else if (block.getType() == Material.TNT) {
                if (getInstance().getRepository().isDisableTNT()) {
                    e.setCancelled(true);
                    UtilMessage.message(player, "Game", ChatColor.YELLOW + "TNT" + ChatColor.GRAY + " is currently disabled.");
                }
            } else if (block.getType() == Material.MOB_SPAWNER) {
                if (getInstance().getRepository().isDisableMobSpawners()) {
                    e.setCancelled(true);
                    UtilMessage.message(player, "Game", ChatColor.YELLOW + "Mob Spawners" + ChatColor.GRAY + " is currently disabled.");
                }
            }
        }
    }
}