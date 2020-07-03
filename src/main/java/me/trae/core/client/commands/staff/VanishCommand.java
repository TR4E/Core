package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class VanishCommand extends Command {

    public VanishCommand(final Main instance) {
        super(instance, "vanish", new String[]{"v", "invisible"}, Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (args == null || args.length == 0) {
            getInstance().getTitleManager().sendActionBar(player, " ");
            getInstance().getClientUtilities().setVanished(player, !(getInstance().getEffectManager().isVanished(player)));
            UtilMessage.message(player, "Vanish", "Vanish Mode: " + (getInstance().getEffectManager().isVanished(player) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
            getInstance().getClientUtilities().messageStaff("Vanish", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + (getInstance().getEffectManager().isVanished(player) ? " is now Invisible." : " is no longer Invisible."), Rank.ADMIN, new UUID[]{player.getUniqueId()});
            return;
        }
        if (args.length == 1) {
            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }
            if (target == player) {
                getInstance().getTitleManager().sendActionBar(player, " ");
                getInstance().getClientUtilities().setVanished(player, !(getInstance().getEffectManager().isVanished(player)));
                UtilMessage.message(player, "Vanish", "Vanish Mode: " + (getInstance().getEffectManager().isVanished(player) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
                getInstance().getClientUtilities().messageStaff("Vanish", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + (getInstance().getEffectManager().isVanished(player) ? " is now Invisible." : " is no longer Invisible."), Rank.ADMIN, new UUID[]{player.getUniqueId()});
                return;
            }
            final Client targetC = getInstance().getClientUtilities().getOnlineClient(target.getUniqueId());
            if (targetC == null) {
                return;
            }
            if (!(player.isOp())) {
                if (targetC.getRank().ordinal() >= client.getRank().ordinal()) {
                    UtilMessage.message(player, "Vanish", "You cannot toggle Vanish mode for this Player!");
                    return;
                }
            }
            getInstance().getTitleManager().sendActionBar(target, " ");
            getInstance().getClientUtilities().setVanished(target, !(getInstance().getEffectManager().isVanished(target)));
            UtilMessage.message(target, "Vanish", "Vanish Mode: " + (getInstance().getEffectManager().isVanished(target) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
            getInstance().getClientUtilities().messageStaff("Vanish", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + (getInstance().getEffectManager().isVanished(target) ? " is now Invisible " : " is no longer Invisible ") + " by " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".", Rank.ADMIN, new UUID[]{target.getUniqueId()});
        }
    }

    @Override
    public void help(final Player player) {

    }
}