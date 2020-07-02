package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ObserverCommand extends Command {

    public ObserverCommand(final Main instance) {
        super(instance, "observer", new String[]{"observe", "obs"}, Rank.HEADMOD);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (args == null || args.length == 0) {
            if (client.isObserving()) {
                player.teleport(client.getObserverLocation());
                client.setObserverLocation(null);
                player.setGameMode(GameMode.SURVIVAL);
            } else {
                client.setObserverLocation(player.getLocation());
                player.setGameMode(GameMode.SPECTATOR);
            }
            UtilMessage.message(player, "Observer", "Observer Mode: " + (client.isObserving() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
            getInstance().getClientUtilities().messageStaff("Observer", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + (client.isObserving() ? " is now Observing." : "is no longer Observing.") + ChatColor.GRAY + ".", Rank.ADMIN, new UUID[]{player.getUniqueId()});
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            final Client targetC = getInstance().getClientUtilities().getOnlineClient(target.getUniqueId());
            if (targetC == null) {
                return;
            }
            if (target == player) {
                if (client.isObserving()) {
                    player.teleport(client.getObserverLocation());
                    client.setObserverLocation(null);
                    player.setGameMode(GameMode.SURVIVAL);
                } else {
                    client.setObserverLocation(player.getLocation());
                    player.setGameMode(GameMode.SPECTATOR);
                }
                UtilMessage.message(player, "Observer", "Observer Mode: " + (client.isObserving() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
                getInstance().getClientUtilities().messageStaff("Observer", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + (client.isObserving() ? " is now Observing." : "is no longer Observing.") + ChatColor.GRAY + ".", Rank.ADMIN, new UUID[]{player.getUniqueId()});
                return;
            }
            if (!(player.isOp())) {
                if (targetC.getRank().ordinal() >= client.getRank().ordinal()) {
                    UtilMessage.message(player, "Observer", "You cannot toggle Observer mode for this Player!");
                    return;
                }
            }
            if (targetC.isObserving()) {
                target.teleport(targetC.getObserverLocation());
                targetC.setObserverLocation(null);
                target.setGameMode(GameMode.SURVIVAL);
            } else {
                targetC.setObserverLocation(target.getLocation());
                target.setGameMode(GameMode.SPECTATOR);
            }
            UtilMessage.message(target, "Observer", "Observer Mode: " + (targetC.isObserving() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
            getInstance().getClientUtilities().messageStaff("Observer", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + (targetC.isObserving() ? " is now Observing" : "is no longer Observing") + ChatColor.GRAY + " by " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".", Rank.ADMIN, new UUID[]{target.getUniqueId()});
        }
    }

    @Override
    public void help(final Player player) {

    }
}