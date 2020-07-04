package me.trae.core.client.commands;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public class SuicideCommand extends Command {

    public SuicideCommand(final Main instance) {
        super(instance, "suicide", new String[]{"kill", "kms"}, (instance.getRepository().isSuicideCommandAdminOnly() ? Rank.ADMIN : Rank.PLAYER));
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            if (getInstance().getRechargeManager().add(player, "Suicide Command", (getInstance().getRepository().getSuicideCommandCooldown() * 1000L), true)) {
                player.setHealth(0);
                final EntityDamageEvent ev = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1000);
                Bukkit.getPluginManager().callEvent(ev);
                ev.getEntity().setLastDamageCause(ev);
                UtilMessage.message(player, "Suicide", "You committed suicide.");
                if (!(getInstance().getEffectManager().isVanished(player))) {
                    UtilMessage.broadcast("Death", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " committed suicide.", new UUID[]{player.getUniqueId()});
                }
                return;
            }
            return;
        }
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(player.isOp() || client.hasRank(Rank.ADMIN, true))) {
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            if (target == player) {
                player.setHealth(0);
                final EntityDamageEvent ev = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1000);
                Bukkit.getPluginManager().callEvent(ev);
                ev.getEntity().setLastDamageCause(ev);
                player.setLastDamageCause(new EntityDamageEvent(null, EntityDamageEvent.DamageCause.SUICIDE, 1000.0));
                UtilMessage.message(player, "Suicide", "You committed suicide.");
                if (!(getInstance().getEffectManager().isVanished(player))) {
                    UtilMessage.broadcast("Death", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " committed suicide.", new UUID[]{player.getUniqueId()});
                }
                return;
            }
            final Client targetC = getInstance().getClientUtilities().getOnlineClient(target.getUniqueId());
            if (targetC == null) {
                return;
            }
            if (!(player.isOp())) {
                if (targetC.getRank().ordinal() >= client.getRank().ordinal()) {
                    UtilMessage.message(player, "Suicide", "You cannot kill this Player!");
                    return;
                }
            }
            target.setHealth(0);
            final EntityDamageEvent ev = new EntityDamageEvent(target, EntityDamageEvent.DamageCause.SUICIDE, 1000);
            Bukkit.getPluginManager().callEvent(ev);
            ev.getEntity().setLastDamageCause(ev);
            UtilMessage.message(player, "Suicide", "You killed " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
            UtilMessage.message(target, "Suicide", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " killed you!");
            if (!(getInstance().getEffectManager().isVanished(target))) {
                UtilMessage.broadcast("Death", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " committed suicide.", new UUID[]{player.getUniqueId(), target.getUniqueId()});
            }
        }
    }

    @Override
    public void help(final Player player) {

    }
}