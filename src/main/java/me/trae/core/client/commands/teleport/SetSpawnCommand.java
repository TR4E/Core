package me.trae.core.client.commands.teleport;

import me.trae.core.Main;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SetSpawnCommand extends Command {

    public SetSpawnCommand(final Main instance) {
        super(instance, "setspawn", new String[]{}, Rank.OWNER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            if (Bukkit.getWorld(getInstance().getRepository().getServerWorld()) != null) {
                Bukkit.getWorld(getInstance().getRepository().getServerWorld()).setSpawnLocation((int) player.getLocation().getX(), (int) player.getLocation().getY(), (int) player.getLocation().getZ());
            } else {
                player.getWorld().setSpawnLocation((int) player.getLocation().getX(), (int) player.getLocation().getY(), (int) player.getLocation().getZ());
            }
            getInstance().getClientUtilities().soundStaff(Sound.PORTAL_TRIGGER, Rank.MOD, null);
            UtilMessage.message(player, "Spawn", "You have updated the " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + " Location. " + UtilLocation.locToString(player.getLocation()));
            getInstance().getClientUtilities().messageStaff("Spawn", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " updated the " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + " Location. " + UtilLocation.locToString(player.getLocation()), Rank.MOD, new UUID[]{player.getUniqueId()});
        }
    }

    @Override
    public void help(final Player player) {

    }
}