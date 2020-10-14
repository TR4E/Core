package me.trae.core.client;

import me.trae.core.Main;
import me.trae.core.effect.Effect;
import me.trae.core.gamer.Gamer;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public final class ClientUtilities {

    private final Main instance;
    private final Map<UUID, Client> clients = new HashMap<>();
    private final Map<UUID, Client> onlineclients = new HashMap<>();

    public ClientUtilities(final Main instance) {
        this.instance = instance;
    }

    public void addClient(final Client c) {
        clients.put(c.getUUID(), c);
    }

    public void removeClient(final Client c) {
        clients.remove(c.getUUID());
    }

    public final Client getClient(final UUID uuid) {
        return clients.get(uuid);
    }

    public final Set<Client> getClients() {
        return new HashSet<>(clients.values());
    }

    public void addOnlineClient(final Client c) {
        onlineclients.put(c.getUUID(), c);
    }

    public void removeOnlineClient(final Client c) {
        onlineclients.remove(c.getUUID());
    }

    public final Client getOnlineClient(final UUID uuid) {
        return onlineclients.get(uuid);
    }

    public final Set<Client> getOnlineClients() {
        return new HashSet<>(onlineclients.values());
    }

    public final Set<Client> getOnlineStaffClients(final boolean showVanishPlayers) {
        final Set<Client> result = new HashSet<>();
        for (final Client client : getOnlineClients()) {
            if (client != null && Bukkit.getPlayer(client.getUUID()) != null) {
                if (client.hasRank(Rank.HELPER, false)) {
                    if (instance.getEffectManager().isVanished(Bukkit.getPlayer(client.getUUID())) && !(showVanishPlayers)) {
                        continue;
                    }
                    result.add(client);
                }
            }
        }
        return result;
    }

    public void messageStaff(final String prefix, final String message, final Rank minimumRank, final UUID[] ignore) {
        for (final Client client : getOnlineClients()) {
            if (client != null && Bukkit.getPlayer(client.getUUID()) != null) {
                if (Bukkit.getPlayer(client.getUUID()).isOp() || client.hasRank(minimumRank, false)) {
                    final Player player = Bukkit.getPlayer(client.getUUID());
                    if (player != null) {
                        if (ignore != null && Arrays.stream(ignore).anyMatch(i -> player.getUniqueId().equals(i))) {
                            continue;
                        }
                        UtilMessage.message(player, prefix, message);
                    }
                }
            }
        }
    }

    public void messageStaff(final String message, final Rank minimumRank, final UUID[] ignore) {
        for (final Client client : getOnlineClients()) {
            if (client != null && Bukkit.getPlayer(client.getUUID()) != null) {
                if (Bukkit.getPlayer(client.getUUID()).isOp() || client.hasRank(minimumRank, false)) {
                    final Player player = Bukkit.getPlayer(client.getUUID());
                    if (player != null) {
                        if (ignore != null && Arrays.stream(ignore).anyMatch(i -> player.getUniqueId().equals(i))) {
                            continue;
                        }
                        UtilMessage.message(player, message);
                    }
                }
            }
        }
    }

    public void messageAdmins(final String prefix, final String message, final UUID[] ignore) {
        for (final Client client : getOnlineClients()) {
            if (client != null && Bukkit.getPlayer(client.getUUID()) != null) {
                if (client.isAdministrating()) {
                    final Player player = Bukkit.getPlayer(client.getUUID());
                    if (player != null) {
                        if (ignore != null && Arrays.stream(ignore).anyMatch(i -> player.getUniqueId().equals(i))) {
                            continue;
                        }
                        UtilMessage.message(player, prefix, message);
                    }
                }
            }
        }
    }

    public void messageAdmins(final String message, final UUID[] ignore) {
        for (final Client client : getOnlineClients()) {
            if (client != null && Bukkit.getPlayer(client.getUUID()) != null) {
                if (client.isAdministrating()) {
                    final Player player = Bukkit.getPlayer(client.getUUID());
                    if (player != null) {
                        if (ignore != null && Arrays.stream(ignore).anyMatch(i -> player.getUniqueId().equals(i))) {
                            continue;
                        }
                        UtilMessage.message(player, message);
                    }
                }
            }
        }
    }

    public void soundStaff(final Sound sound, final Rank minimumRank, final UUID[] ignore) {
        for (final Client client : getOnlineClients()) {
            if (client != null && Bukkit.getPlayer(client.getUUID()) != null) {
                if (Bukkit.getPlayer(client.getUUID()).isOp() || client.hasRank(minimumRank, false)) {
                    final Player player = Bukkit.getPlayer(client.getUUID());
                    if (player != null) {
                        if (ignore != null && Arrays.stream(ignore).anyMatch(i -> player.getUniqueId().equals(i))) {
                            continue;
                        }
                        UtilPlayer.sound(player, sound);
                    }
                }
            }
        }
    }

    public void soundAdmins(final Sound sound, final UUID[] ignore) {
        for (final Client client : getOnlineClients()) {
            if (client != null && Bukkit.getPlayer(client.getUUID()) != null) {
                if (client.isAdministrating()) {
                    final Player player = Bukkit.getPlayer(client.getUUID());
                    if (player != null) {
                        if (ignore != null && Arrays.stream(ignore).anyMatch(i -> player.getUniqueId().equals(i))) {
                            continue;
                        }
                        UtilPlayer.sound(player, sound);
                    }
                }
            }
        }
    }

    public void setVanished(final Player player, final boolean vanished) {
        if (vanished) {
            instance.getEffectManager().addEffect(player, Effect.EffectType.VANISHED);
            for (final Player online : Bukkit.getOnlinePlayers()) {
                final Client client = getOnlineClient(online.getUniqueId());
                if (client != null) {
                    if (online.getUniqueId().equals(player.getUniqueId()) || (online.isOp() || client.hasRank(Rank.ADMIN, false))) {
                        continue;
                    }
                    online.hidePlayer(player);
                }
            }
        } else {
            instance.getEffectManager().removeEffect(player, Effect.EffectType.VANISHED);
            Bukkit.getOnlinePlayers().forEach(o -> o.showPlayer(player));
        }
    }

    public final Client searchClient(final Player player, final String name, final boolean inform) {
        if (getClients().stream().anyMatch(client -> client.getName().toLowerCase().equals(name.toLowerCase()))) {
            return getClients().stream().filter(client -> client.getName().toLowerCase().equals(name.toLowerCase())).findFirst().get();
        }
        final List<Client> clientList = getClients().parallelStream().filter(client -> client.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        if (UtilPlayer.searchPlayer(player, name, false) != null) {
            if (getClient(Objects.requireNonNull(UtilPlayer.searchPlayer(player, name, false)).getUniqueId()) != null) {
                if (!clientList.contains(getClient(Objects.requireNonNull(UtilPlayer.searchPlayer(player, name, false)).getUniqueId()))) {
                    clientList.add(getClient(Objects.requireNonNull(UtilPlayer.searchPlayer(player, name, false)).getUniqueId()));
                }
            }
        }
        if (clientList.size() == 1) {
            return clientList.get(0);
        } else if (inform) {
            UtilMessage.message(player, "Client Search", ChatColor.YELLOW.toString() + clientList.size() + ChatColor.GRAY + " matches found [" + ((clientList.size() == 0) ? ChatColor.YELLOW + name : clientList.stream().map(client -> ChatColor.YELLOW + client.getName()).collect(Collectors.joining(ChatColor.GRAY + ", "))) + ChatColor.GRAY + "].");
        }
        return null;
    }

    public final boolean isStaffOnline(final boolean includeOps) {
        for (final Client client : getOnlineClients()) {
            if (includeOps) {
                final Player player = Bukkit.getPlayer(client.getUUID());
                if (player != null && player.isOp()) {
                    return true;
                }
            }
            if (client.hasRank(Rank.HELPER, false)) {
                return true;
            }
        }
        return false;
    }

    public final Set<Client> getAltsOfClient(final Client client) {
        final Set<Client> result = new HashSet<>();
        for (final Client alt : getClients()) {
            if (alt.getUUID().equals(client.getUUID())) {
                continue;
            }
            for (final String ip : client.getIPAddresses()) {
                if (alt.getIPAddresses().contains(ip)) {
                    result.add(alt);
                }
            }
        }
        return result;
    }

    public final Set<String> getIgnoredNames(final Client client) {
        final Set<String> result = new HashSet<>();
        final Gamer gamer = instance.getGamerUtilities().getGamer(client.getUUID());
        if (gamer != null) {
            if (gamer.getIgnored().size() > 0) {
                for (final UUID uuid : gamer.getIgnored()) {
                    final Client target = getClient(uuid);
                    if (target != null) {
                        result.add(target.getName());
                    }
                }
            }
        }
        return result;
    }
}