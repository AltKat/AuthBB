package io.github.altkat.authBB.BossBars;

import fr.xephi.authme.api.v3.AuthMeApi;
import io.github.altkat.authBB.AuthBB;
import io.github.altkat.authBB.Handlers.ConnectionHandler;
import io.github.altkat.authBB.Handlers.Connections;
import org.bukkit.Bukkit;
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

    public AbstractBossBar(AuthBB plugin, String sectionName) {
        this.plugin = plugin;
        this.section = Connections.config.getConfigurationSection(sectionName);
        this.authMe = AuthMeApi.getInstance();
        this.connectionHandler = Connections.connectionHandler;
    }

    public void createBB(Player player) {
        int time = section.getInt("time");
        String title = section.getString("title", "").replace("%time%", Integer.toString(time));
        String color = section.getString("color", "BLUE");
        String style = section.getString("style", "SEGMENTED_10");
        String kickmessage;
        if(section.getName().equals("LoginBossBar")){
            kickmessage = plugin.getConfig().getString("Kick.login-message").replace("&", "§");
        }else {
            kickmessage = plugin.getConfig().getString("Kick.register-message").replace("&", "§");
        }



        BossBar bossBar = Bukkit.createBossBar(title, BarColor.valueOf(color), BarStyle.valueOf(style));
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
                    String title = section.getString("title")
                            .replace("%time%", String.valueOf(secondsRemaining))
                            .replace("&", "§");
                    bossBar.setTitle(title);
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
                    if(Connections.config.getConfigurationSection("Bungee").getBoolean("enabled")){
                        Connections.connectionTitle.sendTitle(player);
                        connectionHandler.connectServer(player, Connections.config.getConfigurationSection("Bungee").getString("server"));
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);

    }
}