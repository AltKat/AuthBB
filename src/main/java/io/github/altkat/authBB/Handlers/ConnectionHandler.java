package io.github.altkat.authBB.Handlers;

import io.github.altkat.authBB.AuthBB;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConnectionHandler {
    protected AuthBB plugin;
    protected ConfigurationSection section;
    private final Map<Player, BukkitRunnable> tasks = new HashMap<>();
    private final Map<Player, Boolean> listenersRegistered = new HashMap<>();

    public ConnectionHandler(AuthBB plugin, String section){
        this.plugin = plugin;
        this.section = Connections.config.getConfigurationSection("Proxy");
    }

    public void connectServer(Player player, String server){
        Integer delay = section.getInt("delay");
        String errorMessage = section.getString("error-command").replace("&","§").replace("%server_name%", server);
        String successMessage = section.getString("success").replace("&", "§");
        String waitMessage = section.getString("wait").replace("&", "§");
        String wrongConfigPlayer = section.getString("wrong-configuration-player").replace("&", "§");
        String wrongConfigConsole = section.getString("wrong-configuration-console").replace("&", "§");

        if (Connections.sending.contains(player)) {
            player.sendMessage(waitMessage);
            return;
        }

        if (tasks.containsKey(player)) {
            tasks.get(player).cancel();
            tasks.remove(player);
        }


        if (listenersRegistered.getOrDefault(player, false)) {
            plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord");
            listenersRegistered.put(player, false);
        }

        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", (channel, player1, message) -> {
            if (!channel.equals("BungeeCord")) {
                return;
            }

            if (!player.equals(player1)) {
                return;
            }

            try {
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
                String subchannel = in.readUTF();
                if (subchannel.equals("GetServers")) {
                    String[] servers = in.readUTF().split(", ");
                    if (!Arrays.asList(servers).contains(server)) {
                        player.sendMessage(wrongConfigPlayer);
                        player.getServer().getConsoleSender().sendMessage(wrongConfigConsole);
                        player.resetTitle();
                        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord");
                        listenersRegistered.put(player, false);
                        return;
                    }

                    Connections.sending.add(player);
                    Connections.connectionTitle.sendTitle(player);
                    player.sendMessage(successMessage);
                    new BukkitRunnable(){
                        @Override
                        public void run(){
                            ByteArrayOutputStream output = new ByteArrayOutputStream();
                            DataOutputStream out = new DataOutputStream(output);
                            try{
                                out.writeUTF("Connect");
                                out.writeUTF(server);
                                player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
                                Connections.sending.remove(player);
                            } catch (Exception error){
                                error.printStackTrace();
                                Connections.sending.remove(player);
                                player.sendMessage(errorMessage);
                            }
                        }
                    }.runTaskLater(plugin, delay * 20L);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        listenersRegistered.put(player, true);

        new BukkitRunnable() {
            @Override
            public void run() {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(output);
                try {
                    out.writeUTF("GetServers");
                    player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
                } catch (Exception error) {
                    error.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
