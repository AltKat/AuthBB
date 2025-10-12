package io.github.altkat.authBB.Handlers;

import io.github.altkat.authBB.AuthBB;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomServer {
    private final AuthBB plugin;
    private final ConfigurationSection proxySection;

    public RandomServer(AuthBB plugin) {
        this.plugin = plugin;
        this.proxySection = plugin.getConfig().getConfigurationSection("Proxy");
    }

    public String getRandomServer() {
        List<String> serverList = this.proxySection.getStringList("servers");
        if (serverList.isEmpty()) {
            return null;
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(serverList.size());
        return serverList.get(randomIndex);
    }
}