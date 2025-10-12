package io.github.altkat.authBB.Commands;

import io.github.altkat.authBB.AuthBB;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompleteSend implements TabCompleter {
    private final AuthBB plugin;

    public TabCompleteSend(AuthBB plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!plugin.isProxyModeActive() || !sender.hasPermission("AuthBB.send")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return Bukkit.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            return plugin.getConfig().getStringList("Proxy.servers");
        }
        return Collections.emptyList();
    }
}