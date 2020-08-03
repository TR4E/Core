package me.trae.core.client;

import me.trae.core.Main;
import me.trae.core.effect.Effect;
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
    private final Set<Client> clients = new HashSet<>();
    private final Set<Client> onlineclients = new HashSet<>();

    public ClientUtilities(final Main instance) {
        this.instance = instance;
    }

    public void addClient(final Client c) {
        clients.add(c);
    }

    public void removeClient(final Client c) {
        clients.remove(c);
    }

    public final Client getClient(final UUID uuid) {
        return clients.stream().filter(c -> c.getUUID().equals(uuid)).findFirst().orElse(null);
    }

    public final Set<Client> getClients() {
        return clients;
    }

    public void addOnlineClient(final Client c) {
        onlineclients.add(c);
    }

    public void removeOnlineClient(final Client c) {
        onlineclients.remove(c);
    }

    public final Client getOnlineClient(final UUID uuid) {
        return onlineclients.stream().filter(o -> o.getUUID().equals(uuid)).findFirst().orElse(null);
    }

    public final Set<Client> getOnlineClients() {
        return onlineclients;
    }

    public final Set<Client> getOnlineStaffClients(final boolean showVanishPlayers) {
        final Set<Client> result = new HashSet<>();
        for (final Client client : onlineclients) {
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
        for (final Client client : onlineclients) {
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
        for (final Client client : onlineclients) {
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
        for (final Client client : onlineclients) {
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
        for (final Client client : onlineclients) {
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
        for (final Client client : onlineclients) {
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
        for (final Client client : onlineclients) {
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
            Bukkit.getOnlinePlayers().stream().filter(o -> !(o.getUniqueId().equals(player.getUniqueId())) && !(o.isOp() || getOnlineClient(o.getUniqueId()).hasRank(Rank.ADMIN, false))).forEach(o -> o.hidePlayer(player));
        } else {
            instance.getEffectManager().removeEffect(player, Effect.EffectType.VANISHED);
            Bukkit.getOnlinePlayers().forEach(o -> o.showPlayer(player));
        }
    }

    public final Client searchClient(final Player player, final String name, final boolean inform) {
        if (clients.stream().anyMatch(client -> client.getName().toLowerCase().equals(name.toLowerCase()))) {
            return clients.stream().filter(client -> client.getName().toLowerCase().equals(name.toLowerCase())).findFirst().get();
        }
        final List<Client> clientList = clients.parallelStream().filter(client -> client.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
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
        return (onlineclients.stream().anyMatch(c -> c.hasRank(Rank.HELPER, false)) || (includeOps && Bukkit.getOnlinePlayers().stream().anyMatch(Player::isOp)));
    }

    public final Set<Client> getAltsOfClient(final Client client) {
        return clients.stream().filter(a -> !(a.getUUID().equals(client.getUUID())) && a.getIPAddresses().stream().anyMatch(i -> client.getIPAddresses().contains(i))).collect(Collectors.toSet());
    }

    public final Set<String> getIgnoredNames(final Client client) {
        return instance.getGamerUtilities().getGamer(client.getUUID()).getIgnored().stream().map(i -> getClient(i).getName()).collect(Collectors.toSet());
    }
}