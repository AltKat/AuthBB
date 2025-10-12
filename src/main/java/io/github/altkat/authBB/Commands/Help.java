package io.github.altkat.authBB.Commands;

import io.github.altkat.authBB.AuthBB;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Help implements CommandExecutor {
    private final AuthBB plugin;

    public Help(AuthBB plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage("§b/AuthBB help");
            return true;
        }

        if (strings[0].equalsIgnoreCase("help")) {
            if (commandSender.hasPermission("AuthBB.help")) {
                commandSender.sendMessage("§b==========[ AuthBossBar Admin HELP ]==========");
                commandSender.sendMessage("  §f/server (server name): Sends you to the lobby server.");
                commandSender.sendMessage("  §f/send (player) (server name): Sends selected player to the lobby server.");
                commandSender.sendMessage("  §f/AuthBB help: Shows this help page.");
            } else {
                commandSender.sendMessage("§b==========[ AuthBossBar HELP ]==========");
                commandSender.sendMessage("  §f/server (server name): Sends you to the lobby server.");
                commandSender.sendMessage("  §f/AuthBB help: Shows this help page.");
            }
        } else {
            commandSender.sendMessage(plugin.getMessageManager().WRONG_USAGE_SERVER.replace("server servername", "authbb help"));
        }

        return true;
    }
}