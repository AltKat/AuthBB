package io.github.altkat.authBB.Commands;

import io.github.altkat.authBB.AuthBB;
import io.github.altkat.authBB.Variables;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {
    protected AuthBB plugin;
    public ReloadCommand(AuthBB plugin){
        this.plugin = plugin;
    }

    protected ConfigurationSection section = Variables.config.getConfigurationSection("Bungee");

    String nopermission = section.getString("no-permission").replace("&", "§");
    String wrongUsage = section.getString("wrong-usage-server").replace("&", "§");
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            if(strings.length == 0){
                commandSender.sendMessage("§b/AuthBB help");
                return true;
            }

            if(strings[0].equals("reload")) {
                if(commandSender.hasPermission("AuthBB.reload")) {
                    plugin.loadConfig();
                    commandSender.sendMessage("§9[§6AuthBB§9] §bReloaded successfully. We always recommend restarting the server after changes, or use a plugin manager to reload the plugin.");
                }else {
                    commandSender.sendMessage(nopermission);
                }
            }

            if(strings[0].equals("help")){
                if(commandSender.hasPermission("AuthBB.reload")){
                    commandSender.sendMessage("§b==========[ AuthBossBar HELP ]==========");
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
