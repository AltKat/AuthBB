package io.github.altkat.authBB.Handlers;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.AuthMeAsyncPreLoginEvent;
import io.github.altkat.authBB.AuthBB;
import io.github.altkat.authBB.BossBars.LoginBossBar;
import io.github.altkat.authBB.BossBars.RegisterBossBar;
import io.github.altkat.authBB.Titles.LoginTitle;
import io.github.altkat.authBB.Titles.RegisterTitle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Listeners implements Listener {
    protected ConfigurationSection section = Connections.config.getConfigurationSection("Extras");
    private final LoginBossBar loginBossBar;
    private final RegisterBossBar registerBossBar;
    private final LoginTitle loginTitle;
    private final RegisterTitle registerTitle;
    private final AuthMeApi authMe;
    private final AuthBB plugin;


    public Listeners(AuthBB plugin) {
        this.plugin = plugin;
        this.loginBossBar = Connections.loginBossBar;
        this.registerBossBar = Connections.registerBossBar;
        this.loginTitle = Connections.loginTitle;
        this.registerTitle = Connections.registerTitle;
        this.authMe = AuthMeApi.getInstance();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private List<Float> coordinateList = section.getFloatList("teleport-coordinates");
    private float x = coordinateList.get(0);
    private float y = coordinateList.get(1);
    private float z = coordinateList.get(2);
    private float yaw = coordinateList.get(3);
    private float pitch = coordinateList.get(4);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (authMe.isRegistered(player.getName())) {
            this.loginTitle.sendTitle(player);
            this.loginBossBar.createBB(player);
        } else {
            this.registerTitle.sendTitle(player);
            this.registerBossBar.createBB(player);
        }


        if (section.getBoolean("teleport-on-join")) {
            player.teleport(returnLocation(player.getWorld()));
        }

        event.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
        if (section.getBoolean("makeInvisible")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, -1, 0, false, false));
        }

        if (section.getBoolean("removeJoinMessage")) {
            event.setJoinMessage("");
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (section.getBoolean("removeLeaveMessage")) {
            event.setQuitMessage("");
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        if(section.getBoolean("disableChat")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(section.getBoolean("preventMovement")) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (to == null) {
                return;
            }
            if (from.getX() != to.getX() || from.getZ() != to.getZ() || from.getY() != to.getY()) {
                event.setTo(from);
            }
        }
    }

    public Location returnLocation(World world){
        return new Location(world, x, y, z, yaw, pitch);
    }
}