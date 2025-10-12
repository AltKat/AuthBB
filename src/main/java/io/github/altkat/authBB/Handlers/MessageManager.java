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
        ConfigurationSection messagesSection = plugin.getConfig().getConfigurationSection("Messages");

        ONLY_PLAYERS = translate(messagesSection, "only-players");
        NO_PERMISSION = translate(messagesSection, "no-permission");
        WRONG_USAGE_SERVER = translate(messagesSection, "wrong-usage-server");
        WRONG_USAGE_SEND = translate(messagesSection, "wrong-usage-send");
        DISABLED = translate(messagesSection, "disabled");
        SERVER_NOT_FOUND = translate(messagesSection, "server-not-found");
        PLAYER_NOT_FOUND = translate(messagesSection, "player-not-found");
        PLAYER_NOT_AUTHENTICATED = translate(messagesSection, "not-authenticated");
        PLAYER_ALREADY_CONNECTING = translate(messagesSection, "player-already-connecting");
        SEND_SUCCESS_SENDER = translate(messagesSection, "send-success-sender");
        SEND_SUCCESS_SENT = translate(messagesSection, "send-success-sent");
        WAIT = translate(messagesSection, "wait");
    }

    private String translate(ConfigurationSection section, String path) {
        String message = section.getString(path);
        if (message == null) {
            return ChatColor.RED + "Message not found in config.yml: Messages." + path;
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}