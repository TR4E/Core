package me.trae.core.client.commands;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.module.update.UpdateEvent;
import me.trae.core.module.update.Updater;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.*;

public class ReportCommand extends Command {

    private final Map<UUID, Data> map;

    public ReportCommand(final Main instance) {
        super(instance, "report", new String[]{}, Rank.PLAYER);
        this.map = new HashMap<>();
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
            return;
        }
        final Player target = UtilPlayer.searchPlayer(player, args[0], true);
        if (target == null) {
            return;
        }
        if (target == player) {
            UtilMessage.message(player, "Report", "You cannot report yourself.");
            return;
        }
        if (!(player.isOp())) {
            if (getInstance().getClientUtilities().getOnlineClient(target.getUniqueId()).hasRank(Rank.ADMIN, false)) {
                UtilMessage.message(player, "Report", "You cannot report this Player!");
                return;
            }
            if (getInstance().getEffectManager().isVanished(player)) {
                UtilMessage.message(player, "Player Search", ChatColor.YELLOW + "0" + ChatColor.GRAY + " matches found [" + ChatColor.YELLOW + args[0] + ChatColor.GRAY + "]");
                return;
            }
        }
        if (args.length == 1) {
            help(player);
            return;
        }
        if (!(getInstance().getClientUtilities().isStaffOnline(true))) {
            UtilMessage.message(player, "Report", "There are currently no staff online to receive this message.");
            return;
        }
        if (map.containsKey(player.getUniqueId())) {
            if (map.get(player.getUniqueId()).getReported().contains(target.getUniqueId())) {
                UtilMessage.message(player, "Report", "You have already reported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + ".");
                return;
            }
        }
        map.put(player.getUniqueId(), new Data(new HashSet<>(Collections.singleton(target.getUniqueId())), (System.currentTimeMillis() + 10000L)));
        UtilMessage.message(player, "Report", "You reported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " for " + ChatColor.GREEN + UtilFormat.getFinalArg(args, 1) + ChatColor.GRAY + ".");
        getInstance().getClientUtilities().messageStaff("Report", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " reported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " for " + ChatColor.GREEN + UtilFormat.getFinalArg(args, 1) + ChatColor.GRAY + ".", Rank.HELPER, new UUID[]{player.getUniqueId()});
        getInstance().getClientUtilities().soundStaff(Sound.NOTE_PLING, Rank.HELPER, new UUID[]{player.getUniqueId()});
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Report", "Usage: " + ChatColor.AQUA + "/report <player> <reason>");
    }

    @EventHandler
    public void onUpdate(final UpdateEvent e) {
        if (e.getUpdateType() == Updater.UpdateType.TICK_50) {
            final Iterator<UUID> it = map.keySet().iterator();
            if (it.hasNext()) {
                final UUID next = it.next();
                if (map.get(next).getReported() != null) {
                    if (map.get(next).hasExpired()) {
                        it.remove();
                    }
                }
            }
        }
    }
}

class Data {

    private final Set<UUID> reported;
    private final long duration;

    public Data(final Set<UUID> reported, final long duration) {
        this.reported = reported;
        this.duration = duration;
    }

    public final Set<UUID> getReported() {
        return reported;
    }

    public final long getDuration() {
        return duration;
    }

    public final boolean hasExpired() {
        return duration <= System.currentTimeMillis();
    }
}