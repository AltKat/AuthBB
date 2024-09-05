package io.github.altkat.authBB.Commands;

import fr.xephi.authme.api.v3.AuthMeApi;
import io.github.altkat.authBB.AuthBB;
import io.github.altkat.authBB.Handlers.Connections;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class SendCommand implements CommandExecutor {
    protected AuthBB plugin;
    protected ConfigurationSection section = Connections.config.getConfigurationSection("Proxy");
    protected final AuthMeApi authMe;

    public SendCommand(AuthBB plugin){
        this.plugin = plugin;
        this.authMe = AuthMeApi.getInstance();
    }

    private final String sendSuccessSender = ChatColor.translateAlternateColorCodes('&', section.getString("send-success-sender"));
    private final String sendSuccessSent = ChatColor.translateAlternateColorCodes('&', section.getString("send-success-sent"));
    private final String wrongUsage = ChatColor.translateAlternateColorCodes('&', section.getString("wrong-usage-send"));
    private final String playerNotFound = ChatColor.translateAlternateColorCodes('&', section.getString("player-not-found"));
    private final String noPermission = ChatColor.translateAlternateColorCodes('&', section.getString("no-permission"));
    private final String serverNotFound = ChatColor.translateAlternateColorCodes('&', section.getString("server-not-found"));
    private final String playerNotAuthenticated = ChatColor.translateAlternateColorCodes('&', section.getString("not-authenticated"));
    private final String playerAlreadyConnecting = ChatColor.translateAlternateColorCodes('&', section.getString("player-already-connecting"));
    private final String disabled = ChatColor.translateAlternateColorCodes('&', section.getString("disabled"));

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(section.getBoolean("enabled")){
            if(strings.length < 2){
                commandSender.sendMessage(wrongUsage);
                return true;
            }

            Player player = Bukkit.getPlayerExact(strings[0]);
            if(player == null){
                commandSender.sendMessage(playerNotFound);
                return true;
            }

            if(!section.getStringList("servers").contains(strings[1])){
                commandSender.sendMessage(serverNotFound);
                return true;
            }

            if(commandSender.hasPermission("AuthBB.send")){
                if(!authMe.isAuthenticated(player)){
                    commandSender.sendMessage(playerNotAuthenticated);
                }else if(Connections.sending.contains(player)){
                    commandSender.sendMessage(playerAlreadyConnecting);
                } else{
                    plugin.getLogger().info("Sending player " + player.getName() + " to server " + strings[1]);
                    Connections.connectionHandler.connectServer(player, strings[1]);
                    commandSender.sendMessage(sendSuccessSender.replace("%player%", player.getName()).replace("%server%", strings[1]).replace("&", "§"));
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
