package com.hybrid.ffa.listeners;

import com.connorlinfoot.titleapi.TitleAPI;
import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.data.User;
import com.hybrid.ffa.managers.GameMapManager;
import com.hybrid.ffa.utils.KitLevelUpdateEvent;
import net.hybrid.core.utility.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class KitLevelUpdate implements Listener {

    @EventHandler
    public void onKitLevelUpdate(KitLevelUpdateEvent event) {
        User user = new User(event.getUuid());
        GameMapManager manager = FreeForAllPlugin.getInstance().getGameMapManager();

        Player player = Bukkit.getPlayer(event.getUuid());

        if (player != null) {
            Location loc = player.getLocation();
            int level = event.getNewLevel();

            player.playSound(loc, Sound.ENDERDRAGON_HIT, 10, -1);
            player.playSound(loc, Sound.NOTE_PLING, 10, 2);
            player.playSound(loc, Sound.VILLAGER_YES, 10, 2);

            player.sendMessage(CC.translate("&7&m----------------------------"));
            player.sendMessage(CC.translate("&6&lKIT LEVEL UP!"));
            player.sendMessage(CC.translate("&bYou leveled up the &a" + event.getKit().getDisplayName() + " &bto level &d" + level + "&b!"));

            if (level == 10 || level == 25 || level == 40 || level == 75 || level == 100) {
                player.sendMessage(" ");
                player.sendMessage(CC.translate("&7New kit items have been unlocked!"));
            }

            player.sendMessage(CC.translate("&7&m----------------------------"));

            TitleAPI.sendTitle(player, 10, 40, 10,
                    "&6&kLLL &3&lKIT LEVEL UP &6&kLLL",
                    "");
        }
    }
}










