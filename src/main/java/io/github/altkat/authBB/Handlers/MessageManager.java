package io.github.altkat.authBB.Handlers;

import io.github.altkat.authBB.AuthBB;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class MessageManager {
    public final String ONLY_PLAYERS;
    public final String NO_PERMISSION;
    public final String WRONG_USAGE_SERVER;
    public final String WRONG_USAGE_SEND;
    public final String DISABLED;
    public final String SERVER_NOT_FOUND;
    public final String PLAYER_NOT_FOUND;
    public final String PLAYER_NOT_AUTHENTICATED;
    public final String PLAYER_ALREADY_CONNECTING;
    public final String SEND_SUCCESS_SENDER;
    public final String SEND_SUCCESS_SENT;
    public final String WAIT;

    public MessageManager(AuthBB plugin) {
        ConfigurationSection proxySection = plugin.getConfig().getConfigurationSection("Proxy");

        ONLY_PLAYERS = translate(proxySection, "only-players");
        NO_PERMISSION = translate(proxySection, "no-permission");
        WRONG_USAGE_SERVER = translate(proxySection, "wrong-usage-server");
        WRONG_USAGE_SEND = translate(proxySection, "wrong-usage-send");
        DISABLED = translate(proxySection, "disabled");
        SERVER_NOT_FOUND = translate(proxySection, "server-not-found");
        PLAYER_NOT_FOUND = translate(proxySection, "player-not-found");
        PLAYER_NOT_AUTHENTICATED = translate(proxySection, "not-authenticated");
        PLAYER_ALREADY_CONNECTING = translate(proxySection, "player-already-connecting");
        SEND_SUCCESS_SENDER = translate(proxySection, "send-success-sender");
        SEND_SUCCESS_SENT = translate(proxySection, "send-success-sent");
        WAIT = translate(proxySection, "wait");
    }

    private String translate(ConfigurationSection section, String path) {
        String message = section.getString(path);
        if (message == null) {
            return ChatColor.RED + "Message not found in config.yml: Proxy." + path;
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}