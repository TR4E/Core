package me.trae.core.client.commands;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MessageCommand extends Command {

    public MessageCommand(final Main instance) {
        super(instance, "message", new String[]{"msg", "tell", "whisper", "pm", "m"}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length <= 1) {
            help(player);
            return;
        }
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        final Player target = UtilPlayer.searchPlayer(player, args[0], true);
        if (target == null) {
            return;
        }
        if (target == player) {
            UtilMessage.message(player, "Message", "You cannot message yourself.");
            return;
        }
        final Client targetC = getInstance().getClientUtilities().getOnlineClient(target.getUniqueId());
        if (targetC == null) {
            return;
        }
        if (getInstance().getEffectManager().isVanished(target)) {
            UtilMessage.message(player, "Player Search", ChatColor.YELLOW + "0" + ChatColor.GRAY + " matches found [" + ChatColor.YELLOW + args[0] + ChatColor.GRAY + "]");
            return;
        }
        if (!(player.isOp() || client.hasRank(Rank.HELPER, false))) {
            if (getInstance().getGamerUtilities().getGamer(target.getUniqueId()).getIgnored().contains(player.getUniqueId())) {
                UtilMessage.message(player, ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "You" + ChatColor.DARK_AQUA + " -> " + ChatColor.AQUA + target.getName() + ChatColor.DARK_AQUA + "] " + ChatColor.GRAY + UtilFormat.getFinalArg(args, 1));
                return;
            }
        }
        UtilMessage.message(player, ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "You" + ChatColor.DARK_AQUA + " -> " + ChatColor.AQUA + target.getName() + ChatColor.DARK_AQUA + "] " + ChatColor.GRAY + UtilFormat.getFinalArg(args, 1));
        UtilMessage.message(target, ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + player.getName() + ChatColor.DARK_AQUA + " -> " + ChatColor.AQUA + "You" + ChatColor.DARK_AQUA + "] " + ChatColor.GRAY + UtilFormat.getFinalArg(args, 1));
        getInstance().getClientUtilities().messageAdmins(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + player.getName() + ChatColor.DARK_GREEN + " -> " + ChatColor.GREEN + target.getName() + ChatColor.DARK_GREEN + "] " + ChatColor.GRAY + UtilFormat.getFinalArg(args, 1), new UUID[]{player.getUniqueId(), target.getUniqueId()});
        getInstance().getGamerUtilities().getGamer(target.getUniqueId()).setReply(player.getUniqueId());
        getInstance().getGamerUtilities().getGamer(player.getUniqueId()).setReply(target.getUniqueId());
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Message", "Usage: " + ChatColor.AQUA + "/msg <player> <message>");
    }
}