package io.github.altkat.authBB.BossBars;

import fr.xephi.authme.api.v3.AuthMeApi;
import io.github.altkat.authBB.AuthBB;
import io.github.altkat.authBB.Commands.RandomServer;
import io.github.altkat.authBB.Handlers.ConnectionHandler;
import io.github.altkat.authBB.Handlers.Connections;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public abstract class AbstractBossBar {
    protected final ConfigurationSection section;
    protected final AuthBB plugin;
    protected final AuthMeApi authMe;
    protected final ConnectionHandler connectionHandler;
    protected final RandomServer randomServer;

    public AbstractBossBar(AuthBB plugin, String sectionName) {
        this.plugin = plugin;
        this.section = Connections.config.getConfigurationSection(sectionName);
        this.authMe = AuthMeApi.getInstance();
        this.connectionHandler = Connections.connectionHandler;
        this.randomServer = new RandomServer(plugin);
    }

    public void createBB(Player player) {
        int time = section.getInt("time");
        String title = section.getString("title", "").replace("%time%", Integer.toString(time));
        String color = section.getString("color", "BLUE");
        String style = section.getString("style", "SEGMENTED_10");
        String kickmessage;
        if(section.getName().equals("LoginBossBar")){
            kickmessage = plugin.getConfig().getString("Kick.login-message").replace("&", "§");
        } else {
            kickmessage = plugin.getConfig().getString("Kick.register-message").replace("&", "§");
        }

        BossBar existingBossBar = Bukkit.getBossBar(NamespacedKey.minecraft(player.getUniqueId().toString()));
        if (existingBossBar != null) {
            existingBossBar.removePlayer(player);
        }

        BarColor barColor;
        BarStyle barStyle;

        try {
            barColor = BarColor.valueOf(color);
        } catch (IllegalArgumentException e) {
            barColor = BarColor.BLUE;
        }

        try {
            barStyle = BarStyle.valueOf(style);
        } catch (IllegalArgumentException e) {
            barStyle = BarStyle.SEGMENTED_10;
        }

        BossBar bossBar = Bukkit.createBossBar(title, barColor, barStyle);
        bossBar.addPlayer(player);
        bossBar.setVisible(true);

        new BukkitRunnable() {
            private int secondsRemaining = time;

            @Override
            public void run() {
                --secondsRemaining;
                if (secondsRemaining <= 0) {
                    bossBar.removePlayer(player);
                    if(plugin.getConfig().getBoolean("Kick.enabled")) {
                        player.kickPlayer(kickmessage);
                    }
                    cancel();
                } else {
                    String updatedTitle = section.getString("title")
                            .replace("%time%", String.valueOf(secondsRemaining))
                            .replace("&", "§");
                    bossBar.setTitle(updatedTitle);
                    bossBar.setProgress(secondsRemaining / (double) time);

                    if (secondsRemaining <= (time * 0.2)) {
                        bossBar.setColor(BarColor.RED);
                    } else if (secondsRemaining <= (time * 0.5)) {
                        bossBar.setColor(BarColor.YELLOW);
                    } else {
                        bossBar.setColor(BarColor.valueOf(color));
                    }
                }
                if(authMe.isAuthenticated(player)){
                    bossBar.removePlayer(player);
                    player.resetTitle();
                    cancel();
                    if(Connections.config.getConfigurationSection("Proxy").getBoolean("enabled")){
                        Connections.connectionTitle.sendTitle(player);
                        connectionHandler.connectServer(player, randomServer.randomServer());
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
