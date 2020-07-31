package me.trae.core.world;

import me.trae.core.Main;
import me.trae.core.module.CoreListener;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

public final class ExplosionListener extends CoreListener {

    public ExplosionListener(final Main instance) {
        super(instance);
    }

    @EventHandler
    public void onEntityExplode(final EntityExplodeEvent e) {
        e.setCancelled(true);
        if (e.getEntity() != null && e.getEntity().getType() == EntityType.PRIMED_TNT) {
            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.EXPLODE, 2.0F, 1.0F);
            for (final Block block : e.blockList()) {
                if (block.getType() == Material.AIR || block.getType() == Material.BEDROCK) {
                    continue;
                }
                if (block.isLiquid()) {
                    block.setType(Material.AIR);
                }
                boolean broken = false;
                for (final BlockTransformation value : BlockTransformation.values()) {
                    if (new ItemStack(block.getType(), 1, block.getData()).equals(value.getOldBlock())) {
                        block.setType(value.getNewBlock().getType());
                        block.setData(value.getNewBlock().getData().getData());
                        broken = true;
                        break;
                    } else if (new ItemStack(block.getType(), 1, block.getData()).equals(value.getNewBlock())) {
                        block.breakNaturally();
                        broken = true;
                        break;
                    }
                    if (broken) {
                        continue;
                    }
                    if (block.getType().name().contains("CHEST") || block.getType() == Material.ANVIL) {
                        block.breakNaturally();
                    }
                }
                block.breakNaturally();
            }
        }
    }

    public enum BlockTransformation {

        STONE_BRICK(new ItemStack(Material.SMOOTH_BRICK, 1, (byte) 0), new ItemStack(Material.SMOOTH_BRICK, 1, (byte) 2)),
        NETHER_BRICK(new ItemStack(Material.NETHER_BRICK, 1, (byte) 0), new ItemStack(Material.NETHERRACK, 1, (byte) 0)),
        SANDSTONE(new ItemStack(Material.SANDSTONE, 1, (byte) 2), new ItemStack(Material.SANDSTONE, 1, (byte) 0)),
        RED_SANDSTONE(new ItemStack(Material.RED_SANDSTONE, 1, (byte) 2), new ItemStack(Material.RED_SANDSTONE, 1, (byte) 0)),
        QUARTZ(new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 0), new ItemStack(Material.QUARTZ_BLOCK, 1, (byte) 2)),
        PRISMARINE_01(new ItemStack(Material.PRISMARINE, 1, (byte) 2), new ItemStack(Material.PRISMARINE, 1, (byte) 1)),
        PRISMARINE_02(new ItemStack(Material.PRISMARINE, 1, (byte) 1), new ItemStack(Material.PRISMARINE, 1, (byte) 0));

        private final ItemStack oldBlock, newBlock;

        BlockTransformation(final ItemStack oldBlock, final ItemStack newBlock) {
            this.oldBlock = oldBlock;
            this.newBlock = newBlock;
        }

        public final ItemStack getOldBlock() {
            return oldBlock;
        }

        public final ItemStack getNewBlock() {
            return newBlock;
        }
    }
}
