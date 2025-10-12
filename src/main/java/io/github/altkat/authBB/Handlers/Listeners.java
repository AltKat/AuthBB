package io.github.altkat.authBB.Handlers;

import fr.xephi.authme.api.v3.AuthMeApi;
import io.github.altkat.authBB.AuthBB;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Listeners implements Listener {
    private final AuthBB plugin;
    private final AuthMeApi authMe;

    public Listeners(AuthBB plugin) {
        this.plugin = plugin;
        this.authMe = AuthMeApi.getInstance();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (authMe.isRegistered(player.getName())) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    plugin.getLoginTitle().sendTitle(player);
                    plugin.getLoginBossBar().createBB(player);
                });
            } else {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    plugin.getRegisterTitle().sendTitle(player);
                    plugin.getRegisterBossBar().createBB(player);
                });
            }
        });

        if (plugin.getConfig().getBoolean("Extras.teleport-on-join", false)) {
            player.teleport(returnLocation(player.getWorld()));
        }

        if (plugin.getConfig().getBoolean("Extras.makeInvisible", false)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, -1, 0, true, false));
        }

        if (plugin.getConfig().getBoolean("Extras.removeJoinMessage", false)) {
            event.setJoinMessage("");
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (plugin.getConfig().getBoolean("Extras.removeLeaveMessage", false)) {
            event.setQuitMessage("");
        }

        Player player = event.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            PotionEffect invisibilityEffect = player.getPotionEffect(PotionEffectType.INVISIBILITY);

            if (invisibilityEffect != null && invisibilityEffect.isAmbient() && !invisibilityEffect.hasParticles()) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        if (plugin.getConfig().getBoolean("Extras.disableChat", false)) {
            if(authMe.isAuthenticated(event.getPlayer())){
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot chat in this server!" ));
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(plugin.getConfig().getBoolean("Extras.preventMovement", false)) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (to == null) return;
            if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
                event.setTo(from);
            }
        }
    }

    private Location returnLocation(World world){
        List<Double> coordinateList = plugin.getConfig().getDoubleList("Extras.teleport-coordinates");
        if (coordinateList.size() < 5) {
            plugin.getLogger().warning("teleport-coordinates in config.yml is not configured correctly! Needs 5 values (x, y, z, yaw, pitch).");
            return world.getSpawnLocation();
        }
        double x = coordinateList.get(0);
        double y = coordinateList.get(1);
        double z = coordinateList.get(2);
        float yaw = coordinateList.get(3).floatValue();
        float pitch = coordinateList.get(4).floatValue();
        return new Location(world, x, y, z, yaw, pitch);
    }
}