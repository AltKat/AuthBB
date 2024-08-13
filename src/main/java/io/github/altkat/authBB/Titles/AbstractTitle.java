package io.github.altkat.authBB.Titles;

import io.github.altkat.authBB.AuthBB;
import io.github.altkat.authBB.Variables;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public abstract class AbstractTitle {
    protected final ConfigurationSection section;
    protected final AuthBB plugin;

    AbstractTitle(AuthBB plugin, String sectionName){
        this.plugin = plugin;
        this.section = Variables.config.getConfigurationSection(sectionName);
    }

    public void sendTitle(Player player){
        String title = section.getString("title", "").replace("&", "§");
        String subtitle = section.getString("subtitle", "").replace("&", "§");
        Integer fadein = section.getInt("fadein");
        Integer fadeout = section.getInt("fadeout");
        Integer stay = section.getInt("stay");
        player.sendTitle(title, subtitle, fadein*20, stay*20, fadeout*20);
    }
}