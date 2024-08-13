package io.github.altkat.authBB;

import fr.xephi.authme.api.v3.AuthMeApi;
import io.github.altkat.authBB.BossBars.AbstractBossBar;
import io.github.altkat.authBB.BossBars.LoginBossBar;
import io.github.altkat.authBB.BossBars.RegisterBossBar;
import io.github.altkat.authBB.Titles.LoginTitle;
import io.github.altkat.authBB.Titles.RegisterTitle;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listeners implements Listener {
    private final LoginBossBar loginBossBar;
    private final RegisterBossBar registerBossBar;
    private final LoginTitle loginTitle;
    private final RegisterTitle registerTitle;
    private final AuthMeApi authMe;
    private final AuthBB plugin;

    public Listeners(AuthBB plugin){
        this.plugin = plugin;
        this.loginBossBar = Variables.loginBossBar;
        this.registerBossBar = Variables.registerBossBar;
        this.loginTitle = Variables.loginTitle;
        this.registerTitle = Variables.registerTitle;
        this.authMe = AuthMeApi.getInstance();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(authMe.isRegistered(player.getName())){
            this.loginTitle.sendTitle(player);
            this.loginBossBar.createBB(player);
        }else{
            this.registerTitle.sendTitle(player);
            this.registerBossBar.createBB(player);
        }
    }
}