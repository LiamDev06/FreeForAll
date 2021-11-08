package com.hybrid.ffa.data;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.utils.KitLevelUpdateEvent;
import com.hybrid.ffa.utils.PlayerKit;
import com.hybrid.ffa.utils.Prestige;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CachedUser {

    private final UUID uuid;
    private int coins;

    private int kills, deaths, lifetimeArrowsShot, lifetimeArrowsHit, lifetimeLongestKillStreak;
    private double lifetimeExp;
    private Prestige prestige;

    private final Map<PlayerKit, Integer> kitLevel;
    private final Map<PlayerKit, Double> kitExp;
    private final Map<PlayerKit, Boolean> kitUnlocked;

    public CachedUser(UUID uuid) {
        this.uuid = uuid;
        this.coins = 0;

        this.kills = 0;
        this.deaths = 0;
        this.lifetimeArrowsShot = 0;
        this.lifetimeArrowsHit = 0;
        this.lifetimeLongestKillStreak = 0;
        this.lifetimeExp = 0;
        this.prestige = Prestige.COMING_SOON;

        this.kitLevel = new HashMap<>();
        this.kitExp = new HashMap<>();
        this.kitUnlocked = new HashMap<>();

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

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int amount) {
        this.coins = (coins + amount);
    }

    public void removeCoins(int amount) {
        this.coins = (coins - amount);
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int lifetimeKills) {
        this.kills = lifetimeKills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int lifetimeDeaths) {
        this.deaths = lifetimeDeaths;
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

    public int getLongestKillStreak() {
        return lifetimeLongestKillStreak;
    }

    public void setLongestKillStreak(int lifetimeLongestKillStreak) {
        this.lifetimeLongestKillStreak = lifetimeLongestKillStreak;
    }

    public Double getLifetimeExp() {
        return lifetimeExp;
    }

    public void setLifetimeExp(double lifetimeExp) {
        this.lifetimeExp = lifetimeExp;
    }

    public void addLifetimeExp(double amount) {
        this.lifetimeExp = (lifetimeExp + amount);
    }

    public int getKitLevel(PlayerKit playerKit) {
        return this.kitLevel.get(playerKit);
    }

    public void setKitLevel(PlayerKit playerKit, int level) {
        int before = getKitLevel(playerKit);

        if (this.kitLevel.containsKey(playerKit)) {
            this.kitLevel.replace(playerKit, level);
        } else {
            this.kitLevel.put(playerKit, level);
        }

        KitLevelUpdateEvent event = new KitLevelUpdateEvent(level, before, this.uuid, playerKit);
        Bukkit.getPluginManager().callEvent(event);
    }

    public void setKitLevelSilent(PlayerKit playerKit, int level) {
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

    public void addKitExp(PlayerKit playerKit, double amount) {
        setKitExp(playerKit, getKitExp(playerKit) + amount);
    }

    public void resetExp(PlayerKit kit) {
        int required = FreeForAllPlugin.getInstance().getGameMapManager().getExpMaxRequired(uuid, kit);
        setKitExp(kit, getKitExp(kit) - required);
    }

    public boolean hasUnlockedKit(PlayerKit playerKit) {
        return this.kitUnlocked.get(playerKit);
    }

    public void setKitUnlocked(PlayerKit playerKit, boolean value) {
        if (this.kitUnlocked.containsKey(playerKit)) {
            this.kitUnlocked.replace(playerKit, value);
        } else {
            this.kitUnlocked.put(playerKit, value);
        }
    }

    public String getKD() {
        return String.format("%.1f", (double) getKills() / getDeaths());
    }

    public void setPrestige(Prestige prestige) {
        this.prestige = prestige;
    }

    public Prestige getPrestige() {
        return prestige;
    }

    public void updateCache() {
        if (FreeForAllPlugin.getInstance().getUserManager().getCachedUsers().containsKey(uuid)) {
            FreeForAllPlugin.getInstance().getUserManager().getCachedUsers().replace(uuid, this);
        } else {
            FreeForAllPlugin.getInstance().getUserManager().getCachedUsers().put(uuid, this);
        }
    }

}











