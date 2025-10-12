package io.github.altkat.authBB.Commands;

import io.github.altkat.authBB.Handlers.Connections;
import io.github.altkat.authBB.Handlers.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ServerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!Connections.isProxyModeActive) {
            commandSender.sendMessage(MessageManager.DISABLED);
            return true;
        }

        if (!commandSender.hasPermission("AuthBB.server")) {
            commandSender.sendMessage(MessageManager.NO_PERMISSION);
            return true;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(MessageManager.ONLY_PLAYERS);
            return true;
        }
        if(strings.length == 0){
            commandSender.sendMessage(MessageManager.WRONG_USAGE_SERVER);
            return true;
        }
        if(!(Connections.config.getConfigurationSection("Proxy").getStringList("servers").contains(strings[0]))){
            commandSender.sendMessage(MessageManager.SERVER_NOT_FOUND);
            return true;
        }
        Connections.connectionHandler.connectServer(((Player) commandSender).getPlayer(), strings[0]);
        return true;

    }
}

