package me.trae.core.client.commands;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class ListCommand extends Command {

    public ListCommand(final Main instance) {
        super(instance, "list", new String[]{"onlineplayers", "playersonline", "online", "players"}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            UtilMessage.message(player, "List", "There is currently " + ChatColor.YELLOW + Bukkit.getOnlinePlayers().stream().filter(player::canSee).count() + ChatColor.GRAY + " out of the maximum of " + ChatColor.YELLOW + getInstance().getRepository().getMaxPlayerSlots() + ChatColor.GRAY + " players online.");
            if (getInstance().getClientUtilities().isStaffOnline()) {
                UtilMessage.message(player, "[" + ChatColor.YELLOW.toString() + getInstance().getClientUtilities().getOnlineClients().stream().filter(c -> (Bukkit.getPlayer(c.getUUID()) != null && player.canSee(Bukkit.getPlayer(c.getUUID()))) && c.hasRank(Rank.HELPER, false)).map(c -> c.getRank().getColor().toString() + ChatColor.BOLD + c.getName()).collect(Collectors.joining(ChatColor.GRAY + ", ")) + ChatColor.GRAY + "]");
            }
        }
    }

    @Override
    public void help(final Player player) {

    }
}