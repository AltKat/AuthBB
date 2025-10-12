package io.github.altkat.authBB.Handlers;

import com.google.common.base.Charsets;
import io.github.altkat.authBB.AuthBB;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConfigUpdater {

    public static void update(AuthBB plugin) throws IOException {
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("config.yml"), Charsets.UTF_8));
        FileConfiguration userConfig = YamlConfiguration.loadConfiguration(configFile);

        for (String key : defaultConfig.getKeys(true)) {
            if (!userConfig.contains(key)) {
                userConfig.set(key, defaultConfig.get(key));
            }
        }
        userConfig.save(configFile);
    }
}