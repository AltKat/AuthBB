package io.github.altkat.authBB.Commands;

import io.github.altkat.authBB.AuthBB;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AuthBBTabCompleter implements TabCompleter {
    private final AuthBB plugin;

    public AuthBBTabCompleter(AuthBB plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> subCommands = new ArrayList<>(Arrays.asList("help", "server"));
            if (sender.hasPermission("AuthBB.admin")) {
                subCommands.add("reload");
                subCommands.add("send");
            }
            return filterCompletions(subCommands, args[0]);
        }

        String subCommand = args[0].toLowerCase();
        if (subCommand.equals("server") && args.length == 2) {
            if (plugin.isProxyModeActive() && sender.hasPermission("AuthBB.server")) {
                return filterCompletions(plugin.getConfig().getStringList("Proxy.servers"), args[1]);
            }
        } else if (subCommand.equals("send")) {
            if (plugin.isProxyModeActive() && sender.hasPermission("AuthBB.send")) {
                if (args.length == 2) {
                    List<String> playerNames = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
                    return filterCompletions(playerNames, args[1]);
                } else if (args.length == 3) {
                    return filterCompletions(plugin.getConfig().getStringList("Proxy.servers"), args[2]);
                }
            }
        }

        return Collections.emptyList();
    }

    private List<String> filterCompletions(List<String> list, String input) {
        return list.stream().filter(s -> s.toLowerCase().startsWith(input.toLowerCase())).collect(Collectors.toList());
    }
}