package io.github.altkat.authBB.Commands;

import fr.xephi.authme.api.v3.AuthMeApi;
import io.github.altkat.authBB.AuthBB;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AuthBBCommand implements CommandExecutor {
    private final AuthBB plugin;
    private final AuthMeApi authMe;

    public AuthBBCommand(AuthBB plugin) {
        this.plugin = plugin;
        this.authMe = AuthMeApi.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§b/authbb help");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "help":
                return handleHelpCommand(sender);
            case "reload":
                return handleReloadCommand(sender);
            case "server":
                return handleServerCommand(sender, args);
            case "send":
                return handleSendCommand(sender, args);
            default:
                sender.sendMessage(plugin.getMessageManager().WRONG_USAGE_SERVER.replace("/server servername", "/authbb help"));
                return true;
        }
    }

    private boolean handleHelpCommand(CommandSender sender) {
        if (sender.hasPermission("AuthBB.admin")) {
            sender.sendMessage("§b==========[ AuthBossBar Admin HELP ]==========");
            sender.sendMessage("  §f/authbb server <server>: Connect to a lobby server.");
            sender.sendMessage("  §f/authbb send <player> <server>: Send a player to a lobby server.");
            sender.sendMessage("  §f/authbb reload: Reloads the configuration file.");
            sender.sendMessage("  §f/authbb help: Shows this help page.");
        } else {
            sender.sendMessage("§b==========[ AuthBossBar HELP ]==========");
            sender.sendMessage("  §f/authbb server <server>: Connect to a lobby server.");
            sender.sendMessage("  §f/authbb help: Shows this help page.");
        }
        return true;
    }

    private boolean handleReloadCommand(CommandSender sender) {
        if (!sender.hasPermission("AuthBB.admin")) {
            sender.sendMessage(plugin.getMessageManager().NO_PERMISSION);
            return true;
        }
        plugin.reload();
        sender.sendMessage(ChatColor.GREEN + "AuthBB configuration has been reloaded.");
        return true;
    }

    private boolean handleServerCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("AuthBB.server")) {
            sender.sendMessage(plugin.getMessageManager().NO_PERMISSION);
            return true;
        }
        if (!plugin.isProxyModeActive()) {
            sender.sendMessage(plugin.getMessageManager().DISABLED);
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageManager().ONLY_PLAYERS);
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(plugin.getMessageManager().WRONG_USAGE_SERVER.replace("servername", "<server>"));
            return true;
        }
        String targetServer = args[1];
        if (!plugin.getConfig().getStringList("Proxy.servers").contains(targetServer)) {
            sender.sendMessage(plugin.getMessageManager().SERVER_NOT_FOUND);
            return true;
        }
        plugin.getConnectionHandler().connectServer((Player) sender, targetServer);
        return true;
    }

    private boolean handleSendCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("AuthBB.send")) {
            sender.sendMessage(plugin.getMessageManager().NO_PERMISSION);
            return true;
        }
        if (!plugin.isProxyModeActive()) {
            sender.sendMessage(plugin.getMessageManager().DISABLED);
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage(plugin.getMessageManager().WRONG_USAGE_SEND.replace("(player) (server)", "<player> <server>"));
            return true;
        }
        Player targetPlayer = Bukkit.getPlayerExact(args[1]);
        if (targetPlayer == null) {
            sender.sendMessage(plugin.getMessageManager().PLAYER_NOT_FOUND);
            return true;
        }
        String targetServer = args[2];
        if (!plugin.getConfig().getStringList("Proxy.servers").contains(targetServer)) {
            sender.sendMessage(plugin.getMessageManager().SERVER_NOT_FOUND);
            return true;
        }
        if (!authMe.isAuthenticated(targetPlayer)) {
            sender.sendMessage(plugin.getMessageManager().PLAYER_NOT_AUTHENTICATED);
        } else if (plugin.getConnectionHandler().isPlayerSending(targetPlayer)) {
            sender.sendMessage(plugin.getMessageManager().PLAYER_ALREADY_CONNECTING);
        } else {
            plugin.getLogger().info("Sending player " + targetPlayer.getName() + " to server " + targetServer);
            plugin.getConnectionHandler().connectServer(targetPlayer, targetServer);
            sender.sendMessage(plugin.getMessageManager().SEND_SUCCESS_SENDER.replace("%player%", targetPlayer.getName()).replace("%server%", targetServer));
            targetPlayer.sendMessage(plugin.getMessageManager().SEND_SUCCESS_SENT);
        }
        return true;
    }
}