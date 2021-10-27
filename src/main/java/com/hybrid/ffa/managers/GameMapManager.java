package com.hybrid.ffa.managers;

import java.util.HashMap;
import java.util.UUID;

public class GameMapManager {

    //                   Who    Kit Name
    private final HashMap<UUID, String> lastKitUsed;

    public GameMapManager() {
        lastKitUsed = new HashMap<>();
    }

    public HashMap<UUID, String> getLastKitUsed() {
        return lastKitUsed;
    }

}
