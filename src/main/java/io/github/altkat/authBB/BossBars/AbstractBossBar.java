package io.github.altkat.authBB.BossBars;

import fr.xephi.authme.api.v3.AuthMeApi;
import io.github.altkat.authBB.AuthBB;
import io.github.altkat.authBB.Handlers.RandomServer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class AbstractBossBar {
    protected final AuthBB plugin;
    protected final ConfigurationSection section;
    protected final AuthMeApi authMe;
    private final RandomServer randomServer;

    public AbstractBossBar(AuthBB plugin, String sectionName) {
        this.plugin = plugin;
        this.section = plugin.getConfig().getConfigurationSection(sectionName);
        this.authMe = AuthMeApi.getInstance();
        this.randomServer = new RandomServer(plugin);
    }

    public void createBB(Player player) {
        int time = section.getInt("time");
        String title = section.getString("title", "").replace("%time%", Integer.toString(time));
        String color = section.getString("color", "BLUE");
        String style = section.getString("style", "SEGMENTED_10");
        String kickMessage;

        if (section.getName().equals("LoginBossBar")) {
            kickMessage = plugin.getConfig().getString("Kick.login-message", "You were kicked for not logging in.").replace("&", "§");
        } else {
            kickMessage = plugin.getConfig().getString("Kick.register-message", "You were kicked for not registering in time.").replace("&", "§");
        }

        NamespacedKey key = new NamespacedKey(plugin, player.getUniqueId().toString());
        BossBar existingBossBar = Bukkit.getBossBar(key);
        if (existingBossBar != null) {
            existingBossBar.removePlayer(player);
            Bukkit.removeBossBar(key);
        }

        BarColor barColor;
        try {
            barColor = BarColor.valueOf(color.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid BossBar color '" + color + "' in config.yml. Defaulting to BLUE.");
            barColor = BarColor.BLUE;
        }

        BarStyle barStyle;
        try {
            barStyle = BarStyle.valueOf(style.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid BossBar style '" + style + "' in config.yml. Defaulting to SEGMENTED_10.");
            barStyle = BarStyle.SEGMENTED_10;
        }

        BossBar bossBar = Bukkit.createBossBar(key, title, barColor, barStyle);
        bossBar.addPlayer(player);
        bossBar.setVisible(true);

        new BukkitRunnable() {
            private int secondsRemaining = time;

            @Override
            public void run() {
                if (authMe.isAuthenticated(player)) {
                    bossBar.removePlayer(player);
                    player.resetTitle();
                    cancel();
                    if (plugin.isProxyModeActive()) {
                        plugin.getConnectionTitle().sendTitle(player);
                        String server = randomServer.getRandomServer();
                        if (server != null) {
                            plugin.getConnectionHandler().connectServer(player, server);
                        } else {
                            plugin.getLogger().warning("Could not send player " + player.getName() + " to a server because the server list in config.yml is empty.");
                        }
                    }
                    return;
                }

                if (secondsRemaining <= 0) {
                    bossBar.removePlayer(player);
                    if (plugin.getConfig().getBoolean("Kick.enabled")) {
                        player.kickPlayer(kickMessage);
                    }
                    cancel();
                } else {
                    secondsRemaining--;
                    String updatedTitle = section.getString("title", "")
                            .replace("%time%", String.valueOf(secondsRemaining))
                            .replace("&", "§");
                    bossBar.setTitle(updatedTitle);
                    bossBar.setProgress(Math.max(0.0, (double) secondsRemaining / time));

                    if (secondsRemaining <= (time * 0.2)) {
                        bossBar.setColor(BarColor.RED);
                    } else if (secondsRemaining <= (time * 0.5)) {
                        bossBar.setColor(BarColor.YELLOW);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20L);
    }
}