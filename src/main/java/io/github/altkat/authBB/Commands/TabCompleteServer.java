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
        List<String> list = Connections.config.getConfigurationSection("Proxy").getStringList("servers");
        if(Connections.config.getConfigurationSection("Proxy").getBoolean("enabled")) {
            if (sender.hasPermission("AuthBB.server")) {
                if(args.length == 1) {
                    completions.addAll(list);
                }else {
                    completions.clear();
                }
            }
        }
        return completions;
    }
}
