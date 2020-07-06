package me.trae.core.client.commands;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilFormat;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReplyCommand extends Command {

    public ReplyCommand(final Main instance) {
        super(instance, "reply", new String[]{"r"}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            if (getInstance().getGamerUtilities().getGamer(player.getUniqueId()).getReply() == null) {
                UtilMessage.message(player, "Reply", "You have no one to reply to.");
                return;
            }
            help(player);
            UtilMessage.message(player, "Reply", "Your Reply Recipient: " + ChatColor.YELLOW + getInstance().getClientUtilities().getOnlineClient(getInstance().getGamerUtilities().getGamer(player.getUniqueId()).getReply()).getName());
            return;
        }
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (getInstance().getGamerUtilities().getGamer(player.getUniqueId()).getReply() == null) {
            UtilMessage.message(player, "Reply", "You have no one to reply to.");
            return;
        }
        final Player target = Bukkit.getPlayer(getInstance().getGamerUtilities().getGamer(player.getUniqueId()).getReply());
        if (target == null) {
            return;
        }
        if (getInstance().getEffectManager().isVanished(target)) {
            getInstance().getGamerUtilities().getGamer(player.getUniqueId()).setReply(null);
            UtilMessage.message(player, "Reply", "You have no one to reply to.");
            return;
        }
        if (!(player.isOp() || client.hasRank(Rank.HELPER, false))) {
            if (getInstance().getGamerUtilities().getGamer(target.getUniqueId()).getIgnored().contains(player.getUniqueId())) {
                UtilMessage.message(player, ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "You" + ChatColor.DARK_AQUA + " -> " + ChatColor.AQUA + target.getName() + ChatColor.DARK_AQUA + "] " + ChatColor.GRAY + UtilFormat.getFinalArg(args, 0));
                return;
            }
        }
        UtilMessage.message(player, ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "You" + ChatColor.DARK_AQUA + " -> " + ChatColor.AQUA + target.getName() + ChatColor.DARK_AQUA + "] " + ChatColor.GRAY + UtilFormat.getFinalArg(args, 0));
        UtilMessage.message(target, ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + player.getName() + ChatColor.DARK_AQUA + " -> " + ChatColor.AQUA + "You" + ChatColor.DARK_AQUA + "] " + ChatColor.GRAY + UtilFormat.getFinalArg(args, 0));
        getInstance().getClientUtilities().messageAdmins(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + player.getName() + ChatColor.DARK_GREEN + " -> " + ChatColor.GREEN + target.getName() + ChatColor.DARK_GREEN + "] " + ChatColor.GRAY + UtilFormat.getFinalArg(args, 0), new UUID[]{player.getUniqueId(), target.getUniqueId()});
        getInstance().getGamerUtilities().getGamer(target.getUniqueId()).setReply(player.getUniqueId());
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Reply", "Usage: " + ChatColor.AQUA + "/r <message>");
    }
}