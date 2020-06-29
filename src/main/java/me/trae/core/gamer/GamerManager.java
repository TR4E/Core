package me.trae.core.gamer;

import me.trae.core.Main;
import me.trae.core.module.CoreListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GamerManager extends CoreListener {

    public GamerManager(final Main instance) {
        super(instance);
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e) {
        final Player player = e.getEntity().getPlayer();
        if (player == null) {
            return;
        }
        final Player killer = e.getEntity().getKiller();
        if (killer == null) {
            return;
        }
        final Gamer playerGamer = getInstance().getGamerUtilities().getGamer(player.getUniqueId());
        if (playerGamer == null) {
            return;
        }
        final Gamer killerGamer = getInstance().getGamerUtilities().getGamer(killer.getUniqueId());
        if (killerGamer == null) {
            return;
        }
        playerGamer.setDeaths(playerGamer.getDeaths() + 1);
        getInstance().getGamerRepository().updateDeaths(playerGamer);
        killerGamer.setKills(killerGamer.getKills() + 1);
        getInstance().getGamerRepository().updateKills(killerGamer);
    }
}