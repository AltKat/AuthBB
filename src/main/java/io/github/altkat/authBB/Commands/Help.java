package io.github.altkat.authBB.Commands;

import io.github.altkat.authBB.AuthBB;
import io.github.altkat.authBB.Handlers.Connections;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class Help implements CommandExecutor {
    protected AuthBB plugin;
    public Help(AuthBB plugin){
        this.plugin = plugin;
    }

    protected ConfigurationSection section = Connections.config.getConfigurationSection("Bungee");

    String wrongUsage = section.getString("wrong-usage-server").replace("&", "§");
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            if(strings.length == 0){
                commandSender.sendMessage("§b/AuthBB help");
                return true;
            }

            if(strings[0].equals("help")){
                if(commandSender.hasPermission("AuthBB.help")){
                    commandSender.sendMessage("§b==========[ AuthBossBar Admin HELP ]==========");
                    commandSender.sendMessage("  §f/authbossbar reload: Reloads the Plugin");
                    commandSender.sendMessage("  §f/server (server name): Sends you to the lobby server");
                    commandSender.sendMessage("  §f/send player (server name): Sends selected player to the lobby server");
                    commandSender.sendMessage("  §f/AuthBB help: Shows help page.");
                }else {
                    commandSender.sendMessage("§b==========[ AuthBossBar HELP ]==========");
                    commandSender.sendMessage("  §f/server (server name): Sends you to the lobby server");
                    commandSender.sendMessage("  §f/AuthBB help: Shows help page.");
                }
            }


        return true;
    }
}
