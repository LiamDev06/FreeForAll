package com.hybrid.ffa;

import com.hybrid.ffa.commands.KitCommand;
import com.hybrid.ffa.listeners.JoinLeaveListener;
import com.hybrid.ffa.managers.GameMapManager;
import com.hybrid.ffa.managers.KitManager;
import com.hybrid.ffa.commands.SpawnLocationCommand;
import com.hybrid.ffa.listeners.GameMapListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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

        gameMapManager = new GameMapManager();
        kitManager = new KitManager();
        kitsConfigFile = new File(getDataFolder(), "kits.yml");
        kitsConfig = YamlConfiguration.loadConfiguration(kitsConfigFile);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinLeaveListener(), this);
        pm.registerEvents(new GameMapListener(), this);

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

}
