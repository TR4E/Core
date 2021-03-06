package me.trae.core.world;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.module.CoreListener;
import me.trae.core.module.update.UpdateEvent;
import me.trae.core.module.update.Updater;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilMath;
import me.trae.core.utility.UtilMessage;
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
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
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
        if (e.getUpdateType() == Updater.UpdateType.TICK) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (getInstance().getEffectManager().isVanished(player)) {
                    getInstance().getTitleManager().sendActionBar(player, ChatColor.GREEN.toString() + ChatColor.BOLD + "You are invisible to other players!");
                } else if (getInstance().getEffectManager().hasGodMode(player)) {
                    getInstance().getTitleManager().sendActionBar(player, ChatColor.GOLD.toString() + ChatColor.BOLD + "You are currently in God Mode!");
                } else if (getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isObserving()) {
                    getInstance().getTitleManager().sendActionBar(player, ChatColor.AQUA.toString() + ChatColor.BOLD + "You are currently in Observer Mode!");
                } else if (getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating()) {
                    getInstance().getTitleManager().sendActionBar(player, ChatColor.RED.toString() + ChatColor.BOLD + "You are currently in Client Admin!");
                }
            }
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
                }
                if (getInstance().getRepository().isGameAlwaysNight()) {
                    if (world.getTime() != 13000L) {
                        world.setTime(13000L);
                    }
                }
            }
        }
    }

    // TODO: 5/07/2020 | Gotta fix Spider
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e) {
        e.setDeathMessage(null);
        final Player player = e.getEntity().getPlayer();
        if (player.getLastDamageCause() == null || player.getLastDamageCause().getCause() == null) {
            return;
        }
        if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.SUICIDE || player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
            return;
        }
        if (player.getLastDamageCause() == null) {
            return;
        }
        if (player.getKiller() != null) {
            if (player == player.getKiller()) {
                UtilMessage.broadcast("Death", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " has died.");
                return;
            }
        }
        String name = "";
        if (player.getLastDamageCause().getEntityType() != EntityType.PLAYER) {
            if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                final EntityDamageByEntityEvent entity = (EntityDamageByEntityEvent) player.getLastDamageCause();
                name = ChatColor.GRAY + "a " + ChatColor.YELLOW + UtilFormat.cleanString(entity.getDamager().getName());
            } else if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                final EntityDamageByEntityEvent entity = (EntityDamageByEntityEvent) player.getLastDamageCause();
                if (entity.getDamager() instanceof Arrow) {
                    final Arrow a = (Arrow) entity.getDamager();
                    if (a.getShooter() instanceof LivingEntity) {
                        name = ChatColor.GRAY + (a.getShooter() instanceof Player ? "" : "a ") + ChatColor.YELLOW + ((LivingEntity) a.getShooter()).getName();
                    }
                }
            } else if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                final EntityDamageByEntityEvent entity = (EntityDamageByEntityEvent) player.getLastDamageCause();
                name = ChatColor.GRAY + ((entity.getDamager() instanceof Creeper || entity.getDamager() instanceof Wither) ? "a " : "") + ChatColor.YELLOW + UtilFormat.cleanString(entity.getDamager().getName());
            } else if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                name = ChatColor.YELLOW + "Fire";
            } else {
                name = ChatColor.YELLOW + UtilFormat.cleanString(player.getLastDamageCause().getCause().name());
            }
            UtilMessage.broadcast("Death", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was killed by " + ChatColor.YELLOW + name + ChatColor.GRAY + ".");
            return;
        }
        if (player.getKiller() != null) {
            final Player killer = player.getKiller();
            if (killer != player) {
                if (getInstance().getGamerUtilities().getGamer(player.getUniqueId()) != null) {
                    if (!(player.getGameMode() == GameMode.CREATIVE || getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating() || getInstance().getEffectManager().isVanished(player))) {
                        getInstance().getGamerUtilities().getGamer(player.getUniqueId()).setDeaths(getInstance().getGamerUtilities().getGamer(player.getUniqueId()).getDeaths() + 1);
                    }
                }
                if (getInstance().getGamerUtilities().getGamer(killer.getUniqueId()) != null) {
                    if (!(killer.getGameMode() == GameMode.CREATIVE || getInstance().getClientUtilities().getOnlineClient(killer.getUniqueId()).isAdministrating() || getInstance().getEffectManager().isVanished(killer))) {
                        getInstance().getGamerUtilities().getGamer(killer.getUniqueId()).setKills(getInstance().getGamerUtilities().getGamer(killer.getUniqueId()).getKills() + 1);
                    }
                }
                UtilMessage.broadcast("Death", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " was killed by " + ChatColor.YELLOW + killer.getName() + ChatColor.GRAY + " with " + ChatColor.GREEN + UtilFormat.cleanString(killer.getInventory().getItemInHand().getType().name()) + ChatColor.GRAY + ".");
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
                player.getInventory().setItemInHand(getInstance().getItemManager().updateNames(new ItemStack(Material.IRON_DOOR, player.getInventory().getItemInHand().getAmount())));
                UtilMessage.message(player, "Game", "Please use " + ChatColor.YELLOW + "Iron Doors" + ChatColor.GRAY + " (You can right click to open them).");
            } else if (block.getType() == Material.TRAP_DOOR) {
                e.setCancelled(true);
                player.getInventory().setItemInHand(getInstance().getItemManager().updateNames(new ItemStack(Material.IRON_TRAPDOOR, player.getInventory().getItemInHand().getAmount())));
                UtilMessage.message(player, "Game", "Please use " + ChatColor.YELLOW + "Iron Trap Doors" + ChatColor.GRAY + " (You can right click to open them).");
            } else if (block.getType() == Material.BEDROCK || block.getType() == Material.BARRIER) {
                e.setCancelled(true);
                if (!(player.isOp() || client.hasRank(Rank.ADMIN, false))) {
                    player.getInventory().remove(block.getType());
                    getInstance().getClientUtilities().messageStaff("Staff Spy", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " tried to place " + ChatColor.GREEN + UtilFormat.cleanString(block.getType().name()) + ChatColor.GRAY + ".", Rank.MOD, new UUID[]{player.getUniqueId()});
                    getInstance().getClientUtilities().soundStaff(Sound.NOTE_PLING, Rank.MOD, new UUID[]{player.getUniqueId()});
                    getInstance().getClientUtilities().soundStaff(Sound.NOTE_PLING, Rank.MOD, new UUID[]{player.getUniqueId()});
                }
                UtilMessage.message(player, "Game", "You cannot place " + ChatColor.YELLOW + UtilFormat.cleanString(block.getType().name()) + ChatColor.GRAY + ".");
            } else if (block.getType() == Material.TNT) {
                if (getInstance().getRepository().isDisableTNT()) {
                    e.setCancelled(true);
                    UtilMessage.message(player, "Game", ChatColor.YELLOW + "TNT" + ChatColor.GRAY + " is currently disabled.");
                }
            } else if (block.getType() == Material.ENCHANTMENT_TABLE) {
                if (getInstance().getRepository().isGameEnchantments()) {
                    e.setCancelled(true);
                    UtilMessage.message(player, "Game", ChatColor.YELLOW + "Enchanting" + ChatColor.GRAY + " is currently disabled.");
                }
            } else if (block.getType() == Material.HOPPER) {
                if (getInstance().getRepository().isDisableHopper()) {
                    e.setCancelled(true);
                    UtilMessage.message(player, "Game", ChatColor.YELLOW + "Hoppers" + ChatColor.GRAY + " are currently disabled.");
                }
            } else if (block.getType() == Material.ANVIL) {
                if (getInstance().getRepository().isDisableAnvil()) {
                    e.setCancelled(true);
                    UtilMessage.message(player, "Game", ChatColor.YELLOW + "Anvils" + ChatColor.GRAY + " are currently disabled.");
                }
            } else if (block.getType() == Material.DROPPER) {
                if (getInstance().getRepository().isDisableDroppers()) {
                    e.setCancelled(true);
                    UtilMessage.message(player, "Game", ChatColor.YELLOW + "Droppers" + ChatColor.GRAY + " are currently disabled.");
                }
            } else if (block.getType() == Material.DISPENSER) {
                if (getInstance().getRepository().isDisableDispensers()) {
                    e.setCancelled(true);
                    UtilMessage.message(player, "Game", ChatColor.YELLOW + "Dispensers" + ChatColor.GRAY + " are currently disabled.");
                }
            } else if (block.getType() == Material.BREWING_STAND || block.getType() == Material.BREWING_STAND_ITEM) {
                if (getInstance().getRepository().isDisableBrewingStands()) {
                    e.setCancelled(true);
                    UtilMessage.message(player, "Game", ChatColor.YELLOW + "Brewing Stands" + ChatColor.GRAY + " are currently disabled.");
                }
            } else if (block.getType() == Material.ENDER_CHEST) {
                if (getInstance().getRepository().isDisableEnderChests()) {
                    e.setCancelled(true);
                    UtilMessage.message(player, "Game", ChatColor.YELLOW + "Ender Chests" + ChatColor.GRAY + " are currently disabled.");
                }
            } else if (block.getType() == Material.TRAPPED_CHEST) {
                if (getInstance().getRepository().isDisableTrappedChests()) {
                    e.setCancelled(true);
                    UtilMessage.message(player, "Game", ChatColor.YELLOW + "Trapped Chests" + ChatColor.GRAY + " are currently disabled.");
                }
            } else if (block.getType() == Material.MOB_SPAWNER) {
                if (getInstance().getRepository().isDisableMobSpawners()) {
                    e.setCancelled(true);
                    UtilMessage.message(player, "Game", ChatColor.YELLOW + "Mob Spawners" + ChatColor.GRAY + " is currently disabled.");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void addBlockBreakToGamer(final BlockBreakEvent e) {
        if (!(e.isCancelled())) {
            final Player player = e.getPlayer();
            if (player != null) {
                if (!(player.getGameMode() == GameMode.CREATIVE || getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating() || getInstance().getEffectManager().isVanished(player))) {
                    if (getInstance().getGamerUtilities().getGamer(player.getUniqueId()) != null) {
                        getInstance().getGamerUtilities().getGamer(player.getUniqueId()).setBlocksBroken(getInstance().getGamerUtilities().getGamer(player.getUniqueId()).getBlocksBroken() + 1);
                        getInstance().getGamerRepository().updateBlocksBroken(getInstance().getGamerUtilities().getGamer(player.getUniqueId()));
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void addBlockPlaceToGamer(final BlockPlaceEvent e) {
        if (!(e.isCancelled())) {
            final Player player = e.getPlayer();
            if (player != null) {
                if (!(player.getGameMode() == GameMode.CREATIVE || getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating() || getInstance().getEffectManager().isVanished(player))) {
                    if (getInstance().getGamerUtilities().getGamer(player.getUniqueId()) != null) {
                        getInstance().getGamerUtilities().getGamer(player.getUniqueId()).setBlocksPlaced(getInstance().getGamerUtilities().getGamer(player.getUniqueId()).getBlocksPlaced() + 1);
                        getInstance().getGamerRepository().updateBlocksPlaced(getInstance().getGamerUtilities().getGamer(player.getUniqueId()));
                    }
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

    @EventHandler
    public void onInventoryOpen(final InventoryOpenEvent e) {
        if (e.getPlayer() instanceof Player) {
            final Player player = (Player) e.getPlayer();
            if (player != null) {
                final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
                if (client != null) {
                    if (!(client.isAdministrating())) {
                        if (e.getInventory().getType() == InventoryType.ENCHANTING) {
                            if (!(getInstance().getRepository().isGameEnchantments())) {
                                e.setCancelled(true);
                                UtilMessage.message(player, "Game", ChatColor.YELLOW + "Enchanting" + ChatColor.GRAY + " is currently disabled.");
                            }
                        } else if (e.getInventory().getType() == InventoryType.ANVIL) {
                            if (getInstance().getRepository().isDisableAnvil()) {
                                e.setCancelled(true);
                                UtilMessage.message(player, "Game", ChatColor.YELLOW + "Anvils" + ChatColor.GRAY + " are currently disabled.");
                            }
                        } else if (e.getInventory().getType() == InventoryType.HOPPER) {
                            if (getInstance().getRepository().isDisableHopper()) {
                                e.setCancelled(true);
                                UtilMessage.message(player, "Game", ChatColor.YELLOW + "Hoppers" + ChatColor.GRAY + " are currently disabled.");
                            }
                        } else if (e.getInventory().getType() == InventoryType.DROPPER) {
                            if (getInstance().getRepository().isDisableDroppers()) {
                                e.setCancelled(true);
                                UtilMessage.message(player, "Game", ChatColor.YELLOW + "Droppers" + ChatColor.GRAY + " are currently disabled.");
                            }
                        } else if (e.getInventory().getType() == InventoryType.DISPENSER) {
                            if (getInstance().getRepository().isDisableDispensers()) {
                                e.setCancelled(true);
                                UtilMessage.message(player, "Game", ChatColor.YELLOW + "Dispensers" + ChatColor.GRAY + " are currently disabled.");
                            }
                        } else if (e.getInventory().getType() == InventoryType.BREWING) {
                            if (getInstance().getRepository().isDisableBrewingStands()) {
                                e.setCancelled(true);
                                UtilMessage.message(player, "Game", ChatColor.YELLOW + "Brewing Stands" + ChatColor.GRAY + " are currently disabled.");
                            }
                        } else if (e.getInventory().getType() == InventoryType.ENDER_CHEST) {
                            if (getInstance().getRepository().isDisableEnderChests()) {
                                e.setCancelled(true);
                                UtilMessage.message(player, "Game", ChatColor.YELLOW + "Ender Chests" + ChatColor.GRAY + " are currently disabled.");
                            }
                        }
                    }
                }
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
        e.getItem().getItemStack().setItemMeta(getInstance().getItemManager().updateNames(e.getItem().getItemStack()).getItemMeta());
    }

    @EventHandler
    public void onItemCraft(final PrepareItemCraftEvent e) {
        e.getInventory().setResult(getInstance().getItemManager().updateNames(e.getRecipe().getResult()));
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        final Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        if (!(getInstance().getRepository().isGameBreakCrops()) && e.getAction() == Action.PHYSICAL && (block.getType() == Material.SOIL || block.getType() == Material.CROPS || block.getType().name().toLowerCase().contains("seed"))) {
            e.setCancelled(true);
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.TNT && getInstance().getRepository().isDisableTNT() && !(getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating())) {
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
            final Player player = (Player) e.getEntity();
            if (!(getInstance().getRepository().isGameSaturation())) {
                e.setCancelled(true);
                player.setFoodLevel(20);
                return;
            }
            if (getInstance().getEffectManager().hasGodMode(player) || getInstance().getEffectManager().isVanished(player)) {
                e.setCancelled(true);
                player.setFoodLevel(20);
                return;
            }
            final int num = UtilMath.randomInt(0, 100);
            if (!(num > 50 && num < 60)) {
                e.setCancelled(true);
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
                        player.setVelocity(new Vector(0.0D, (getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating() ? 2.4D : 1.8D), 0.0D));
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
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            if (!(getInstance().getRepository().isGamePvP())) {
                e.setCancelled(true);
                UtilMessage.message((Player) e.getDamager(), "Game", "You cannot harm " + ChatColor.YELLOW + e.getEntity().getName() + ChatColor.GRAY + ".");
                return;
            }
        }
        if (e.getDamager() instanceof Player && (e.getEntity() instanceof ArmorStand || e.getEntity() instanceof ItemFrame)) {
            final Player player = (Player) e.getDamager();
            if (player != null) {
                if (player.getGameMode() == GameMode.ADVENTURE) {
                    e.setCancelled(true);
                }
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

    @EventHandler
    public void onBucketFill(final PlayerBucketFillEvent e) {
        final Player player = e.getPlayer();
        if (!(getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating())) {
            e.setCancelled(true);
            player.getInventory().setItemInHand(getInstance().getItemManager().updateNames(new ItemStack(Material.IRON_INGOT, player.getInventory().getItemInHand().getAmount() * 3)));
            UtilMessage.message(player, "Game", "Your " + ChatColor.YELLOW + "Bucket" + ChatColor.GRAY + " broke!");
        }
    }

    @EventHandler
    public void onBucketEmpty(final PlayerBucketEmptyEvent e) {
        final Player player = e.getPlayer();
        if (!(getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating())) {
            e.setCancelled(true);
            player.getInventory().setItemInHand(getInstance().getItemManager().updateNames(new ItemStack(Material.IRON_INGOT, player.getInventory().getItemInHand().getAmount() * 3)));
            UtilMessage.message(player, "Game", "Your " + ChatColor.YELLOW + "Bucket" + ChatColor.GRAY + " broke!");
        }
    }

    @EventHandler
    public void onItemSmelt(final FurnaceSmeltEvent e) {
        e.setResult(getInstance().getItemManager().updateNames(e.getResult()));
    }
}