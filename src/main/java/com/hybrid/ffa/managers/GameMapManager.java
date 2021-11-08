package com.hybrid.ffa.managers;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.data.CachedUser;
import com.hybrid.ffa.utils.PlayerKit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GameMapManager {

    //                   Who     Kit
    private final HashMap<UUID, PlayerKit> lastKitUsed;

    // Returns if the player is in the arena or still just in the "kit selection stage"
    private final ArrayList<UUID> isInArena;

    // Returns the players kill streak
    private final HashMap<UUID, Integer> killStreak;

    // Returns the players current kit
    private final HashMap<UUID, PlayerKit> currentKit;

    // Returns the players current spawn location
    private final HashMap<UUID, String> spawnLocation;

    public GameMapManager() {
        lastKitUsed = new HashMap<>();
        isInArena = new ArrayList<>();
        killStreak = new HashMap<>();
        currentKit = new HashMap<>();
        spawnLocation = new HashMap<>();
    }

    public HashMap<UUID, PlayerKit> getLastKitUsed() {
        return lastKitUsed;
    }

    public ArrayList<UUID> getIsInArena() {
        return isInArena;
    }

    public HashMap<UUID, Integer> getKillStreak() {
        return killStreak;
    }

    public HashMap<UUID, PlayerKit> getCurrentKit() {
        return currentKit;
    }

    public int getExpMaxRequired(UUID uuid, PlayerKit playerKit) {
        CachedUser user = FreeForAllPlugin.getInstance().getUserManager().getCachedUser(uuid);
        int level = user.getKitLevel(playerKit);
        int closest = getClosestLevelUp(level);

        FileConfiguration config = FreeForAllPlugin.getInstance().getKitsConfig();

        return config.getInt(playerKit.name().toLowerCase() + "." + closest + ".expCost");
    }

    public HashMap<UUID, String> getSpawnLocation() {
        return spawnLocation;
    }


    public static int getClosestLevelUp(int levelFrom) {
        if (levelFrom == 100) {
            return 0;
        }

        if (levelFrom >= 75) {
            return 100;
        }

        if (levelFrom >= 40) {
            return 75;
        }

        if (levelFrom >= 25) {
            return 40;
        }

        if (levelFrom >= 10) {
            return 25;
        }

        if (levelFrom >= 1) {
            return 10;
        }

        return 0;
    }
}









