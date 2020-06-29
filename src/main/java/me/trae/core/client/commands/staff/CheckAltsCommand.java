package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class CheckAltsCommand extends Command {

    public CheckAltsCommand(final Main instance) {
        super(instance, "checkalts", new String[]{"altcheck", "altsearch", "searchalts", "alts", "showalts"}, Rank.HEADMOD);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            help(player);
            return;
        }
        if (args.length == 1) {
            final Client target = getInstance().getClientUtilities().searchClient(player, args[0], true);
            if (target == null) {
                return;
            }
            if (!(player.isOp())) {
                if ((Bukkit.getPlayer(target.getUUID()) != null && Bukkit.getPlayer(target.getUUID()).isOp()) || (target.getRank().ordinal() >= getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).getRank().ordinal())) {
                    UtilMessage.message(player, "Alt Check", "You cannot alt check this player!");
                    return;
                }
            }
            UtilMessage.message(player, "Alts", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + (getInstance().getClientUtilities().getAltsOfClient(target).size() != 0 ? " has " + ChatColor.YELLOW + getInstance().getClientUtilities().getAltsOfClient(target).size() + ChatColor.GRAY + " alternative accounts that played this server." + "\n" + ChatColor.GRAY + "[" + getInstance().getClientUtilities().getAltsOfClient(target).stream().map(p -> ChatColor.YELLOW + p.getName()).collect(Collectors.joining(ChatColor.GRAY + ", ")) + ChatColor.GRAY + "]" : " does not have any alternative accounts that have joined this server."));
        }
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Alt Check", "Usage: " + ChatColor.AQUA + "/checkalts <player>");
    }
}