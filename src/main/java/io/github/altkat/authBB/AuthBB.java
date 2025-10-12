package io.github.altkat.authBB;

import io.github.altkat.authBB.BossBars.LoginBossBar;
import io.github.altkat.authBB.BossBars.RegisterBossBar;
import io.github.altkat.authBB.Commands.*;
import io.github.altkat.authBB.Handlers.ConnectionHandler;
import io.github.altkat.authBB.Handlers.Listeners;
import io.github.altkat.authBB.Handlers.MessageManager;
import io.github.altkat.authBB.Handlers.UpdateChecker;
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

    private ConnectionHandler connectionHandler;
    private MessageManager messageManager;
    private LoginBossBar loginBossBar;
    private RegisterBossBar registerBossBar;
    private LoginTitle loginTitle;
    private RegisterTitle registerTitle;
    private ConnectionTitle connectionTitle;
    private boolean isProxyModeActive = false;

    @Override
    public void onEnable() {
        new Metrics(this, 23372);

        reload();

        registerListenersAndCommands();

        final int SPIGOT_RESOURCE_ID = 118798;
        new UpdateChecker(this, SPIGOT_RESOURCE_ID).getVersion(newVersion -> {
            if (UpdateChecker.isNewerVersion(this.getDescription().getVersion(), newVersion)) {
                getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §eA new update is available! Version: " + newVersion);
                getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §eDownload it from: https://www.spigotmc.org/resources/authbb-enhanced-boss-bar-integration-for-authme-proxy-teleport-multi-lobby-support." + SPIGOT_RESOURCE_ID + "/");
            }else {
                getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §aYou are using the latest version of AuthBB! Version: " + this.getDescription().getVersion());
            }
        });

        getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §aAuthBB has been enabled!");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §cAuthBB has been disabled!");
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();

        if (getServer().getPluginManager().getPlugin("AuthMe") == null) {
            getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §cAuthMe is not installed! Disabling AuthBB...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        initializeManagers();

        setupProxy();
    }

    private void initializeManagers() {
        this.messageManager = new MessageManager(this);
        this.connectionHandler = new ConnectionHandler(this);
        this.loginBossBar = new LoginBossBar(this);
        this.registerBossBar = new RegisterBossBar(this);
        this.loginTitle = new LoginTitle(this);
        this.registerTitle = new RegisterTitle(this);
        this.connectionTitle = new ConnectionTitle(this);
    }

    private void setupProxy() {
        boolean isProxyEnabledInConfig = getConfig().getBoolean("Proxy.enabled", false);
        if (isProxyEnabledInConfig) {
            if (isProxyDetected()) {
                this.isProxyModeActive = true;
                getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §aProxy mode is enabled. Server is running under a proxy (BungeeCord/Velocity).");
                getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
                getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this.connectionHandler);
            } else {
                this.isProxyModeActive = false;
            }
        } else {
            this.isProxyModeActive = false;
            getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §cProxy support is disabled in the config file.");
        }
    }

    private void registerListenersAndCommands() {
        new Listeners(this);
        Objects.requireNonNull(getCommand("authbb")).setExecutor(new Help(this));
        Objects.requireNonNull(getCommand("authbb")).setTabCompleter(new TabComplete(this));
        PluginCommand serverCommand = getCommand("server");
        if (serverCommand != null) {
            serverCommand.setExecutor(new ServerCommand(this));
            serverCommand.setTabCompleter(new TabCompleteServer(this));
        }
        PluginCommand sendCommand = getCommand("send");
        if (sendCommand != null) {
            sendCommand.setExecutor(new SendCommand(this));
            sendCommand.setTabCompleter(new TabCompleteSend(this));
        }
    }

    private boolean isProxyDetected() {
        boolean isBungee = getServer().spigot().getConfig().getBoolean("settings.bungeecord", false);
        boolean isVelocity = isVelocitySupported();

        if (isBungee && isVelocity) {
            getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §c[CRITICAL CONFIG ERROR] Both BungeeCord and Velocity support are enabled at the same time!");
            getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §cThis will cause IP forwarding issues. Please choose ONLY ONE proxy type.");
            getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §cRecommendation: If you use Velocity, set 'bungeecord: false' in spigot.yml.");
            getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §cProxy features will be disabled until this is fixed.");
            return false;
        }

        if (isBungee || isVelocity) {
            return true;
        }

        getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §eProxy mode is enabled in config.yml, but no proxy environment was detected.");
        getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §eFor BungeeCord, set 'bungeecord: true' in spigot.yml.");
        getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §eFor Velocity, enable velocity support in your proxy and server configs.");
        getServer().getConsoleSender().sendMessage("§9[§6AuthBB§9] §eProxy features will now be disabled to prevent errors.");
        return false;
    }

    private boolean isVelocitySupported() {
        if (!PaperLib.isPaper()) return false;
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

    public ConnectionHandler getConnectionHandler() { return connectionHandler; }
    public MessageManager getMessageManager() { return messageManager; }
    public LoginBossBar getLoginBossBar() { return loginBossBar; }
    public RegisterBossBar getRegisterBossBar() { return registerBossBar; }
    public LoginTitle getLoginTitle() { return loginTitle; }
    public RegisterTitle getRegisterTitle() { return registerTitle; }
    public ConnectionTitle getConnectionTitle() { return connectionTitle; }
    public boolean isProxyModeActive() { return isProxyModeActive; }
}