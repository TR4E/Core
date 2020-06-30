package me.trae.core.client.commands.teleport;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpawnCommand extends Command {

    public SpawnCommand(final Main instance) {
        super(instance, "spawn", new String[]{}, (instance.getRepository().isSpawnCommandAdminOnly() ? Rank.ADMIN : Rank.PLAYER));
    }

    @Override
    public void execute(final Player player, final String[] args) {
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (args == null || args.length == 0) {
            player.teleport(Bukkit.getWorld(getInstance().getRepository().getServerWorld()).getSpawnLocation());
            UtilPlayer.sound(player, Sound.ENDERMAN_TELEPORT);
            UtilMessage.message(player, "Spawn", "You teleported to Spawn.");
            getInstance().getClientUtilities().messageAdmins("Spawn", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported to " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
            return;
        }
        if (!(player.isOp() || client.hasRank(Rank.ADMIN, true))) {
            return;
        }
        final Player target = UtilPlayer.searchPlayer(player, args[0], true);
        if (target == null) {
            return;
        }
        if (target == player) {
            player.teleport(Bukkit.getWorld(getInstance().getRepository().getServerWorld()).getSpawnLocation());
            UtilPlayer.sound(player, Sound.ENDERMAN_TELEPORT);
            UtilMessage.message(player, "Spawn", "You teleported to Spawn.");
            getInstance().getClientUtilities().messageAdmins("Spawn", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported to " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
            return;
        }
        target.teleport(Bukkit.getWorld(getInstance().getRepository().getServerWorld()).getSpawnLocation());
        UtilPlayer.sound(target, Sound.ENDERMAN_TELEPORT);
        UtilMessage.message(target, "Spawn", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported you to Spawn!");
        UtilMessage.message(player, "Spawn", "You teleported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to Spawn.");
        getInstance().getClientUtilities().messageAdmins("Spawn", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " teleported " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId(), target.getUniqueId()});
    }

    @Override
    public void help(final Player player) {

    }
}