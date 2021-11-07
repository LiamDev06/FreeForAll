package com.hybrid.ffa.data;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.utils.KitLevelUpdateEvent;
import com.hybrid.ffa.utils.PlayerKit;
import net.hybrid.core.utility.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class User {

    private final UUID uuid;
    private final File userFile;
    private FileConfiguration userConfig;

    public User(UUID uuid) {
        this.uuid = uuid;

        userFile = new File(FreeForAllPlugin.getInstance().getDataFolder() + "/PlayerData", uuid.toString() + ".yml");
        userConfig = YamlConfiguration.loadConfiguration(userFile);
        if (!userFile.exists()) {
            userConfig.set("playerName", Bukkit.getOfflinePlayer(uuid).getName());
            userConfig.set("uuid", uuid.toString());
            userConfig.set("coins", 0);

            userConfig.set("stats.lifetimeKills", 0);
            userConfig.set("stats.lifetimeDeaths", 0);
            userConfig.set("stats.lifetimeExp", 0);
            userConfig.set("stats.lifetimeArrowsShot", 0);
            userConfig.set("stats.lifetimeArrowsHit", 0);
            userConfig.set("stats.lifetimeLongestKillStreak", 0);

            userConfig.set("kits.swordsman.unlocked", true);
            userConfig.set("kits.swordsman.level", 1);
            userConfig.set("kits.swordsman.exp", 0);

            userConfig.set("kits.archer.unlocked", true);
            userConfig.set("kits.archer.level", 1);
            userConfig.set("kits.archer.exp", 0);

            userConfig.set("kits.samurai.unlocked", true);
            userConfig.set("kits.samurai.level", 1);
            userConfig.set("kits.samurai.exp", 0);

            userConfig.set("kits.tank.unlocked", true);
            userConfig.set("kits.tank.level", 1);
            userConfig.set("kits.tank.exp", 0);

            userConfig.set("kits.wizard.unlocked", false);
            userConfig.set("kits.wizard.level", 1);
            userConfig.set("kits.wizard.exp", 0);

            userConfig.set("kits.well_rounded.unlocked", false);
            userConfig.set("kits.well_rounded.level", 1);
            userConfig.set("kits.well_rounded.exp", 0);

            try {
                userConfig.save(userFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public File getUserFile() {
        return userFile;
    }

    public FileConfiguration getUserConfig() {
        return userConfig;
    }

    public void setCoins(int value) {
        userConfig.set("coins", value);
        save();
    }

    public void addCoins(int value) {
        userConfig.set("coins", getCoins() + value);
        save();
    }

    public void removeCoins(int value) {
        userConfig.set("coins", getCoins() - value);
        save();
    }

    public int getCoins() {
        return userConfig.getInt("coins");
    }

    public void setKills(int value) {
        userConfig.set("stats.lifetimeKills", value);
        save();
    }

    public void setDeaths(int value) {
        userConfig.set("stats.lifetimeDeaths", value);
        save();
    }

    public void setLifetimeExp(double value) {
        userConfig.set("stats.lifetimeExp", value);
        save();
    }

    public void addLifetimeExp(double value) {
        userConfig.set("stats.lifetimeExp", getLifetimeExp() + value);
        save();
    }

    public void setArrowsShot(int value) {
        userConfig.set("stats.lifetimeArrowsShot", value);
        save();
    }

    public void setArrowsHit(int value) {
        userConfig.set("stats.lifetimeArrowsHit", value);
        save();
    }

    public void setLongestKillStreak(int value) {
        userConfig.set("stats.lifetimeLongestKillStreak", value);
        save();
    }

    public int getKills() {
        return userConfig.getInt("stats.lifetimeKills");
    }

    public int getDeaths() {
        return userConfig.getInt("stats.lifetimeDeaths");
    }

    public Double getLifetimeExp() {
        return userConfig.getDouble("stats.lifetimeExp");
    }

    public int getArrowsShot() {
        return userConfig.getInt("stats.lifetimeArrowsShot");
    }

    public int getArrowsHit() {
        return userConfig.getInt("stats.lifetimeArrowsHit");
    }

    public int getLongestKillStreak() {
        return userConfig.getInt("stats.lifetimeLongestKillStreak");
    }

    public String getKD() {
        return String.format("%.1f", (double) getKills() / getDeaths());
    }

    public void setKitLevel(PlayerKit kit, int level) {
        int before = getKitLevel(kit);

        userConfig.set("kits." + kit.name().toLowerCase() + ".level", level);
        save();

        KitLevelUpdateEvent event = new KitLevelUpdateEvent(level, before, uuid, kit);
        Bukkit.getPluginManager().callEvent(event);
    }

    public void setKitExp(PlayerKit kit, double exp) {
        userConfig.set("kits." + kit.name().toLowerCase() + ".exp", exp);
        save();
    }

    public void addKitExp(PlayerKit kit, double value) {
        userConfig.set("kits." + kit.name().toLowerCase() + ".exp", getKitExp(kit) + value);
        save();
    }

    public void removeKitExp(PlayerKit kit, double value) {
        userConfig.set("kits." + kit.name().toLowerCase() + ".exp", getKitExp(kit) - value);
        save();
    }

    public void resetExp(PlayerKit kit) {
        int required = FreeForAllPlugin.getInstance().getGameMapManager().getExpMaxRequired(uuid, kit);
        userConfig.set("kits." + kit.name().toLowerCase() + ".exp", getKitExp(kit) - required);
        save();
    }

    public int getKitLevel(PlayerKit kit) {
        return userConfig.getInt("kits." + kit.name().toLowerCase() + ".level");
    }

    public Double getKitExp(PlayerKit kit) {
        return userConfig.getDouble("kits." + kit.name().toLowerCase() + ".exp");
    }

    public String getPrestige() {
        return CC.translate("&c(Coming Soon)");
    }

    public boolean hasUnlockedKit(PlayerKit playerKit) {
        return userConfig.getBoolean("kits." + playerKit.name().toLowerCase() + ".unlocked");
    }

    private void save() {
        try {
            userConfig.save(userFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void reloadConfig() {
        userConfig = YamlConfiguration.loadConfiguration(userFile);
    }

}
