package io.github.altkat.authBB.Commands;

import fr.xephi.authme.api.v3.AuthMeApi;
import io.github.altkat.authBB.AuthBB;
import io.github.altkat.authBB.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class SendCommand implements CommandExecutor {
    protected AuthBB plugin;
    protected ConfigurationSection section = Variables.config.getConfigurationSection("Bungee");
    protected final AuthMeApi authMe;
    public SendCommand(AuthBB plugin){
        this.plugin = plugin;
        this.authMe = AuthMeApi.getInstance();
    }

    String sendSuccessSender = section.getString("send-success-sender");
    String sendSuccessSent = section.getString("send-success-sent").replace("&", "§");
    String wrongUsage = section.getString("wrong-usage-send").replace("&", "§");
    String playerNotFound = section.getString("player-not-found").replace("&", "§");
    String noPermission = section.getString("no-permission").replace("&", "§");
    String serverNotFound = section.getString("server-not-found").replace("&", "§");
    String playerNotAuthenticated = section.getString("not-authenticated").replace("&", "§");
    String playerAlreadyTeleporting = section.getString("player-already-teleporting").replace("&", "§");
    String disabled = section.getString("disabled").replace("&", "§");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(section.getBoolean("enabled")){
            if(strings.length < 1){
                commandSender.sendMessage(wrongUsage);
                return true;
            }

            Player player = Bukkit.getPlayerExact(strings[0]);
            if(player == null){
                commandSender.sendMessage(playerNotFound);
                return true;
            }

            if(!strings[1].equals(section.getString("server"))){
                commandSender.sendMessage(serverNotFound);
                return true;
            }

            if(commandSender.hasPermission("AuthBB.send")){
                if(!authMe.isAuthenticated(player)){
                    commandSender.sendMessage(playerNotAuthenticated);
                }else if(Variables.sending.contains(player)){
                    commandSender.sendMessage(playerAlreadyTeleporting);
                } else{
                    Variables.teleportHandler.teleportServer(player, strings[1]);
                    commandSender.sendMessage((sendSuccessSender).replace("%player%", player.getName()).replace("%server%", strings[1]).replace("&", "§"));
                    player.sendMessage(sendSuccessSent);
                }
            }else{
                commandSender.sendMessage(noPermission);
            }
        }else{
            commandSender.sendMessage(disabled);
        }
        return true;
    }
}
