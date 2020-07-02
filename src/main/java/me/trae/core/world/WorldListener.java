package me.trae.core.world;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.module.CoreListener;
import me.trae.core.module.update.UpdateEvent;
import me.trae.core.module.update.Updater;
import me.trae.core.utility.*;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.UUID;

public final class WorldListener extends CoreListener {

    public WorldListener(final Main instance) {
        super(instance);
    }

    @EventHandler
    public void onUpdate(final UpdateEvent e) {
        if (e.getUpdateType() == Updater.UpdateType.TICK_50) {
            Bukkit.getOnlinePlayers().stream().filter(o -> getInstance().getClientUtilities().getOnlineClient(o.getUniqueId()).isVanished()).forEach(o -> getInstance().getTitleManager().sendActionBar(o, ChatColor.GREEN + "You are invisible to other players!"));
            final World world = Bukkit.getWorld(getInstance().getRepository().getServerWorld());
            if (world != null) {
                if (world.hasStorm()) {
                    world.setStorm(false);
                }
                if (world.isThundering()) {
                    world.setThundering(false);
                }
                if (getInstance().getRepository().isGameAlwaysDay()) {
                    if (world.getTime() != 1000L) {
                        world.setTime(1000L);
                    }
                } else if (getInstance().getRepository().isGameAlwaysNight()) {
                    if (world.getTime() != 13000L) {
                        world.setTime(13000L);
                    }
                }
            }
        }
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
            } else if (block.getType() == Material.BEDROCK || block.getType() == Material.BARRIER) {
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(final BlockBreakEvent e) {
        final Player player = e.getPlayer();
        final Block block = e.getBlock();
        if (block == null) {
            return;
        }
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(client.isAdministrating())) {
            if (block.getType() == Material.BEDROCK || block.getType() == Material.BARRIER) {
                e.setCancelled(true);
                UtilMessage.message(player, "Game", "You cannot break " + ChatColor.YELLOW + UtilFormat.cleanString(block.getType().name()) + ChatColor.GRAY + ".");
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onDoorInteract(final PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = e.getClickedBlock();
            if (block.getType() == Material.IRON_DOOR || e.getClickedBlock().getType() == Material.IRON_DOOR_BLOCK) {
                if (block.getData() >= 8) {
                    block = block.getRelative(BlockFace.DOWN);
                }
                if (block.getData() < 4) {
                    block.setData((byte) (block.getData() + 4), true);
                    block.getWorld().playSound(block.getLocation(), Sound.DOOR_OPEN, 1.0F, 1.0F);
                    block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 1);
                } else {
                    block.setData((byte) (block.getData() - 4), true);
                    block.getWorld().playSound(block.getLocation(), Sound.DOOR_CLOSE, 1.0F, 1.0F);
                }
                final EntityPlayer ep = ((CraftPlayer) e.getPlayer()).getHandle();
                final PacketPlayOutAnimation packet = new PacketPlayOutAnimation(ep, 0);
                ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(packet);
                e.setCancelled(true);
            } else if (block.getType() == Material.IRON_TRAPDOOR) {
                if (block.getData() < 4) {
                    block.setData((byte) (block.getData() + 4), true);
                    block.getWorld().playSound(block.getLocation(), Sound.DOOR_OPEN, 1.0F, 1.0F);
                } else {
                    block.setData((byte) (block.getData() - 4), true);
                    block.getWorld().playSound(block.getLocation(), Sound.DOOR_CLOSE, 1.0F, 1.0F);
                }
                final EntityPlayer ep = ((CraftPlayer) e.getPlayer()).getHandle();
                final PacketPlayOutAnimation packet = new PacketPlayOutAnimation(ep, 0);
                ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(packet);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemPickup(final PlayerPickupItemEvent e) {
        e.getItem().getItemStack().setItemMeta(UtilItem.updateNames(e.getItem().getItemStack()).getItemMeta());
    }

    @EventHandler
    public void onItemCraft(final PrepareItemCraftEvent e) {
        e.getInventory().setResult(UtilItem.updateNames(e.getRecipe().getResult()));
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        final Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        if (!(getInstance().getRepository().isGameBreakCrops()) && e.getAction() == Action.PHYSICAL && block.getType() == Material.SOIL) {
            e.setCancelled(true);
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.TNT && !(getInstance().getRepository().isDisableTNT() && getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating())) {
            e.setCancelled(true);
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.ITEM_FRAME || block.getType() == Material.ARMOR_STAND) {
            if (player.getGameMode() == GameMode.ADVENTURE) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPortal(final EntityPortalEvent e) {
        if (!(getInstance().getRepository().isGamePortals())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(final FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!(getInstance().getRepository().isGameSaturation())) {
                e.setCancelled(true);
                e.setFoodLevel(20);
            }
        }
    }

    @EventHandler
    public void onInteractSpring(final PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.SPONGE) {
                final Player player = e.getPlayer();
                if (!(UtilMath.offset(player.getLocation(), e.getClickedBlock().getLocation().add(0.5D, 1.5D, 0.5D)) > 0.6D)) {
                    if (getInstance().getRechargeManager().add(player, "Sponge", (8 * 100), false)) {
                        player.setVelocity(new Vector(0.0D, 1.8D, 0.0D));
                        player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0, 15);
                        player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, 19, 15);
                        player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, 19, 15);
                        player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, 19, 15);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player player = (Player) e.getEntity();
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (UtilBlock.getBlockUnder(player.getLocation()).getType() == Material.SPONGE || UtilBlock.getBlockUnder(player.getLocation()).getType() == Material.WOOL) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent e) {
        if (getInstance().getRepository().isFunThrowingPearl()) {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemFrameBreak(final HangingBreakByEntityEvent e) {
        if (e.getRemover() instanceof Player) {
            final Player player = (Player) e.getRemover();
            if (player.getGameMode() == GameMode.ADVENTURE) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onArmorStandManipulate(final PlayerArmorStandManipulateEvent e) {
        final Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.ADVENTURE) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent e) {
        final Player player = e.getPlayer();
        if (e.getRightClicked() instanceof ArmorStand || e.getRightClicked() instanceof ItemFrame) {
            if (player.getGameMode() == GameMode.ADVENTURE) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onArmorStandDeath(final EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            final Player player = (Player) e.getDamager();
            if (player.getGameMode() == GameMode.ADVENTURE) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onProjectileHit(final ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            final Arrow a = (Arrow) e.getEntity();
            a.remove();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCeatureSpawn(final CreatureSpawnEvent e) {
        if (!(getInstance().getRepository().isGameMobs())) {
            e.setCancelled(true);
            if (e.getEntity().getWorld().getEntities().size() != 0) {
                e.getEntity().getWorld().getEntities().forEach(Entity::remove);
            }
        }
    }
}