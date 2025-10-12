package io.github.altkat.authBB.Handlers;

import io.github.altkat.authBB.AuthBB;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

public class ConnectionHandler implements PluginMessageListener {
    private final AuthBB plugin;
    private final ConfigurationSection proxySection;

    private final Map<UUID, String> pendingServerChecks = new HashMap<>();
    private final List<UUID> sending = new ArrayList<>();

    public ConnectionHandler(AuthBB plugin) {
        this.plugin = plugin;
        this.proxySection = plugin.getConfig().getConfigurationSection("Proxy");
    }

    public void connectServer(Player player, String server) {
        if (sending.contains(player.getUniqueId())) {
            player.sendMessage(plugin.getMessageManager().PLAYER_ALREADY_CONNECTING);
            return;
        }

        pendingServerChecks.put(player.getUniqueId(), server);

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(output);
            out.writeUTF("GetServers");
            player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not send GetServers plugin message", e);
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        UUID playerUUID = player.getUniqueId();
        if (!pendingServerChecks.containsKey(playerUUID)) {
            return;
        }

        String targetServer = pendingServerChecks.get(playerUUID);

        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
            String subchannel = in.readUTF();

            if (subchannel.equals("GetServers")) {
                String[] servers = in.readUTF().split(", ");
                pendingServerChecks.remove(playerUUID);

                if (!Arrays.asList(servers).contains(targetServer)) {
                    player.sendMessage(plugin.getMessageManager().SERVER_NOT_FOUND);
                    plugin.getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §c" + player.getName() + " tried to connect to a server ('" + targetServer + "') not found in the proxy's server list.");
                    player.resetTitle();
                    return;
                }
                sendPlayerToProxyServer(player, targetServer);
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Error processing plugin message from BungeeCord", e);
            pendingServerChecks.remove(playerUUID);
        }
    }

    private void sendPlayerToProxyServer(Player player, String server) {
        int delay = proxySection.getInt("delay", 3);
        String successMessage = proxySection.getString("success", "&aConnecting you to the server...").replace("&", "§");
        String errorMessage = proxySection.getString("error-command", "&cAn error occurred.").replace("&", "§");

        sending.add(player.getUniqueId());
        plugin.getConnectionTitle().sendTitle(player);
        player.sendMessage(successMessage);

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(output);
                    out.writeUTF("Connect");
                    out.writeUTF(server);
                    player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
                } catch (IOException error) {
                    plugin.getLogger().log(Level.SEVERE, "Could not send Connect plugin message for " + player.getName(), error);
                    player.sendMessage(errorMessage);
                } finally {
                    sending.remove(player.getUniqueId());
                }
            }
        }.runTaskLater(plugin, delay * 20L);
    }

    public boolean isPlayerSending(Player player) {
        return sending.contains(player.getUniqueId());
    }
}