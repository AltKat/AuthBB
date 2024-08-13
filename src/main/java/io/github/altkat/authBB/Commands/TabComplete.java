package io.github.altkat.authBB.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (sender.hasPermission("AuthBB.admin")) {
            completions.add("reload");
            completions.add("help");
        }else {
            completions.add("help");
        }

        return completions;
    }
}
