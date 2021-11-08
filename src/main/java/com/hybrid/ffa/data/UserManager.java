package com.hybrid.ffa.data;

import com.hybrid.ffa.utils.PlayerKit;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class UserManager {

    private final JavaPlugin plugin;
    private final HashMap<UUID, CachedUser> cachedUsers;

    public UserManager(JavaPlugin plugin) {
        this.plugin = plugin;
        cachedUsers = new HashMap<>();
    }

    public CachedUser getCachedUser(UUID uuid) {
        return this.cachedUsers.get(uuid);
    }

    public void offLoadPlayerFromCache(UUID uuid) {
        File file = new File(plugin.getDataFolder() + "/PlayerData", uuid.toString() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        CachedUser cachedUser = this.cachedUsers.get(uuid);
        config.set("coins", cachedUser.getCoins());
        config.set("stats.lifetimeKills", cachedUser.getLifetimeKills());
        config.set("stats.lifetimeDeaths", cachedUser.getLifetimeDeaths());
        config.set("stats.lifetimeExp", cachedUser.getLifetimeExp());
        config.set("stats.lifetimeArrowsShot", cachedUser.getLifetimeArrowsShot());
        config.set("stats.lifetimeArrowsHit", cachedUser.getLifetimeArrowsHit());
        config.set("stats.lifetimeLongestKillStreak", cachedUser.getLifetimeLongestKillStreak());

        for (PlayerKit playerKit : PlayerKit.values()) {
            config.set("kits." + playerKit.name().toLowerCase() + ".unlocked", cachedUser.kitIsUnlocked(playerKit));
            config.set("kits." + playerKit.name().toLowerCase() + ".level", cachedUser.getKitLevel(playerKit));
            config.set("kits." + playerKit.name().toLowerCase() + ".exp", cachedUser.getKitExp(playerKit));
        }
    }

    public void offLoadPlayersFromCache() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            offLoadPlayerFromCache(online.getUniqueId());
        }
    }

    public void loadPlayerToCache(UUID uuid) {
        File file = new File(plugin.getDataFolder() + "/PlayerData", uuid.toString() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        CachedUser cachedUser = new CachedUser(uuid);
        cachedUser.setCoins(config.getInt("coins"));
        cachedUser.setLifetimeKills(config.getInt("stats.lifetimeKills"));
        cachedUser.setLifetimeDeaths(config.getInt("stats.lifetimeDeaths"));
        cachedUser.setLifetimeExp(config.getDouble("stats.lifetimeExp"));
        cachedUser.setLifetimeArrowsShot(config.getInt("stats.lifetimeArrowsShot"));
        cachedUser.setLifetimeArrowsHit(config.getInt("stats.lifetimeArrowsHit"));
        cachedUser.setLifetimeLongestKillStreak(config.getInt("stats.lifetimeLongestKillStreak"));

        for (PlayerKit kit : PlayerKit.values()) {
            cachedUser.setKitUnlocked(kit, config.getBoolean("kits." + kit.name().toLowerCase() + ".unlocked"));
            cachedUser.setKitLevel(kit, config.getInt("kits." + kit.name().toLowerCase() + ".level"));
            cachedUser.setKitExp(kit, config.getDouble("kits." + kit.name().toLowerCase() + ".exp"));
        }

        cachedUser.updateCache();
    }

    public HashMap<UUID, CachedUser> getCachedUsers() {
        return cachedUsers;
    }
}
