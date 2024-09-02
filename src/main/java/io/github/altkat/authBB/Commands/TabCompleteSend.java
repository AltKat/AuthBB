package io.github.altkat.authBB.Commands;

import io.github.altkat.authBB.Handlers.Connections;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteSend implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> list = Connections.config.getConfigurationSection("Proxy").getStringList("servers");
        if(Connections.config.getConfigurationSection("Proxy").getBoolean("enabled")) {
            if (sender.hasPermission("AuthBB.send")) {
                if(args.length == 1) {
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        completions.add(player.getName());
                    }
                }else if(args.length == 2) {
                    completions.addAll(list);
                }
            }
        }
        return completions;
    }
}
