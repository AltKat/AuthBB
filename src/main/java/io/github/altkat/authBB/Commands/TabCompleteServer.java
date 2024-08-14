package io.github.altkat.authBB.Commands;

import io.github.altkat.authBB.Handlers.Connections;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteServer implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if(Connections.config.getConfigurationSection("Bungee").getBoolean("enabled")) {
            if (sender.hasPermission("AuthBB.server")) {
                if(args.length == 1) {
                    completions.add(Connections.config.getConfigurationSection("Bungee").getString("server"));
                }else {
                    completions.clear();
                }
            }
        }
        return completions;
    }
}
