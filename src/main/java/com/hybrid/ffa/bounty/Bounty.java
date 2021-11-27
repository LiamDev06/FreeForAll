package com.hybrid.ffa.bounty;

import com.hybrid.ffa.FreeForAllPlugin;

import java.util.UUID;

public class Bounty {

    private UUID owner;
    private int taskCunt;

    public Bounty(UUID owner) {
        this.owner = owner;
        this.taskCunt = 60;
    }

    public UUID getOwner() {
        return owner;
    }

    public Bounty setOwner(UUID owner) {
        this.owner = owner;
        return this;
    }

    public void setTaskCunt(int taskCunt) {
        this.taskCunt = taskCunt;
    }

    public void decreaseTaskCount() {
        this.taskCunt = this.taskCunt - 1;
    }

    public void increaseTaskCount() {
        this.taskCunt = this.taskCunt + 1;
    }

    public int getTaskCunt() {
        return taskCunt;
    }

    public Bounty save() {
        if (FreeForAllPlugin.getInstance().getBountySystem().getBountyCache().containsKey(owner)) {
            FreeForAllPlugin.getInstance().getBountySystem().getBountyCache().replace(owner, this);
        } else {
            FreeForAllPlugin.getInstance().getBountySystem().getBountyCache().put(owner, this);
        }

        return this;
    }

}
