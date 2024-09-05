package io.github.altkat.authBB.Commands;

import io.github.altkat.authBB.Handlers.Connections;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ServerCommand implements CommandExecutor {
    private final ConfigurationSection section;

    private final String onlyPlayers;
    private final String noPermission;
    private final String wrongUsage;
    private final String disabled;
    private final String serverNotFound;

    public ServerCommand(){
        this.section = Connections.config.getConfigurationSection("Proxy");
        this.onlyPlayers = ChatColor.translateAlternateColorCodes('&', section.getString("only-players"));
        this.noPermission = ChatColor.translateAlternateColorCodes('&', section.getString("no-permission"));
        this.wrongUsage = ChatColor.translateAlternateColorCodes('&', section.getString("wrong-usage-server"));
        this.disabled = ChatColor.translateAlternateColorCodes('&', section.getString("disabled"));
        this.serverNotFound = ChatColor.translateAlternateColorCodes('&', section.getString("server-not-found"));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("AuthBB.server")) {
            commandSender.sendMessage(noPermission);
            return true;
        }
        if(!section.getBoolean("enabled")){
            commandSender.sendMessage(disabled);
            return true;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(onlyPlayers);
            return true;
        }
        if(strings.length == 0){
            commandSender.sendMessage(wrongUsage);
            return true;
        }
        if(!(section.getStringList("servers").contains(strings[0]))){
            commandSender.sendMessage(serverNotFound);
            return true;
        }
        Connections.connectionHandler.connectServer(((Player) commandSender).getPlayer(), strings[0]);
        return true;

    }
}

