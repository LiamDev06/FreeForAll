package com.hybrid.ffa.managers;

import com.hybrid.ffa.utils.PlayerKit;

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

    //Returns the players current kit
    private final HashMap<UUID, PlayerKit> currentKit;

    public GameMapManager() {
        lastKitUsed = new HashMap<>();
        isInArena = new ArrayList<>();
        killStreak = new HashMap<>();
        currentKit = new HashMap<>();
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

    public int getExpRequiredForLevelUp(PlayerKit playerKit) {



        return 100;
    }

}
