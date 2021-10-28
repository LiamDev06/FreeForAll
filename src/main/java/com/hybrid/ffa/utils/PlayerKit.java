package com.hybrid.ffa.utils;

import com.hybrid.ffa.FreeForAllPlugin;

public enum PlayerKit {

    SWORDSMAN(FreeForAllPlugin.getInstance().getKitsConfig().getString("swordsman.displayName"),
            FreeForAllPlugin.getInstance().getKitsConfig().getBoolean("swordsman.unlockedByDefault")),
    ARCHER(FreeForAllPlugin.getInstance().getKitsConfig().getString("archer.displayName"),
            FreeForAllPlugin.getInstance().getKitsConfig().getBoolean("archer.unlockedByDefault")),
    SAMURAI(FreeForAllPlugin.getInstance().getKitsConfig().getString("samurai.displayName"),
            FreeForAllPlugin.getInstance().getKitsConfig().getBoolean("samurai.unlockedByDefault")),
    TANK(FreeForAllPlugin.getInstance().getKitsConfig().getString("tank.displayName"),
            FreeForAllPlugin.getInstance().getKitsConfig().getBoolean("tank.unlockedByDefault")),
    WIZARD(FreeForAllPlugin.getInstance().getKitsConfig().getString("wizard.displayName"),
            FreeForAllPlugin.getInstance().getKitsConfig().getBoolean("wizard.unlockedByDefault")),
    WELL_ROUNDED(FreeForAllPlugin.getInstance().getKitsConfig().getString("well_rounded.displayName"),
            FreeForAllPlugin.getInstance().getKitsConfig().getBoolean("well_rounded.unlockedByDefault"));

    private final String displayName;
    private final boolean unlockedByDefault;

    PlayerKit(String displayName, boolean unlockedByDefault) {
        this.displayName = displayName;
        this.unlockedByDefault = unlockedByDefault;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isUnlockedByDefault() {
        return unlockedByDefault;
    }
}
