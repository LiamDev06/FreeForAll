package com.hybrid.ffa.utils;

import net.hybrid.core.utility.CC;

public enum Prestige {

    COMING_SOON(CC.translate("&cComing Soon"));

    private final String displayName;

    Prestige(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
