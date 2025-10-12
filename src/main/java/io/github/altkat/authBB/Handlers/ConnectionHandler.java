package io.github.altkat.authBB.Handlers;

import io.github.altkat.authBB.AuthBB;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
public class ConnectionHandler implements PluginMessageListener {
    protected AuthBB plugin;
    protected ConfigurationSection section;
    private final Map<UUID, String> pendingServerChecks = new HashMap<>();


    public ConnectionHandler(AuthBB plugin, String section){
        this.plugin = plugin;
        this.section = Connections.config.getConfigurationSection("Proxy");
    }

    public void connectServer(Player player, String server){
        String waitMessage = section.getString("wait").replace("&", "§");

        if (Connections.sending.contains(player)) {
            player.sendMessage(waitMessage);
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

        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
            String subchannel = in.readUTF();

            if (subchannel.equals("GetServers")) {
                String[] servers = in.readUTF().split(", ");

                pendingServerChecks.remove(playerUUID);

                if (!Arrays.asList(servers).contains(targetServer)) {

                    String wrongConfigPlayer = section.getString("wrong-configuration-player").replace("&", "§");
                    String wrongConfigConsole = section.getString("wrong-configuration-console").replace("&", "§");
                    player.sendMessage(wrongConfigPlayer);
                    plugin.getServer().getConsoleSender().sendMessage(wrongConfigConsole);
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
        Integer delay = section.getInt("delay");
        String successMessage = section.getString("success").replace("&", "§");
        String errorMessage = section.getString("error-command").replace("&","§").replace("%server_name%", server);

        Connections.sending.add(player);
        Connections.connectionTitle.sendTitle(player);
        player.sendMessage(successMessage);

        new BukkitRunnable(){
            @Override
            public void run(){
                try{
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(output);
                    out.writeUTF("Connect");
                    out.writeUTF(server);
                    player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
                } catch (IOException error){
                    plugin.getLogger().log(Level.SEVERE, "Could not send Connect plugin message for " + player.getName(), error);
                    player.sendMessage(errorMessage);
                } finally {
                    Connections.sending.remove(player);
                }
            }
        }.runTaskLater(plugin, delay * 20L);
    }
}