package io.github.altkat.authBB.Handlers;

import io.github.altkat.authBB.BossBars.LoginBossBar;
import io.github.altkat.authBB.BossBars.RegisterBossBar;
import io.github.altkat.authBB.Titles.LoginTitle;
import io.github.altkat.authBB.Titles.RegisterTitle;
import io.github.altkat.authBB.Titles.ConnectionTitle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Connections {
    public static List<Player> sending = new ArrayList<>();
    public static FileConfiguration config;
    public static LoginBossBar loginBossBar;
    public static RegisterBossBar registerBossBar;
    public static LoginTitle loginTitle;
    public static RegisterTitle registerTitle;
    public static ConnectionTitle connectionTitle;
    public static ConnectionHandler connectionHandler;
}
