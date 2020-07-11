package me.trae.core.client.commands;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ClearInvCommand extends Command {

    private final Set<UUID> confirmation;

    public ClearInvCommand(final Main instance) {
        super(instance, "clearinventory", new String[]{"cleaninventory", "clearinv", "cleaninv", "clear", "ci"}, (instance.getRepository().isClearInvCommandAdminOnly() ? Rank.ADMIN : Rank.PLAYER));
        this.confirmation = new HashSet<>();
    }

    @Override
    public void execute(final Player player, final String[] args) {
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (args == null || args.length == 0) {
            if (client.isAdministrating() || confirmation.contains(player.getUniqueId())) {
                confirmation.remove(player.getUniqueId());
                UtilPlayer.clearInventory(player);
                UtilMessage.message(player, "Inventory", "Your Inventory has been cleared.");
                return;
            }
            if (getInstance().getRechargeManager().add(player, "Clear Inventory Command", (getInstance().getRepository().getClearInvCommandCooldown() * 1000L), true)) {
                confirmation.add(player.getUniqueId());
                UtilMessage.message(player, "Inventory", ChatColor.GOLD + "Repeat the command again if you want to clear your Inventory.");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        confirmation.remove(player.getUniqueId());
                    }
                }.runTaskLater(getInstance(), 1200L);
                return;
            }
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
                UtilPlayer.clearInventory(player);
                UtilMessage.message(player, "Inventory", "Your Inventory has been cleared.");
                return;
            }
            final Client targetC = getInstance().getClientUtilities().getOnlineClient(target.getUniqueId());
            if (targetC == null) {
                return;
            }
            if (!(player.isOp())) {
                if (targetC.getRank().ordinal() >= client.getRank().ordinal()) {
                    UtilMessage.message(player, "Inventory", "You cannot clear Inventory for this Player!");
                    return;
                }
            }
            UtilPlayer.clearInventory(target);
            UtilMessage.message(player, "Inventory", "You cleared " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "'s Inventory.");
            UtilMessage.message(target, "Inventory", "Your Inventory has been cleared by " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".");
            getInstance().getClientUtilities().messageStaff("Inventory", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " cleared " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + "'s Inventory.", Rank.OWNER, new UUID[]{player.getUniqueId(), target.getUniqueId()});
        }
    }

    @Override
    public void help(final Player player) {

    }
}