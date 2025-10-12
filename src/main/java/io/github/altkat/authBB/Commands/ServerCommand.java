package io.github.altkat.authBB.Commands;

import io.github.altkat.authBB.AuthBB;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerCommand implements CommandExecutor {
    private final AuthBB plugin;

    public ServerCommand(AuthBB plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!plugin.isProxyModeActive()) {
            commandSender.sendMessage(plugin.getMessageManager().DISABLED);
            return true;
        }

        if (!commandSender.hasPermission("AuthBB.server")) {
            commandSender.sendMessage(plugin.getMessageManager().NO_PERMISSION);
            return true;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(plugin.getMessageManager().ONLY_PLAYERS);
            return true;
        }

        if (strings.length == 0) {
            commandSender.sendMessage(plugin.getMessageManager().WRONG_USAGE_SERVER);
            return true;
        }

        String targetServer = strings[0];
        if (!plugin.getConfig().getStringList("Proxy.servers").contains(targetServer)) {
            commandSender.sendMessage(plugin.getMessageManager().SERVER_NOT_FOUND);
            return true;
        }

        plugin.getConnectionHandler().connectServer((Player) commandSender, targetServer);
        return true;
    }
}