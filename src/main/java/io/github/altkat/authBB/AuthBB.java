package io.github.altkat.authBB;

import io.github.altkat.authBB.BossBars.LoginBossBar;
import io.github.altkat.authBB.BossBars.RegisterBossBar;
import io.github.altkat.authBB.Commands.*;
import io.github.altkat.authBB.Handlers.Connections;
import io.github.altkat.authBB.Handlers.Listeners;
import io.github.altkat.authBB.Handlers.ConnectionHandler;
import io.github.altkat.authBB.Titles.LoginTitle;
import io.github.altkat.authBB.Titles.RegisterTitle;
import io.github.altkat.authBB.Titles.ConnectionTitle;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class AuthBB extends JavaPlugin {
    FileConfiguration config = getConfig();

    public void loadConnections(){
        Connections.config = getConfig();
        Connections.connectionHandler = new ConnectionHandler(this, "Proxy");
        Connections.loginBossBar = new LoginBossBar(this);
        Connections.registerBossBar = new RegisterBossBar(this);
        Connections.loginTitle = new LoginTitle(this);
        Connections.registerTitle = new RegisterTitle(this);
        Connections.connectionTitle = new ConnectionTitle(this);

    }
    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 23372);
        loadConfig();
        loadConfig();
        if (getServer().getPluginManager().getPlugin("AuthMe") != null) {
            getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §aAuthMe found! Enabling AuthBB...");

        }else {
            getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §cAuthMe is not installed! Disabling AuthBB...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        loadConnections();
        new Listeners(this);

        if (Connections.config.getConfigurationSection("Proxy").getBoolean("enabled")) {
            Bukkit.getConsoleSender().sendMessage("§9[§6AuthBB§9] §aProxy support is enabled.");
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        }else{
            Bukkit.getConsoleSender().sendMessage("§9[§6AuthBB§9] §cProxy support is disabled, you can enable it from the config file.");
        }


        PluginCommand server = getCommand("server");
        if (server != null) {
            server.setExecutor(new ServerCommand());
        }

        Objects.requireNonNull(getCommand("send")).setExecutor(new SendCommand(this));
        Objects.requireNonNull(getCommand("authbb")).setExecutor(new Help(this));

        Objects.requireNonNull(getCommand("authbb")).setTabCompleter(new TabComplete());
        Objects.requireNonNull(getCommand("server")).setTabCompleter(new TabCompleteServer());
        getCommand("send").setTabCompleter(new TabCompleteSend());

        getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §ahas been enabled!");
    }

    @Override
    public void onDisable() {

    getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §chas been disabled!");
    }

    public void loadConfig(){
        reloadConfig();
        saveDefaultConfig();
    }
}
