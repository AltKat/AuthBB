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
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class AuthBB extends JavaPlugin {
    FileConfiguration config = getConfig();

    public void loadConnections(){
        Connections.config = getConfig();
        Connections.connectionHandler = new ConnectionHandler(this, "Bungee");
        Connections.loginBossBar = new LoginBossBar(this);
        Connections.registerBossBar = new RegisterBossBar(this);
        Connections.loginTitle = new LoginTitle(this);
        Connections.registerTitle = new RegisterTitle(this);
        Connections.connectionTitle = new ConnectionTitle(this);

    }
    @Override
    public void onEnable() {
        // Plugin startup logic
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

        if (Connections.config.getConfigurationSection("Bungee").getBoolean("enabled")) {
            Bukkit.getConsoleSender().sendMessage("§9[§6AuthBB§9] §aBungeecord support is enabled.");
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        }else{
            Bukkit.getConsoleSender().sendMessage("§9[§6AuthBB§9] §cBungeecord support is disabled, you can open it from the config file.");
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
        // Plugin shutdown logic
    getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §chas been disabled!");
    }

    public void loadConfig(){
        reloadConfig();
        saveDefaultConfig();
    }
}
