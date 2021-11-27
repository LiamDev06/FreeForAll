package com.hybrid.ffa.commands.admin;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.managers.GameMapManager;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.entity.Player;

public class LockRegionSpawnCommand extends PlayerCommand {

    public LockRegionSpawnCommand() {
        super("lockregionspawn");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid Usage: &6/lockregionspawn <region>");
            return;
        }

        String region = args[0];
        GameMapManager manager = FreeForAllPlugin.getInstance().getGameMapManager();

        if (region.equalsIgnoreCase("reset")) {
            hybridPlayer.sendMessage("&c[ADMIN DEBUG] &6The region spawn lock has been reset and removed.");
            manager.resetRegionSpawnLock();
            return;
        }

        if (region.equalsIgnoreCase("winter")) {
            hybridPlayer.sendMessage("&c[ADMIN DEBUG] &6The region spawn has been locked to &3" + "Winter" + "&6.");
            manager.setRegionSpawnLock("winter");
            return;
        }

        if (region.equalsIgnoreCase("plains")) {
            hybridPlayer.sendMessage("&c[ADMIN DEBUG] &6The region spawn has been locked to &3" + "Plains" + "&6.");
            manager.setRegionSpawnLock("plains");
            return;
        }

        if (region.equalsIgnoreCase("desert")) {
            hybridPlayer.sendMessage("&c[ADMIN DEBUG] &6The region spawn has been locked to &3" + "Desert" + "&6.");
            manager.setRegionSpawnLock("desert");
            return;
        }

        if (region.equalsIgnoreCase("jungle")) {
            hybridPlayer.sendMessage("&c[ADMIN DEBUG] &6The region spawn has been locked to &3" + "Jungle" + "&6.");
            manager.setRegionSpawnLock("jungle");
            return;
        }

        hybridPlayer.sendMessage("&c&lNOT FOUND! &cThe region you entered (&6" + region + "&c) could not be found!");
    }

}










