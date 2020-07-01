package me.trae.core.command;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import me.trae.core.Main;
import me.trae.core.client.Client;
import me.trae.core.client.Rank;
import me.trae.core.module.CoreListener;
import me.trae.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.UUID;

public class CommandCenter extends CoreListener {

    private final String[] allowedCommands = new String[]{"gamemode", "gm", "gms", "gmc", "gma", "gmsp"};

    public CommandCenter(final Main instance) {
        super(instance);
        ProtocolLibrary.getProtocolManager().addPacketListener(tabCompletion());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandChat(final PlayerCommandPreprocessEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        final Client client = getInstance().getClientUtilities().getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(player.getUniqueId().equals(UUID.fromString("213bae9b-bbe1-4839-a74b-a59da8743062")))) {
            if (e.getMessage().toLowerCase().startsWith("/op ") || e.getMessage().toLowerCase().startsWith("/deop ") || e.getMessage().equalsIgnoreCase("/op") || e.getMessage().equalsIgnoreCase("/deop")) {
                e.setCancelled(true);
                UtilMessage.message(player, "Permissions", "This requires Permission Rank [" + ChatColor.DARK_RED + "Owner" + ChatColor.GRAY + "].");
                return;
            }
        }
        if (!(player.isOp())) {
            if (e.getMessage().startsWith("/?")) {
                e.setMessage("/help");
            }
            if (e.getMessage().startsWith("/") && e.getMessage().contains(":") && !(e.getMessage().contains(" "))) {
                e.setCancelled(true);
                return;
            }
            if (e.getMessage().equalsIgnoreCase("/pl") || e.getMessage().toLowerCase().startsWith("/plugins")) {
                e.setCancelled(true);
                UtilMessage.message(player, ChatColor.WHITE + "Plugins (3): " + ChatColor.GREEN + "Core" + ChatColor.WHITE + ", " + ChatColor.GREEN + "WorldEdit" + ChatColor.WHITE + ", " + ChatColor.GREEN + "Buycraft");
                return;
            }
        }
        if (e.getMessage().equalsIgnoreCase("/x")) {
            e.setMessage("/client admin");
        }
        String cmd = e.getMessage().substring(1);
        String[] args = null;
        if (cmd.contains(" ")) {
            cmd = cmd.split(" ")[0];
            args = e.getMessage().substring(e.getMessage().indexOf(' ') + 1).split(" ");
        }
        if (cmd == null) {
            return;
        }
        final Command command = getInstance().getCommandManager().getCommand(cmd);
        if (command != null) {
            if (player.isOp() || client.hasRank(command.getRequiredRank(), true)) {
                e.setCancelled(true);
                command.execute(player, args);
                return;
            }
            e.setCancelled(true);
        } else {
            if (player.isOp() || client.hasRank(Rank.OWNER, false) || Arrays.stream(allowedCommands).anyMatch(cmd::equalsIgnoreCase)) {
                return;
            }
            e.setCancelled(true);
            UtilMessage.message(player, ChatColor.WHITE + "Unknown command. Type \"/help\" for help.");
        }
    }

    public final PacketAdapter tabCompletion() {
        return new PacketAdapter(getInstance(), PacketType.Play.Client.TAB_COMPLETE) {
            public void onPacketReceiving(final PacketEvent e) {
                if (e.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                    try {
                        final PacketContainer packet = e.getPacket();
                        final String message = packet.getSpecificModifier(String.class).read(0).toLowerCase().split(" ")[0].substring(1);
                        if (!(e.getPlayer().isOp() || getInstance().getClientUtilities().getOnlineClient(e.getPlayer().getUniqueId()).hasRank(Rank.HELPER, false))) {
                            if (getInstance().getCommandManager().getCommand(message) != null) {
                                if (getInstance().getClientUtilities().getOnlineClient(e.getPlayer().getUniqueId()).hasRank(getInstance().getCommandManager().getCommand(message).getRequiredRank(), false)) {
                                    return;
                                }
                            } else {
                                if (Arrays.stream(allowedCommands).anyMatch(message::equalsIgnoreCase)) {
                                    return;
                                }
                            }
                            e.setCancelled(true);
                        }
                    } catch (final FieldAccessException ex) {
                        System.out.println("[Packet Error]: Tab Completion");
                    }
                }
            }
        };
    }
}