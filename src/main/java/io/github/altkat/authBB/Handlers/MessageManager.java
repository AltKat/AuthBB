package io.github.altkat.authBB.Handlers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class MessageManager {

    public static String ONLY_PLAYERS;
    public static String NO_PERMISSION;
    public static String WRONG_USAGE_SERVER;
    public static String WRONG_USAGE_SEND;
    public static String DISABLED;
    public static String SERVER_NOT_FOUND;
    public static String PLAYER_NOT_FOUND;
    public static String PLAYER_NOT_AUTHENTICATED;
    public static String PLAYER_ALREADY_CONNECTING;
    public static String SEND_SUCCESS_SENDER;
    public static String SEND_SUCCESS_SENT;

    public static void loadMessages() {
        ConfigurationSection proxySection = Connections.config.getConfigurationSection("Proxy");

        ONLY_PLAYERS = translate(proxySection.getString("only-players"));
        NO_PERMISSION = translate(proxySection.getString("no-permission"));
        WRONG_USAGE_SERVER = translate(proxySection.getString("wrong-usage-server"));
        WRONG_USAGE_SEND = translate(proxySection.getString("wrong-usage-send"));
        DISABLED = translate(proxySection.getString("disabled"));
        SERVER_NOT_FOUND = translate(proxySection.getString("server-not-found"));
        PLAYER_NOT_FOUND = translate(proxySection.getString("player-not-found"));
        PLAYER_NOT_AUTHENTICATED = translate(proxySection.getString("not-authenticated"));
        PLAYER_ALREADY_CONNECTING = translate(proxySection.getString("player-already-connecting"));
        SEND_SUCCESS_SENDER = translate(proxySection.getString("send-success-sender"));
        SEND_SUCCESS_SENT = translate(proxySection.getString("send-success-sent"));
    }

    private static String translate(String message) {
        if (message == null) {
            return "§cMessage not found in config.yml";
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}