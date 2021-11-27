package com.hybrid.ffa.data;

import com.hybrid.ffa.utils.PlayerKit;
import com.hybrid.ffa.utils.Prestige;
import net.hybrid.core.utility.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
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
        if (cachedUsers.containsKey(uuid)) {
            return this.cachedUsers.get(uuid);
        }

        if (existsInStorage(uuid)) {
            loadPlayerToCache(uuid);
            return this.cachedUsers.get(uuid);
        }

        return new CachedUser(uuid);
    }

    public void offLoadPlayerFromCache(UUID uuid) {
        File file = new File(plugin.getDataFolder() + "/PlayerData", uuid.toString() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        CachedUser cachedUser = this.cachedUsers.get(uuid);
        if (config.get("uuid") == null) {
            config.set("uuid", uuid.toString());
        }

        config.set("playerName", Bukkit.getOfflinePlayer(uuid).getName());
        config.set("coins", cachedUser.getCoins());
        config.set("prestige", cachedUser.getPrestige().name());
        config.set("stats.lifetimeKills", cachedUser.getKills());
        config.set("stats.lifetimeDeaths", cachedUser.getDeaths());
        config.set("stats.lifetimeExp", cachedUser.getLifetimeExp());
        config.set("stats.lifetimeArrowsShot", cachedUser.getLifetimeArrowsShot());
        config.set("stats.lifetimeArrowsHit", cachedUser.getLifetimeArrowsHit());
        config.set("stats.lifetimeLongestKillStreak", cachedUser.getLongestKillStreak());

        for (PlayerKit playerKit : PlayerKit.values()) {
            config.set("kits." + playerKit.name().toLowerCase() + ".unlocked", cachedUser.hasUnlockedKit(playerKit));
            config.set("kits." + playerKit.name().toLowerCase() + ".level", cachedUser.getKitLevel(playerKit));
            config.set("kits." + playerKit.name().toLowerCase() + ".exp", cachedUser.getKitExp(playerKit));
        }

        try {
            config.save(file);
        } catch (IOException exception) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&c[ERROR] Something went wrong with off-loading a user file. See the UserManager class line 63"));
        }

        cachedUsers.remove(uuid);
    }

    public void offLoadPlayersFromCache() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            offLoadPlayerFromCache(online.getUniqueId());
        }

        cachedUsers.keySet().forEach(this::offLoadPlayerFromCache);
    }

    public void loadPlayerToCache(UUID uuid) {
        File file = new File(plugin.getDataFolder() + "/PlayerData", uuid.toString() + ".yml");
        CachedUser cachedUser = new CachedUser(uuid);

        if (!file.exists()) {
            cachedUser.updateCache();
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        cachedUser.setCoins(config.getInt("coins"));
        cachedUser.setPrestige(Prestige.valueOf(config.getString("prestige")));
        cachedUser.setKills(config.getInt("stats.lifetimeKills"));
        cachedUser.setDeaths(config.getInt("stats.lifetimeDeaths"));
        cachedUser.setLifetimeExp(config.getDouble("stats.lifetimeExp"));
        cachedUser.setLifetimeArrowsShot(config.getInt("stats.lifetimeArrowsShot"));
        cachedUser.setLifetimeArrowsHit(config.getInt("stats.lifetimeArrowsHit"));
        cachedUser.setLongestKillStreak(config.getInt("stats.lifetimeLongestKillStreak"));

        for (PlayerKit kit : PlayerKit.values()) {
            cachedUser.setKitUnlocked(kit, config.getBoolean("kits." + kit.name().toLowerCase() + ".unlocked"));
            cachedUser.setKitLevelSilent(kit, config.getInt("kits." + kit.name().toLowerCase() + ".level"));
            cachedUser.setKitExp(kit, config.getDouble("kits." + kit.name().toLowerCase() + ".exp"));
        }

        cachedUser.updateCache();
    }

    public HashMap<UUID, CachedUser> getCachedUsers() {
        return cachedUsers;
    }

    public boolean existsInStorage(UUID uuid) {
        return new File(plugin.getDataFolder() + "/PlayerData", uuid.toString() + ".yml").exists();
    }

    public boolean hasPlayedFFABefore(UUID uuid) {
        return existsInStorage(uuid) || cachedUsers.containsKey(uuid);
    }

}









