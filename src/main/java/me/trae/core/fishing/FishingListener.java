package me.trae.core.fishing;

import me.trae.core.Main;
import me.trae.core.module.CoreListener;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FishingListener extends CoreListener {

    private final Set<UUID> lineIn;

    public FishingListener(final Main instance) {
        super(instance);
        this.lineIn = new HashSet<>();
    }

    @EventHandler
    public void onPlayerFish(final PlayerFishEvent e) {
        if (e.getCaught() != null && (e.getCaught().getType() == EntityType.ARMOR_STAND || e.getCaught().getType() == EntityType.ITEM_FRAME)) {
            e.setCancelled(true);
            return;
        }
        getInstance().getItemManager().setBiteTime(e.getHook(), (getInstance().getClientUtilities().getOnlineClient(e.getPlayer().getUniqueId()).isAdministrating() ? (e.getPlayer().getGameMode() == GameMode.CREATIVE ? 1 : 5) : (e.getPlayer().getGameMode() == GameMode.CREATIVE ? 10 : 15)));
        final Player player = e.getPlayer();
        if (e.getCaught() != null) {
            if (e.getCaught() instanceof LivingEntity) {
                final LivingEntity caught = (LivingEntity) e.getCaught();
                if (caught != null) {
                    caught.setVelocity(player.getLocation().getDirection().multiply((getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating() ? (player.getGameMode() == GameMode.CREATIVE ? -3.5 : -3) : (player.getGameMode() == GameMode.CREATIVE ? -2.5 : -1))));
                }
            }
            if (e.getHook().getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid()) {
                String name = UtilFormat.cleanString(e.getCaught().getType().name());
                if (e.getCaught() instanceof Item) {
                    name = getInstance().getItemManager().updateNames(((Item) e.getCaught()).getItemStack()).getItemMeta().getDisplayName();
                } else if (e.getCaught() instanceof LivingEntity) {
                    name = UtilFormat.cleanString(e.getCaught().getType().name());
                }
                UtilMessage.broadcast("Fishing", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " caught a " + ChatColor.GREEN + ChatColor.stripColor(name) + ChatColor.GRAY + ".");
            }
        }
    }
}