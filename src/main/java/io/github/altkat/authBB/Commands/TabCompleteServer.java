package io.github.altkat.authBB.Commands;

import io.github.altkat.authBB.AuthBB;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabCompleteServer implements TabCompleter {
    private final AuthBB plugin;

    public TabCompleteServer(AuthBB plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!plugin.isProxyModeActive() || !sender.hasPermission("AuthBB.server")) {
            return Collections.emptyList();
        }
        if (args.length == 1) {
            return plugin.getConfig().getStringList("Proxy.servers");
        }
        return Collections.emptyList();
    }
}