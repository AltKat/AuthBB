package io.github.altkat.authBB.Commands;

import io.github.altkat.authBB.AuthBB;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Help implements CommandExecutor {
    private final AuthBB plugin;

    public Help(AuthBB plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            if (sender.hasPermission("AuthBB.admin")) {
                sender.sendMessage("§b==========[ AuthBossBar Admin HELP ]==========");
                sender.sendMessage("  §f/server (server): Connect to a lobby server.");
                sender.sendMessage("  §f/send (player) (server): Send a player to a lobby server.");
                sender.sendMessage("  §f/authbb reload: Reloads the configuration file.");
                sender.sendMessage("  §f/authbb help: Shows this help page.");
            } else {
                sender.sendMessage("§b==========[ AuthBossBar HELP ]==========");
                sender.sendMessage("  §f/server (server): Connect to a lobby server.");
                sender.sendMessage("  §f/authbb help: Shows this help page.");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("AuthBB.admin")) {
                plugin.reload();
                sender.sendMessage(ChatColor.GREEN + "AuthBB configuration has been reloaded.");
            } else {
                sender.sendMessage(plugin.getMessageManager().NO_PERMISSION);
            }
            return true;
        }

        sender.sendMessage(plugin.getMessageManager().WRONG_USAGE_SERVER.replace("server servername", "authbb help"));
        return true;
    }
}