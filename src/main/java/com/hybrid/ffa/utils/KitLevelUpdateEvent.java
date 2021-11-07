package com.hybrid.ffa.utils;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class KitLevelUpdateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final int newLevel;
    private final int oldLevel;
    private final UUID uuid;
    private final PlayerKit kit;

    public KitLevelUpdateEvent(int newLevel, int oldLevel, UUID uuid, PlayerKit kit) {
        this.newLevel = newLevel;
        this.oldLevel = oldLevel;
        this.uuid = uuid;
        this.kit = kit;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public UUID getUuid() {
        return uuid;
    }

    public PlayerKit getKit() {
        return kit;
    }
}
