package io.github.altkat.authBB.Commands;

import io.github.altkat.authBB.Handlers.Connections;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ServerCommand implements CommandExecutor {
    protected ConfigurationSection section = Connections.config.getConfigurationSection("Bungee");
    String onlyPlayers = section.getString("only-players").replace("&", "§");
    String nopermission = section.getString("no-permission").replace("&", "§");
    String wrongUsage = section.getString("wrong-usage-server").replace("&", "§");
    String disabled = section.getString("disabled").replace("&", "§");
    String serverNotFound = section.getString("server-not-found").replace("&", "§");
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(section.getBoolean("enabled")){

                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(onlyPlayers);
                }
                if (!commandSender.hasPermission("AuthBB.server")) {
                    commandSender.sendMessage(nopermission);
                    return true;
                }
                if(strings.length == 0){
                    commandSender.sendMessage(wrongUsage);
                    return true;
                }
                if(!strings[0].equals(section.getString("server"))){
                    commandSender.sendMessage(serverNotFound);
                }else if (commandSender.hasPermission("AuthBB.server")) {
                    Connections.connectionHandler.connectServer(((Player) commandSender).getPlayer(), strings[0]);
                    return true;
                }

            return true;
        }else{
            commandSender.sendMessage(disabled);
        }
        return true;
    }
}

