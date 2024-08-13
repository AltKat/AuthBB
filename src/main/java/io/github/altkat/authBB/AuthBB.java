package io.github.altkat.authBB;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.api.v3.AuthMeApi;
import io.github.altkat.authBB.BossBars.LoginBossBar;
import io.github.altkat.authBB.BossBars.RegisterBossBar;
import io.github.altkat.authBB.Commands.*;
import io.github.altkat.authBB.Handlers.TeleportHandler;
import io.github.altkat.authBB.Titles.LoginTitle;
import io.github.altkat.authBB.Titles.RegisterTitle;
import io.github.altkat.authBB.Titles.TeleportTitle;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class AuthBB extends JavaPlugin {
    FileConfiguration config = getConfig();

    public void loadVariables(){
        Variables.config = getConfig();
        Variables.teleportHandler = new TeleportHandler(this, "Bungee");
        Variables.loginBossBar = new LoginBossBar(this);
        Variables.registerBossBar = new RegisterBossBar(this);
        Variables.loginTitle = new LoginTitle(this);
        Variables.registerTitle = new RegisterTitle(this);
        Variables.teleportTitle = new TeleportTitle(this);

    }
    @Override
    public void onEnable() {
        // Plugin startup logic

        if (getServer().getPluginManager().getPlugin("AuthMe") == null) {
            getServer().getConsoleSender().sendMessage("§cAuthMe is not installed! Disabling AuthBB...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        loadVariables();
        new Listeners(this);

        if (Variables.config.getConfigurationSection("Bungee").getBoolean("enabled")) {
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
        Objects.requireNonNull(getCommand("authbb")).setExecutor(new ReloadCommand(this));

        Objects.requireNonNull(getCommand("authbb")).setTabCompleter(new TabComplete());
        Objects.requireNonNull(getCommand("server")).setTabCompleter(new TabCompleteServer());
        getCommand("send").setTabCompleter(new TabCompleteSend());
        loadConfig();

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
