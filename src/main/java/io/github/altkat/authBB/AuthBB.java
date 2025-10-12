package io.github.altkat.authBB;

import io.github.altkat.authBB.BossBars.LoginBossBar;
import io.github.altkat.authBB.BossBars.RegisterBossBar;
import io.github.altkat.authBB.Commands.*;
import io.github.altkat.authBB.Handlers.ConnectionHandler;
import io.github.altkat.authBB.Handlers.Connections;
import io.github.altkat.authBB.Handlers.Listeners;
import io.github.altkat.authBB.Titles.ConnectionTitle;
import io.github.altkat.authBB.Titles.LoginTitle;
import io.github.altkat.authBB.Titles.RegisterTitle;
import io.papermc.lib.PaperLib;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public final class AuthBB extends JavaPlugin {

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
        new Metrics(this, 23372);
        loadConfig();

        if (getServer().getPluginManager().getPlugin("AuthMe") == null) {
            getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §cAuthMe is not installed! Disabling AuthBB...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        loadConnections();
        new Listeners(this);

        boolean isProxyEnabledInConfig = getConfig().getConfigurationSection("Proxy").getBoolean("enabled");

        if (isProxyEnabledInConfig) {
            if (isProxyDetected()) {
                Connections.isProxyModeActive = true;
                getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §aProxy mode is enabled. Server is running under a proxy (BungeeCord/Velocity).");
                getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
                getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", Connections.connectionHandler);
            }

        } else {
            Connections.isProxyModeActive = false;
            getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §cProxy support is disabled in the config file.");
        }

        Objects.requireNonNull(getCommand("authbb")).setExecutor(new Help(this));
        Objects.requireNonNull(getCommand("authbb")).setTabCompleter(new TabComplete());

        PluginCommand serverCommand = getCommand("server");
        if (serverCommand != null) {
            serverCommand.setExecutor(new ServerCommand());
            serverCommand.setTabCompleter(new TabCompleteServer());
        }

        PluginCommand sendCommand = getCommand("send");
        if (sendCommand != null) {
            sendCommand.setExecutor(new SendCommand(this));
            sendCommand.setTabCompleter(new TabCompleteSend());
        }

        getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §aAuthBB has been enabled!");
    }

    private boolean isProxyDetected() {
        boolean isBungee = getServer().spigot().getConfig().getBoolean("settings.bungeecord", false);
        boolean isVelocity = isVelocitySupported();

        if (isBungee && isVelocity) {
            Connections.isProxyModeActive = false;
            getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §c[CRITICAL CONFIGURATION ERROR] Both BungeeCord and Velocity support are enabled at the same time!");
            getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §cThis will cause IP forwarding issues. Please choose ONLY ONE proxy type.");
            getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §cRecommendation: If you use Velocity, set 'bungeecord: false' in spigot.yml.");
            getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §cProxy features will be disabled until this is fixed.");
            return false;
        }

        if (isBungee || isVelocity) {
            return true;
        }

        Connections.isProxyModeActive = false;
        getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §eProxy mode is enabled in config.yml, but no proxy environment was detected.");
        getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §eFor BungeeCord, set 'bungeecord: true' in spigot.yml.");
        getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §eFor Velocity, enable velocity support in your proxy and server configs.");
        getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §eProxy features will now be disabled to prevent errors.");
        return false;
    }

    /**
     * Checks only for Velocity support across all Paper versions.
     * @return true if Velocity is enabled, false otherwise.
     */
    private boolean isVelocitySupported() {
        if (!PaperLib.isPaper()) {
            return false;
        }
        try {
            Class<?> globalConfigClass = Class.forName("io.papermc.paper.configuration.GlobalConfiguration");
            Method getMethod = globalConfigClass.getMethod("get");
            Object globalConfig = getMethod.invoke(null);
            Field proxiesField = globalConfig.getClass().getDeclaredField("proxies");
            proxiesField.setAccessible(true);
            Object proxies = proxiesField.get(globalConfig);
            Field velocityField = proxies.getClass().getDeclaredField("velocity");
            velocityField.setAccessible(true);
            Object velocity = velocityField.get(proxies);
            Field enabledField = velocity.getClass().getDeclaredField("enabled");
            enabledField.setAccessible(true);
            return (boolean) enabledField.get(velocity);
        } catch (Exception ignored) {
            return getServer().spigot().getConfig().getBoolean("settings.velocity-support.enabled", false);
        }
    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        getServer().getMessenger().unregisterIncomingPluginChannel(this);
        getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §cAuthBB has been disabled!");
    }

    public void loadConfig(){
        saveDefaultConfig();
        reloadConfig();
    }
}