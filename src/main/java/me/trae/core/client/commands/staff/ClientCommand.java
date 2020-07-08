package me.trae.core.client.commands.staff;

import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.command.Command;
import me.trae.core.effect.Effect;
import me.trae.core.module.update.UpdateEvent;
import me.trae.core.module.update.Updater;
import me.trae.core.utility.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;

public class ClientCommand extends Command {

    private final Set<UUID> falling;

    public ClientCommand(final Main instance) {
        super(instance, "client", new String[]{}, Rank.PLAYER);
        this.falling = new HashSet<>();
    }

    @Override
    public void execute(final Player player, final String[] args) {
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (args == null || args.length == 0) {
            if (player.isOp() || client.hasRank(Rank.MOD, true)) {
                help(player);
            }
            return;
        }
        if (args[0].equalsIgnoreCase("admin")) {
            if (player.isOp() || client.hasRank(Rank.ADMIN, true)) {
                adminCommand(player);
            }
        } else if (args[0].equalsIgnoreCase("search")) {
            if (player.isOp() || client.hasRank(Rank.MOD, true)) {
                searchCommand(player, args);
            }
        } else if (args[0].equalsIgnoreCase("promote")) {
            if (player.isOp() || client.hasRank(Rank.OWNER, true)) {
                promoteCommand(player, args);
            }
        } else if (args[0].equalsIgnoreCase("demote")) {
            if (player.isOp() || client.hasRank(Rank.OWNER, true)) {
                demoteCommand(player, args);
            }
        } else {
            if (player.isOp() || client.hasRank(Rank.MOD, true)) {
                help(player);
            }
        }
    }

    @Override
    public void help(final Player player) {
        UtilMessage.message(player, "Client", "Command List:");
        if (player.isOp() || getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).hasRank(Rank.MOD, false)) {
            UtilMessage.message(player, ChatColor.AQUA + "/client search <client>" + ChatColor.GRAY + " - " + "Search a Client.");
        }
        if (player.isOp() || getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).hasRank(Rank.OWNER, false)) {
            UtilMessage.message(player, ChatColor.AQUA + "/client promote <client>" + ChatColor.GRAY + " - " + "Promote a Client.");
            UtilMessage.message(player, ChatColor.AQUA + "/client demote <client>" + ChatColor.GRAY + " - " + "Demote a Client.");
        }
        if (player.isOp() || getInstance().getClientUtilities().getOnlineClient(player.getUniqueId()).hasRank(Rank.ADMIN, false)) {
            UtilMessage.message(player, ChatColor.AQUA + "/client admin" + ChatColor.GRAY + " - " + "Toggle Client Admin.");
        }
    }

    private void adminCommand(final Player player) {
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        getInstance().getTitleManager().sendActionBar(player, " ");
        client.setAdministrating(!(client.isAdministrating()));
        UtilMessage.message(player, "Client", "Admin Mode: " + (client.isAdministrating() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
        getInstance().getClientUtilities().messageStaff("Client", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + (client.isAdministrating() ? " is now Administrating" : "is no longer Administrating") + ".", Rank.ADMIN, new UUID[]{player.getUniqueId()});
    }

    private void searchCommand(final Player player, final String[] args) {
        if (args.length == 1) {
            UtilMessage.message(player, "Client", "Usage: " + ChatColor.AQUA + "/client search <client>");
            return;
        }
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        final Client target = getInstance().getClientUtilities().searchClient(player, args[1], true);
        if (target == null) {
            return;
        }
        UtilMessage.message(player, "Client", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " Information:");
        UtilMessage.message(player, ChatColor.GREEN + "Profile: " + ChatColor.WHITE.toString() + ChatColor.UNDERLINE + "https://mine.ly/" + target.getName());
        UtilMessage.message(player, ChatColor.GREEN + "UUID: " + ChatColor.WHITE + target.getUUID());
        if (target.getOldName() != null) {
            UtilMessage.message(player, ChatColor.GREEN + "Previous Name: " + ChatColor.WHITE + target.getOldName());
        }
        if (!(getInstance().getGamerUtilities().getGamer(target.getUUID()).getIgnored().isEmpty())) {
            UtilMessage.message(player, ChatColor.GREEN + "Ignored: " + ChatColor.WHITE + getInstance().getClientUtilities().getIgnoredNames(target));
        }
        final List<String> ips = new ArrayList<>(target.getIPAddresses());
        if (Bukkit.getPlayer(target.getUUID()) != null) {
            UtilMessage.message(player, ChatColor.GREEN + "IP Address: " + ChatColor.WHITE + ((player.isOp() || client.hasRank(Rank.ADMIN, false)) ? UtilPlayer.getIP(Bukkit.getPlayer(target.getUUID())) : ChatColor.RED + "N/A"));
            ips.remove(UtilPlayer.getIP(Bukkit.getPlayer(target.getUUID())));
        }
        if (!(ips.isEmpty()) && (player.isOp() || client.hasRank(Rank.ADMIN, false))) {
            UtilMessage.message(player, ChatColor.GREEN + "IP Aliases: " + ChatColor.WHITE + ips);
        }
        UtilMessage.message(player, ChatColor.GREEN + "Rank: " + ChatColor.WHITE + UtilFormat.cleanString(target.getRank().getPrefix()));
        UtilMessage.message(player, ChatColor.GREEN + "First Joined: " + ChatColor.WHITE + (target.getFirstJoined() == 0 ? "Never" : UtilTime.getTime(System.currentTimeMillis() - target.getFirstJoined(), UtilTime.TimeUnit.BEST, 1)));
        if (Bukkit.getPlayer(target.getUUID()) == null) {
            UtilMessage.message(player, ChatColor.GREEN + "Last Online: " + ChatColor.WHITE + UtilTime.getTime(System.currentTimeMillis() - target.getLastOnline(), UtilTime.TimeUnit.BEST, 1));
        }
        if (Bukkit.getPlayer(target.getUUID()) != null) {
            if (Bukkit.getPlayer(target.getUUID()).isOp() || target.hasRank(Rank.ADMIN, false)) {
                UtilMessage.message(player, ChatColor.GREEN + "Admin Mode: " + ChatColor.WHITE + (target.isAdministrating() ? "Enabled" : "Disabled"));
            }
            if (client.isAdministrating()) {
                if (Bukkit.getPlayer(target.getUUID()).isOp() || target.hasRank(Rank.ADMIN, false) || getInstance().getEffectManager().hasGodMode(Bukkit.getPlayer(target.getUUID()))) {
                    UtilMessage.message(player, ChatColor.GREEN + "God Mode: " + ChatColor.WHITE + (getInstance().getEffectManager().hasGodMode(Bukkit.getPlayer(target.getUUID())) ? "Enabled" : "Disabled"));
                }
                if (Bukkit.getPlayer(target.getUUID()).isOp() | target.hasRank(Rank.HEADMOD, false)) {
                    UtilMessage.message(player, ChatColor.GREEN + "Observer Mode: " + ChatColor.WHITE + (target.isObserving() ? "Enabled" : "Disabled"));
                }
                if (Bukkit.getPlayer(target.getUUID()).isOp() || target.hasRank(Rank.MOD, false)) {
                    UtilMessage.message(player, ChatColor.GREEN + "Vanish Mode: " + ChatColor.WHITE + (getInstance().getEffectManager().isVanished(Bukkit.getPlayer(target.getUUID())) ? "Enabled" : "Disabled"));
                }
                UtilMessage.message(player, ChatColor.GREEN + "Fly Mode: " + ChatColor.WHITE + (Bukkit.getPlayer(target.getUUID()).getAllowFlight() ? "Enabled" + (Bukkit.getPlayer(target.getUUID()).isFlying() ? " (Flying)" : "") : "Disabled"));
            }
            UtilMessage.message(player, ChatColor.GREEN + "Gamemode: " + ChatColor.WHITE + UtilFormat.cleanString(Bukkit.getPlayer(target.getUUID()).getGameMode().name()));
        }
    }


    private void promoteCommand(final Player player, final String[] args) {
        if (args.length == 1) {
            UtilMessage.message(player, "Client", "Usage: " + ChatColor.AQUA + "/client promote <client>");
            return;
        }
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        final Client target = getInstance().getClientUtilities().searchClient(player, args[1], true);
        if (target == null) {
            return;
        }
        if (target.getRank() == Rank.OWNER) {
            UtilMessage.message(player, "Client", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " cannot be promoted any further.");
            return;
        }
        target.setRank(Rank.getRank(target.getRank().ordinal() + 1));
        getInstance().getClientRepository().updateRank(target);
        if (target.getRank() == Rank.ADMIN || target.getRank() == Rank.OWNER) {
            if (Bukkit.getPlayer(target.getUUID()) != null) {
                Bukkit.getOnlinePlayers().stream().filter(o -> getInstance().getEffectManager().isVanished(o)).forEach(o -> Bukkit.getPlayer(target.getUUID()).showPlayer(o));
            }
        }
        UtilMessage.broadcast("Client", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " promoted " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + target.getRank().getTag(true) + ChatColor.GRAY + ".");
    }

    private void demoteCommand(final Player player, final String[] args) {
        if (args.length == 1) {
            UtilMessage.message(player, "Client", "Usage: " + ChatColor.AQUA + "/client demote <client>");
            return;
        }
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        final Client target = getInstance().getClientUtilities().searchClient(player, args[1], true);
        if (target == null) {
            return;
        }
        if (target.getRank() == Rank.PLAYER) {
            UtilMessage.message(player, "Client", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " cannot be demoted any further.");
            return;
        }
        target.setRank(Rank.getRank(target.getRank().ordinal() - 1));
        getInstance().getClientRepository().updateRank(target);
        if (!(Bukkit.getPlayer(target.getUUID()).isOp())) {
            if (target.getRank() == Rank.HEADMOD) {
                if (Bukkit.getPlayer(target.getUUID()).getGameMode() == GameMode.CREATIVE) {
                    Bukkit.getPlayer(target.getUUID()).setGameMode(GameMode.SURVIVAL);
                    Bukkit.getPlayer(target.getUUID()).setAllowFlight(false);
                    Bukkit.getPlayer(target.getUUID()).setFlying(false);
                    falling.add(target.getUUID());
                } else if (Bukkit.getPlayer(target.getUUID()).getGameMode() == GameMode.SPECTATOR) {
                    if (!(target.isObserving())) {
                        Bukkit.getPlayer(target.getUUID()).teleport(Bukkit.getPlayer(target.getUUID()).getWorld().getHighestBlockAt(Bukkit.getPlayer(target.getUUID()).getLocation()).getLocation().add(0.0D, 1.0D, 0.0D));
                        Bukkit.getPlayer(target.getUUID()).setGameMode(GameMode.SURVIVAL);
                        Bukkit.getPlayer(target.getUUID()).setAllowFlight(false);
                        Bukkit.getPlayer(target.getUUID()).setFlying(false);
                    }
                }
                target.setAdministrating(false);
                if (Bukkit.getPlayer(target.getUUID()) != null) {
                    getInstance().getClientUtilities().setVanished(Bukkit.getPlayer(target.getUUID()), false);
                    Bukkit.getOnlinePlayers().stream().filter(o -> getInstance().getEffectManager().isVanished(o)).forEach(o -> Bukkit.getPlayer(target.getUUID()).hidePlayer(o));
                    getInstance().getEffectManager().removeEffect(Bukkit.getPlayer(target.getUUID()), Effect.EffectType.GOD_MODE);
                    getInstance().getTitleManager().sendActionBar(Bukkit.getPlayer(target.getUUID()), " ");
                }
            } else if (target.getRank() == Rank.MOD) {
                if (Bukkit.getPlayer(target.getUUID()) != null) {
                    Bukkit.getPlayer(target.getUUID()).setAllowFlight(false);
                    Bukkit.getPlayer(target.getUUID()).setFlying(false);
                    if (target.isObserving()) {
                        Bukkit.getPlayer(target.getUUID()).teleport(target.getObserverLocation());
                        target.setObserverLocation(null);
                        getInstance().getTitleManager().sendActionBar(Bukkit.getPlayer(target.getUUID()), " ");
                        Bukkit.getPlayer(target.getUUID()).setGameMode(GameMode.SURVIVAL);
                        return;
                    }
                    if (UtilBlock.getBlockUnder(Bukkit.getPlayer(target.getUUID()).getLocation()).getType() == Material.AIR) {
                        falling.add(target.getUUID());
                    }
                }
            } else if (!(target.hasRank(Rank.HELPER, false))) {
                target.setStaffChat(false);
            }
        }
        UtilMessage.broadcast("Client", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " demoted " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to " + target.getRank().getTag(true) + ChatColor.GRAY + ".");
    }

    @EventHandler
    public void onUpdate(final UpdateEvent e) {
        if (e.getUpdateType() == Updater.UpdateType.TICK_50) {
            final Iterator<UUID> it = falling.iterator();
            if (it.hasNext()) {
                final UUID next = it.next();
                if (Bukkit.getPlayer(next) != null) {
                    final Player player = Bukkit.getPlayer(next);
                    if (player != null) {
                        final Location loc = player.getLocation();
                        loc.setY(loc.getY() - 5);
                        if (loc.getBlock().getType() != Material.AIR) {
                            getInstance().getEffectManager().addEffect(player, Effect.EffectType.NO_FALL, 1000L);
                            it.remove();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        falling.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e) {
        falling.remove(e.getEntity().getUniqueId());
    }

    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent e) {
        falling.remove(e.getPlayer().getUniqueId());
    }
}