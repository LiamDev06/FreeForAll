package com.hybrid.ffa.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GameMapManager {

    //                   Who    Kit Name
    private final HashMap<UUID, String> lastKitUsed;

    // Returns if the player is in the arena or still just in the "kit selection stage"
    private final ArrayList<UUID> isInArena;

    // Returns the players kill streak
    private final HashMap<UUID, Integer> killStreak;

    public GameMapManager() {
        lastKitUsed = new HashMap<>();
        isInArena = new ArrayList<>();
        killStreak = new HashMap<>();
    }

    public HashMap<UUID, String> getLastKitUsed() {
        return lastKitUsed;
    }

    public ArrayList<UUID> getIsInArena() {
        return isInArena;
    }

    public HashMap<UUID, Integer> getKillStreak() {
        return killStreak;
    }

}
