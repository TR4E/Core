package me.trae.core.module.recharge;

import me.trae.core.Main;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilTime;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.Map.Entry;

public final class RechargeManager {

    private static final Map<UUID, List<Recharge>> cooldown = new HashMap<>();
    private final Main instance;

    public RechargeManager(final Main instance) {
        this.instance = instance;
        new BukkitRunnable() {
            @Override
            public void run() {
                for (final Iterator<Entry<UUID, List<Recharge>>> it = cooldown.entrySet().iterator(); it.hasNext(); ) {
                    final Entry<UUID, List<Recharge>> recharge = it.next();
                    if (recharge.getValue().stream().anyMatch(recharge1 -> recharge1.getRemaining() <= 0)) {
                        if (Bukkit.getPlayer(recharge.getKey()) != null) {
                            if (cooldown.get(recharge.getKey()).stream().filter(r -> r.getAbility().equalsIgnoreCase(recharge.getValue().stream().filter(g -> g.getRemaining() <= 0).findFirst().get().getAbility())).anyMatch(Recharge::isInform)) {
                                UtilMessage.message(Bukkit.getPlayer(recharge.getKey()), "Recharge", ChatColor.GREEN + recharge.getValue().stream().filter(rechargeData1 -> rechargeData1.getRemaining() <= 0).findFirst().get().getAbility() + ChatColor.GRAY + " has been recharged!");
                                cooldown.get(recharge.getKey()).forEach(recharge1 -> recharge1.setInform(false));
                            }
                        }
                        recharge.getValue().remove(recharge.getValue().stream().filter(recharge1 -> recharge1.getRemaining() <= 0).findFirst().orElse(null));
                    }
                    if (recharge.getValue().isEmpty()) {
                        it.remove();
                    }
                }
            }
        }.runTaskTimer(instance, 0L, 2L);
    }

    public final boolean add(final Player player, final String ability, final long duration, boolean inform) {
        if (player == null) {
            return false;
        }
        if (instance.getClientUtilities().getOnlineClient(player.getUniqueId()).isAdministrating()) {
            return true;
        }
        if (!(cooldown.containsKey(player.getUniqueId()))) {
            cooldown.put(player.getUniqueId(), new ArrayList<>());
            cooldown.get(player.getUniqueId()).add(new Recharge(ability, duration));
            return true;
        }
        if (cooldown.get(player.getUniqueId()).stream().noneMatch(recharge -> recharge.getAbility().equals(ability))) {
            cooldown.get(player.getUniqueId()).add(new Recharge(ability, duration));
            return true;
        }
        if (duration == 0) {
            inform = false;
        }
        if (cooldown.get(player.getUniqueId()).stream().filter(recharge -> recharge.getAbility().equals(ability)).anyMatch(recharge -> (recharge.getSeconds() + recharge.getSystime() - System.currentTimeMillis()) > 0)) {
            if (inform) {
                cooldown.get(player.getUniqueId()).stream().filter(r -> r.getAbility().equalsIgnoreCase(ability)).forEach(recharge -> recharge.setInform(true));
                final long time = cooldown.get(player.getUniqueId()).stream().filter(recharge -> recharge.getAbility().equals(ability)).findFirst().get().getRemaining();
                UtilMessage.message(player, "Recharge", "You cannot use " + ChatColor.GREEN + ability + ChatColor.GRAY + " for " + ChatColor.GREEN + UtilTime.convert(time, UtilTime.TimeUnit.BEST, 1) + " " + UtilTime.getTimeUnit2(time) + ChatColor.GRAY + ".");
            }
            return false;
        }
        return cooldown.get(player.getUniqueId()).stream().filter(recharge -> recharge.getAbility().equals(ability)).anyMatch(recharge -> recharge.getRemaining() > 0);
    }


    public void remove(final Player player, final String ability, final long seconds, final boolean inform) {
        if (player == null) {
            return;
        }
        for (final Entry<UUID, List<Recharge>> recharge : cooldown.entrySet()) {
            if (recharge.getValue().stream().anyMatch(recharge1 -> recharge1.getRemaining() <= 0)) {
                UtilMessage.message(player, "Recharge", ChatColor.GREEN + ability + ChatColor.GRAY + " has been recharged!");
            }
            recharge.getValue().remove(recharge.getValue().stream().filter(recharge1 -> recharge1.getRemaining() <= 0).findFirst().get());
        }
    }

    public final boolean isCooling(final Player player, final String ability, final boolean inform) {
        if (player == null) {
            return false;
        }
        if (instance.getClientUtilities().getClient(player.getUniqueId()).isAdministrating()) {
            return false;
        }
        if (!cooldown.containsKey(player.getUniqueId())) {
            return false;
        }
        if (cooldown.get(player.getUniqueId()).stream().noneMatch(rechargeData -> rechargeData.getAbility().equals(ability))) {
            return false;
        } else if (inform) {
            UtilMessage.message(player, "Recharge", "You cannot use " + ChatColor.GREEN + ability + ChatColor.GRAY + " for " + ChatColor.GREEN + UtilTime.convert(cooldown.get(player.getUniqueId()).stream().filter(rechargeData -> rechargeData.getAbility().equals(ability)).findFirst().get().getRemaining(), UtilTime.TimeUnit.BEST, 1) + " " + UtilTime.getTimeUnit2(cooldown.get(player.getUniqueId()).stream().filter(recharge -> recharge.getAbility().equals(ability)).findFirst().get().getRemaining()) + ChatColor.GRAY + ".");
        }
        return cooldown.get(player.getUniqueId()).stream().filter(rechargeData -> rechargeData.getAbility().equals(ability)).anyMatch(rechargeData -> rechargeData.getRemaining() > 0);
    }

    public final boolean removeCooldowns(final Player player) {
        if (cooldown.containsKey(player.getUniqueId())) {
            cooldown.keySet().remove(player.getUniqueId());
            return true;
        }
        return false;
    }

    public final List<Recharge> getRecharges(final Player player) {
        if (!(cooldown.isEmpty())) {
            if (cooldown.containsKey(player.getUniqueId())) {
                return cooldown.get(player.getUniqueId());
            }
        }
        return null;
    }
}