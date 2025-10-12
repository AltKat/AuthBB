package io.github.altkat.authBB.Commands;

import fr.xephi.authme.api.v3.AuthMeApi;
import io.github.altkat.authBB.AuthBB;
import io.github.altkat.authBB.Handlers.Connections;
import io.github.altkat.authBB.Handlers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendCommand implements CommandExecutor {
    protected AuthBB plugin;
    protected final AuthMeApi authMe;

    public SendCommand(AuthBB plugin){
        this.plugin = plugin;
        this.authMe = AuthMeApi.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!Connections.isProxyModeActive) {
            commandSender.sendMessage(MessageManager.DISABLED);
            return true;
        }

        if(!commandSender.hasPermission("AuthBB.send")){
            commandSender.sendMessage(MessageManager.NO_PERMISSION);
            return true;
        }

        if(strings.length < 2){
            commandSender.sendMessage(MessageManager.WRONG_USAGE_SEND);
            return true;
        }

        Player player = Bukkit.getPlayerExact(strings[0]);
        if(player == null){
            commandSender.sendMessage(MessageManager.PLAYER_NOT_FOUND);
            return true;
        }

        if(!Connections.config.getConfigurationSection("Proxy").getStringList("servers").contains(strings[1])){
            commandSender.sendMessage(MessageManager.SERVER_NOT_FOUND);
            return true;
        }

        if(!authMe.isAuthenticated(player)){
            commandSender.sendMessage(MessageManager.PLAYER_NOT_AUTHENTICATED);
        } else if(Connections.sending.contains(player)){
            commandSender.sendMessage(MessageManager.PLAYER_ALREADY_CONNECTING);
        } else {
            plugin.getLogger().info("Sending player " + player.getName() + " to server " + strings[1]);
            Connections.connectionHandler.connectServer(player, strings[1]);
            commandSender.sendMessage(MessageManager.SEND_SUCCESS_SENDER.replace("%player%", player.getName()).replace("%server%", strings[1]));
            player.sendMessage(MessageManager.SEND_SUCCESS_SENT);
        }

        return true;
    }
}