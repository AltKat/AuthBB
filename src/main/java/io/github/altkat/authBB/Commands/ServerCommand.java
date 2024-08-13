package io.github.altkat.authBB.Commands;

import io.github.altkat.authBB.Variables;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ServerCommand implements CommandExecutor {
    protected ConfigurationSection section = Variables.config.getConfigurationSection("Bungee");
    String onlyPlayers = section.getString("only-players").replace("&", "§");
    String nopermission = section.getString("no-permission").replace("&", "§");
    String teleporting = section.getString("teleporting").replace("&", "§");
    String wrongUsage = section.getString("wrong-usage").replace("&", "§");
    String disabled = section.getString("disabled").replace("&", "§");
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(section.getBoolean("enabled")){

                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(onlyPlayers);
                }
                if (!commandSender.hasPermission("AuthBB.server") || !commandSender.hasPermission("AuthBB.admin")) {
                    commandSender.sendMessage(nopermission);
                    return true;
                }
                if(strings.length == 0){
                    commandSender.sendMessage(wrongUsage);
                    return true;
                }
                if (commandSender.hasPermission("AuthBB.server") || commandSender.hasPermission("AuthBB.admin")) {
                    commandSender.sendMessage(teleporting);
                    Variables.teleportHandler.teleportServer(((Player) commandSender).getPlayer(), strings[0]);
                    return true;
                }
            return true;
        }else{
            commandSender.sendMessage(disabled);
        }
        return true;
    }
}

