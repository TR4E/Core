package me.trae.core.module.update;

import me.trae.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class Updater extends BukkitRunnable {

    private final Map<UpdateType, Long> updateTypeHashMap = new HashMap<>();

    public Updater(final Main instance) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Updater.this.run();
                } catch (final Exception e) {
                    System.out.println("[Error]: Update Event");
                }
            }
        }.runTaskTimer(instance, 0, 1);
    }

    public void run() {
        for (final UpdateType updateType : UpdateType.values()) {
            if (!this.updateTypeHashMap.containsKey(updateType)) {
                this.updateTypeHashMap.put(updateType, System.currentTimeMillis());
            }
            if (this.updateTypeHashMap.get(updateType) + updateType.time < System.currentTimeMillis()) {
                Bukkit.getServer().getPluginManager().callEvent(new UpdateEvent(updateType));
                this.updateTypeHashMap.put(updateType, System.currentTimeMillis());
            }
        }
    }

    public enum UpdateType {
        TICK(0L),
        TICK_50(50L),
        FASTEST(125L),
        FASTER(250L),
        FAST(500L),
        SEC_01(1000L),
        SEC_02(2000L),
        SEC_05(5000L),
        SEC_10(10000L),
        SEC_30(30000L),
        SEC_45(45000L),
        MIN_01(60000L),
        MIN_02(120000L),
        MIN_10(600000L),
        MIN_15(960000L),
        MIN_30(1920000L),
        MIN_60(3840000L),
        MIN_120(7680000L);
        final long time;

        UpdateType(final long time) {
            this.time = time;
        }
    }
}