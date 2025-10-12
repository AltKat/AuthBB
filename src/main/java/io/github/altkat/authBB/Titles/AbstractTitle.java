package io.github.altkat.authBB.Titles;

import io.github.altkat.authBB.AuthBB;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public abstract class AbstractTitle {
    protected final AuthBB plugin;
    protected final ConfigurationSection section;

    AbstractTitle(AuthBB plugin, String sectionName){
        this.plugin = plugin;
        this.section = plugin.getConfig().getConfigurationSection(sectionName);
    }

    public void sendTitle(Player player){
        if (section == null) {
            plugin.getLogger().warning("Title configuration section '" + section.getName() + "' not found in config.yml!");
            return;
        }

        String title = ChatColor.translateAlternateColorCodes('&', section.getString("title", " "));
        String subtitle = ChatColor.translateAlternateColorCodes('&', section.getString("subtitle", " "));
        int fadein = section.getInt("fadein", 1);
        int stay = section.getInt("stay", 5);
        int fadeout = section.getInt("fadeout", 2);

        player.sendTitle(title, subtitle, fadein * 20, stay * 20, fadeout * 20);
    }
}