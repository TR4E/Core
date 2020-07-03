package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ClearChatCommand extends Command {

    public ClearChatCommand(final Main instance) {
        super(instance, "clearchat", new String[]{"cleanchat", "cc"}, Rank.ADMIN);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000; i++) {
                        Bukkit.getOnlinePlayers().stream().filter(o -> !(o.isOp() || getInstance().getClientUtilities().getOnlineClient(o.getUniqueId()).hasRank(Rank.MOD, false))).forEach(o -> o.sendMessage(" "));
                    }
                }
            }.runTaskLaterAsynchronously(getInstance(), 1L);
            Bukkit.getOnlinePlayers().stream().filter(o -> !(o.isOp() || getInstance().getClientUtilities().getOnlineClient(o.getUniqueId()).hasRank(Rank.MOD, false))).forEach(o -> UtilMessage.message(o, "Server", "The Chat has been cleared!"));
            Bukkit.getOnlinePlayers().stream().filter(o -> (o.isOp() || getInstance().getClientUtilities().getOnlineClient(o.getUniqueId()).hasRank(Rank.MOD, false))).forEach(o -> UtilMessage.message(o, "Server", "The Chat has been cleared by " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + "."));
        }
    }

    @Override
    public void help(final Player player) {

    }
}