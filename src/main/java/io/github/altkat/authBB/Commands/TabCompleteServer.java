package io.github.altkat.authBB.Commands;

import io.github.altkat.authBB.Variables;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteServer implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if(Variables.config.getConfigurationSection("Bungee").getBoolean("enabled")) {
            if (sender.hasPermission("AuthBB.server") || sender.hasPermission("AuthBB.admin")) {
                completions.add(Variables.config.getConfigurationSection("Bungee").getString("server"));
            }
        }
        return completions;
    }
}
