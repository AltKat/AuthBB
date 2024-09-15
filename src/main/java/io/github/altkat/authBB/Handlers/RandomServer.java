package io.github.altkat.authBB.Handlers;

import io.github.altkat.authBB.AuthBB;
import io.github.altkat.authBB.Handlers.Connections;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;

public class RandomServer {
    protected AuthBB plugin;
    protected ConfigurationSection section;

    public RandomServer(AuthBB plugin) {
        this.plugin = plugin;
        this.section = Connections.config.getConfigurationSection("Proxy");
    }

    public String randomServer() {
        List<String> list = this.section.getStringList("servers");
        int randomInt = (int)(Math.random() * (double)list.size());
        return (String)list.get(randomInt);
    }
}
