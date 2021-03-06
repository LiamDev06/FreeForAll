package com.hybrid.ffa;

import com.hybrid.ffa.commands.KitCommand;
import com.hybrid.ffa.commands.admin.*;
import com.hybrid.ffa.menus.KitMenu;
import com.hybrid.ffa.bounty.BountySystem;
import com.hybrid.ffa.data.UserManager;
import com.hybrid.ffa.listeners.JoinLeaveListener;
import com.hybrid.ffa.listeners.KitLevelUpdate;
import com.hybrid.ffa.listeners.NPCListener;
import com.hybrid.ffa.managers.DeathManager;
import com.hybrid.ffa.managers.GameMapManager;
import com.hybrid.ffa.managers.KitManager;
import com.hybrid.ffa.managers.ScoreboardManager;
import com.hybrid.ffa.commands.SpawnLocationCommand;
import com.hybrid.ffa.listeners.GameMapListener;
import net.hybrid.core.utility.actionbar.ActionbarAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class FreeForAllPlugin extends JavaPlugin {

    private static FreeForAllPlugin INSTANCE;
    private GameMapManager gameMapManager;
    private UserManager userManager;
    private KitManager kitManager;
    private BountySystem bountySystem;
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
        new LoadPlayerDataFromConfigCommand();
        new WipePlayerCommand();
        new CoinsCommand();
        new KillsCommand();
        new DeathsCommand();
        new SetKitLevel();
        new CheckKillstreakCommand();
        new UploadPlayerDataToConfigCommand();
        new InArenaDebugCommand();
        new LockRegionSpawnCommand();
        new CreateBountyCommand();

        userManager = new UserManager(this);
        gameMapManager = new GameMapManager();
        kitManager = new KitManager();
        bountySystem = new BountySystem();
        kitsConfigFile = new File(getDataFolder(), "kits.yml");
        kitsConfig = YamlConfiguration.loadConfiguration(kitsConfigFile);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinLeaveListener(), this);
        pm.registerEvents(new GameMapListener(), this);
        pm.registerEvents(new NPCListener(), this);
        pm.registerEvents(new KitMenu(), this);
        pm.registerEvents(new DeathManager(), this);
        pm.registerEvents(new KitLevelUpdate(), this);

        ActionbarAPI.init();
        GameMapListener.init();

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

        final int cooldown = 5;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() >= 2 && bountySystem.getBountyCache().size() != Bukkit.getOnlinePlayers().size()) {
                    bountySystem.createNewBounty(Bukkit.getWorld("ffa_main"), "IGNORE");
                }
            }
        }.runTaskTimer(this, (20 * 60) * cooldown, (20 * 60) * cooldown);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (bountySystem.hasBounty(player)) {
                    if (!BountySystem.stands.containsKey(player.getUniqueId())) return;

                    ArmorStand as = BountySystem.stands.get(player.getUniqueId());
                    as.teleport(player.getLocation().add(0, .1, 0));
                } else if (BountySystem.stands.containsKey(player.getUniqueId())){
                    BountySystem.stands.get(player.getUniqueId()).remove();
                    BountySystem.stands.remove(player.getUniqueId());
                }
            }
        },0L, 1L);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (bountySystem.hasBounty(player)) {
                    if (bountySystem.getBounty(player).getTaskCunt() == 0) {
                        bountySystem.bountyExpired(player);
                    } else {
                        bountySystem.getBounty(player).decreaseTaskCount();
                    }
                }
            }
        },0L, 20L);

        getLogger().info("Hybrid Free For All plugin has been STARTED and is WORKING. This took " + (time - System.currentTimeMillis()) + "ms!");
    }

    @Override
    public void onDisable() {
        userManager.offLoadPlayersFromCache();
        userManager.getCachedUsers().clear();
        BountySystem.stands.values().forEach(Entity::remove);

        bountySystem.getBountyCache().clear();

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

    public UserManager getUserManager() {
        return userManager;
    }

    public BountySystem getBountySystem() {
        return bountySystem;
    }

    public void reloadKitsConfig() {
        kitsConfig = YamlConfiguration.loadConfiguration(kitsConfigFile);
    }
}
