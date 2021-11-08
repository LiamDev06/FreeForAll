package com.hybrid.ffa.data;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.utils.PlayerKit;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;

public class CachedUser {

    private final UUID uuid;
    private String playerName;
    private int coins;

    private int lifetimeKills, lifetimeDeaths, lifetimeArrowsShot, lifetimeArrowsHit, lifetimeLongestKillStreak;
    private double lifetimeExp;

    private Map<PlayerKit, Integer> kitLevel;
    private Map<PlayerKit, Double> kitExp;
    private Map<PlayerKit, Boolean> kitUnlocked;

    public CachedUser(UUID uuid) {
        this.uuid = uuid;
        this.playerName = Bukkit.getOfflinePlayer(uuid).getName();
        this.coins = 0;

        this.lifetimeKills = 0;
        this.lifetimeDeaths = 0;
        this.lifetimeArrowsShot = 0;
        this.lifetimeArrowsHit = 0;
        this.lifetimeLongestKillStreak = 0;
        this.lifetimeExp = 0;

        this.kitLevel.put(PlayerKit.SWORDSMAN, 1);
        this.kitLevel.put(PlayerKit.ARCHER, 1);
        this.kitLevel.put(PlayerKit.SAMURAI, 1);
        this.kitLevel.put(PlayerKit.TANK, 1);
        this.kitLevel.put(PlayerKit.WIZARD, 1);
        this.kitLevel.put(PlayerKit.WELL_ROUNDED, 1);

        this.kitExp.put(PlayerKit.SWORDSMAN, 0D);
        this.kitExp.put(PlayerKit.ARCHER, 0D);
        this.kitExp.put(PlayerKit.SAMURAI, 0D);
        this.kitExp.put(PlayerKit.TANK, 0D);
        this.kitExp.put(PlayerKit.WIZARD, 0D);
        this.kitExp.put(PlayerKit.WELL_ROUNDED, 0D);

        this.kitUnlocked.put(PlayerKit.SWORDSMAN, true);
        this.kitUnlocked.put(PlayerKit.ARCHER, true);
        this.kitUnlocked.put(PlayerKit.SAMURAI, true);
        this.kitUnlocked.put(PlayerKit.TANK, true);
        this.kitUnlocked.put(PlayerKit.WIZARD, false);
        this.kitUnlocked.put(PlayerKit.WELL_ROUNDED, false);

        FreeForAllPlugin.getInstance().getUserManager().getCachedUsers().put(uuid, this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getLifetimeKills() {
        return lifetimeKills;
    }

    public void setLifetimeKills(int lifetimeKills) {
        this.lifetimeKills = lifetimeKills;
    }

    public int getLifetimeDeaths() {
        return lifetimeDeaths;
    }

    public void setLifetimeDeaths(int lifetimeDeaths) {
        this.lifetimeDeaths = lifetimeDeaths;
    }

    public int getLifetimeArrowsShot() {
        return lifetimeArrowsShot;
    }

    public void setLifetimeArrowsShot(int lifetimeArrowsShot) {
        this.lifetimeArrowsShot = lifetimeArrowsShot;
    }

    public int getLifetimeArrowsHit() {
        return lifetimeArrowsHit;
    }

    public void setLifetimeArrowsHit(int lifetimeArrowsHit) {
        this.lifetimeArrowsHit = lifetimeArrowsHit;
    }

    public int getLifetimeLongestKillStreak() {
        return lifetimeLongestKillStreak;
    }

    public void setLifetimeLongestKillStreak(int lifetimeLongestKillStreak) {
        this.lifetimeLongestKillStreak = lifetimeLongestKillStreak;
    }

    public Double getLifetimeExp() {
        return lifetimeExp;
    }

    public void setLifetimeExp(double lifetimeExp) {
        this.lifetimeExp = lifetimeExp;
    }

    public int getKitLevel(PlayerKit playerKit) {
        return this.kitLevel.get(playerKit);
    }

    public void setKitLevel(PlayerKit playerKit, int level) {
        if (this.kitLevel.containsKey(playerKit)) {
            this.kitLevel.replace(playerKit, level);
        } else {
            this.kitLevel.put(playerKit, level);
        }
    }

    public Double getKitExp(PlayerKit playerKit) {
        return this.kitExp.get(playerKit);
    }

    public void setKitExp(PlayerKit playerKit, double exp) {
        if (this.kitExp.containsKey(playerKit)) {
            this.kitExp.replace(playerKit, exp);
        } else {
            this.kitExp.put(playerKit, exp);
        }
    }

    public boolean kitIsUnlocked(PlayerKit playerKit) {
        return this.kitUnlocked.get(playerKit);
    }

    public void setKitUnlocked(PlayerKit playerKit, boolean value) {
        if (this.kitUnlocked.containsKey(playerKit)) {
            this.kitUnlocked.replace(playerKit, value);
        } else {
            this.kitUnlocked.put(playerKit, value);
        }
    }

    public void updateCache() {
        if (FreeForAllPlugin.getInstance().getUserManager().getCachedUsers().containsKey(uuid)) {
            FreeForAllPlugin.getInstance().getUserManager().getCachedUsers().replace(uuid, this);
        } else {
            FreeForAllPlugin.getInstance().getUserManager().getCachedUsers().put(uuid, this);
        }
    }

}











