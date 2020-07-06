package me.trae.core.fishing;

import me.trae.core.Main;
import me.trae.core.module.CoreListener;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingListener extends CoreListener {

    public FishingListener(final Main instance) {
        super(instance);
    }

    @EventHandler
    public void onPlayerFish(final PlayerFishEvent e) {
        if (e.getCaught() != null && e.getCaught().getType() == EntityType.ARMOR_STAND) {
            e.setCancelled(true);
            return;
        }
        final Player player = e.getPlayer();
        if (e.getCaught() != null) {
            if (e.getCaught() instanceof LivingEntity) {
                final LivingEntity caught = (LivingEntity) e.getCaught();
                if (caught != null) {
                    caught.setVelocity(player.getLocation().getDirection().multiply((getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating() ? (player.getGameMode() == GameMode.CREATIVE ? -3.5 : -3) : (player.getGameMode() == GameMode.CREATIVE ? -2.5 : -1))));
                }
            }
        }
    }
}