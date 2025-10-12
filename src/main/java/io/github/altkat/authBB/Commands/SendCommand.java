package io.github.altkat.authBB.Commands;

import fr.xephi.authme.api.v3.AuthMeApi;
import io.github.altkat.authBB.AuthBB;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendCommand implements CommandExecutor {
    private final AuthBB plugin;
    private final AuthMeApi authMe;

    public SendCommand(AuthBB plugin) {
        this.plugin = plugin;
        this.authMe = AuthMeApi.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!plugin.isProxyModeActive()) {
            commandSender.sendMessage(plugin.getMessageManager().DISABLED);
            return true;
        }

        if (!commandSender.hasPermission("AuthBB.send")) {
            commandSender.sendMessage(plugin.getMessageManager().NO_PERMISSION);
            return true;
        }

        if (strings.length < 2) {
            commandSender.sendMessage(plugin.getMessageManager().WRONG_USAGE_SEND);
            return true;
        }

        Player targetPlayer = Bukkit.getPlayerExact(strings[0]);
        if (targetPlayer == null) {
            commandSender.sendMessage(plugin.getMessageManager().PLAYER_NOT_FOUND);
            return true;
        }

        String targetServer = strings[1];
        if (!plugin.getConfig().getStringList("Proxy.servers").contains(targetServer)) {
            commandSender.sendMessage(plugin.getMessageManager().SERVER_NOT_FOUND);
            return true;
        }

        if (!authMe.isAuthenticated(targetPlayer)) {
            commandSender.sendMessage(plugin.getMessageManager().PLAYER_NOT_AUTHENTICATED);
        } else if (plugin.getConnectionHandler().isPlayerSending(targetPlayer)) {
            commandSender.sendMessage(plugin.getMessageManager().PLAYER_ALREADY_CONNECTING);
        } else {
            plugin.getLogger().info("Sending player " + targetPlayer.getName() + " to server " + targetServer);
            plugin.getConnectionHandler().connectServer(targetPlayer, targetServer);
            commandSender.sendMessage(plugin.getMessageManager().SEND_SUCCESS_SENDER.replace("%player%", targetPlayer.getName()).replace("%server%", targetServer));
            targetPlayer.sendMessage(plugin.getMessageManager().SEND_SUCCESS_SENT);
        }
        return true;
    }
}