package io.github.altkat.authBB.Handlers;

import io.github.altkat.authBB.AuthBB;
import io.github.altkat.authBB.Variables;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Objects;

public class TeleportHandler {
    protected AuthBB plugin;
    protected ConfigurationSection section;

    public TeleportHandler(AuthBB plugin, String section){
        this.plugin = plugin;
        this.section = Variables.config.getConfigurationSection("Bungee");
    }

    public void teleportServer(Player player, String server){
        String targetServer = section.getString("server");
        Integer delay = section.getInt("delay");
        Boolean isenabled = section.getBoolean("enabled");
        String errorMessage = section.getString("error-command").replace("&","§").replace("%server_name%", targetServer);
        String successMessage = section.getString("success").replace("&", "§");
        String waitMessage = section.getString("wait").replace("&", "§");

        if(server == null){
            player.sendMessage(errorMessage);
        }
        if(!Objects.equals(server, targetServer)){
            player.sendMessage(errorMessage);
        }

        if(Variables.sending.contains(player)) {
            player.sendMessage(waitMessage);
            return;
        }
            Variables.sending.add(player);
            new BukkitRunnable(){
                @Override
                public void run(){
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(output);
                    try{
                        out.writeUTF("Connect");
                        out.writeUTF(server);
                        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
                        Variables.sending.remove(player);
                        player.sendMessage(successMessage);
                    }catch (Exception error){
                        error.printStackTrace();
                        Variables.sending.remove(player);
                        player.sendMessage(errorMessage);
                    }

                }
            }.runTaskLater(plugin, delay*20L);
    }
}
