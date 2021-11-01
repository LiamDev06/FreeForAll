package com.hybrid.ffa;

import com.hybrid.ffa.commands.KitCommand;
import com.hybrid.ffa.commands.admin.ReloadConfigCommand;
import com.hybrid.ffa.commands.admin.ReloadKitsConfigCommand;
import com.hybrid.ffa.listeners.JoinLeaveListener;
import com.hybrid.ffa.managers.GameMapManager;
import com.hybrid.ffa.managers.KitManager;
import com.hybrid.ffa.managers.ScoreboardManager;
import com.hybrid.ffa.commands.admin.ReloadPlayerDataCommand;
import com.hybrid.ffa.commands.SpawnLocationCommand;
import com.hybrid.ffa.listeners.GameMapListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class FreeForAllPlugin extends JavaPlugin {

    private static FreeForAllPlugin INSTANCE;
    private GameMapManager gameMapManager;
    private KitManager kitManager;
    private File kitsConfigFile;
    private FileConfiguration kitsConfig;

    @Override
    public void onEnable() {
        long time = System.currentTimeMillis();
        INSTANCE = this;

        new KitCommand();
        new SpawnLocationCommand();

        new ReloadConfigCommand();
        new ReloadKitsConfigCommand();
        new ReloadPlayerDataCommand();

        gameMapManager = new GameMapManager();
        kitManager = new KitManager();
        kitsConfigFile = new File(getDataFolder(), "kits.yml");
        kitsConfig = YamlConfiguration.loadConfiguration(kitsConfigFile);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinLeaveListener(), this);
        pm.registerEvents(new GameMapListener(), this);

        new BukkitRunnable() {

            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() >= 1) {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        ScoreboardManager.updateScoreboard(online);
                    }
                }
            }

        }.runTaskTimer(this, 0, 15);

        getLogger().info("Hybrid Free For All plugin has been STARTED and is WORKING. This took " + (time - System.currentTimeMillis()) + "ms!");
    }

    @Override
    public void onDisable() {
        INSTANCE = null;
        getLogger().info("Hybrid Free For All plugin has been disabled.");
    }

    public static FreeForAllPlugin getInstance() {
        return INSTANCE;
    }

    public GameMapManager getGameMapManager() {
        return gameMapManager;
    }

    public File getKitsConfigFile() {
        return kitsConfigFile;
    }

    public FileConfiguration getKitsConfig() {
        return kitsConfig;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public void reloadKitsConfig() {
        kitsConfig = YamlConfiguration.loadConfiguration(kitsConfigFile);
    }
}
