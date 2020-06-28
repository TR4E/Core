package me.trae.core.client;

import me.trae.core.Main;
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

    public void messageStaff(final String prefix, final String message, final Rank minimumRank, final UUID[] ignore) {
        onlineclients.stream().filter(c -> (Bukkit.getPlayer(c.getUUID()) != null && Arrays.stream(ignore).noneMatch(i -> i.equals(c.getUUID())) && c.hasRank(minimumRank, false))).forEach(c -> Bukkit.getPlayer(c.getUUID()).sendMessage(ChatColor.BLUE + prefix + "> " + ChatColor.GRAY + message));
    }

    public void messageStaff(final String message, final Rank minimumRank, final UUID[] ignore) {
        onlineclients.stream().filter(c -> (Bukkit.getPlayer(c.getUUID()) != null && Arrays.stream(ignore).noneMatch(i -> i.equals(c.getUUID()) && c.hasRank(minimumRank, false)))).forEach(c -> Bukkit.getPlayer(c.getUUID()).sendMessage(message));
    }

    public void messageAdmins(final String prefix, final String message, final UUID[] ignore) {
        onlineclients.stream().filter(c -> (Bukkit.getPlayer(c.getUUID()) != null && Arrays.stream(ignore).noneMatch(i -> i.equals(c.getUUID())) && c.isAdministrating())).forEach(c -> Bukkit.getPlayer(c.getUUID()).sendMessage(ChatColor.BLUE + prefix + "> " + ChatColor.GRAY + message));
    }

    public void messageAdmins(final String message, final UUID[] ignore) {
        onlineclients.stream().filter(c -> (Bukkit.getPlayer(c.getUUID()) != null && Arrays.stream(ignore).noneMatch(i -> i.equals(c.getUUID()) && c.isAdministrating()))).forEach(c -> Bukkit.getPlayer(c.getUUID()).sendMessage(message));
    }

    public void soundStaff(final Sound sound, final Rank minimumRank) {
        instance.getClientUtilities().getOnlineClients().stream().filter(c -> (Bukkit.getPlayer(c.getUUID()) != null && c.hasRank(minimumRank, false))).forEach(c -> Bukkit.getPlayer(c.getUUID()).playSound(Bukkit.getPlayer(c.getUUID()).getLocation(), sound, 1.0F, 1.0F));
    }

    public void soundAdmins(final Sound sound) {
        instance.getClientUtilities().getOnlineClients().stream().filter(c -> (Bukkit.getPlayer(c.getUUID()) != null && c.isAdministrating())).forEach(c -> Bukkit.getPlayer(c.getUUID()).playSound(Bukkit.getPlayer(c.getUUID()).getLocation(), sound, 1.0F, 1.0F));
    }

    public void setVanished(final Player player, final boolean vanished) {
        getOnlineClient(player.getUniqueId()).setVanished(vanished);
        if (vanished) {
            Bukkit.getOnlinePlayers().stream().filter(o -> !(o.getUniqueId().equals(player.getUniqueId()) && (o.isOp() || getOnlineClient(o.getUniqueId()).hasRank(Rank.ADMIN, false)))).forEach(o -> o.hidePlayer(player));
        } else {
            Bukkit.getOnlinePlayers().stream().filter(o -> !(o.getUniqueId()).equals(player.getUniqueId())).forEach(o -> o.showPlayer(player));
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
            UtilMessage.message(player, "Client Search", ChatColor.YELLOW.toString() + clientList.size() + ChatColor.GRAY + " matches found [" + ((clientList.size() == 0) ? ChatColor.YELLOW + name : clientList.stream().map(client -> ChatColor.YELLOW + client.getName()).collect(Collectors.joining(ChatColor.GRAY + ", "))) + ChatColor.GRAY + "]");
        }
        return null;
    }
}