package me.trae.core.client;

import me.trae.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
}