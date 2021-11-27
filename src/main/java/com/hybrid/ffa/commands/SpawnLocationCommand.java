package com.hybrid.ffa.commands;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.utils.LocationUtil;
import com.hybrid.ffa.managers.GameMapManager;
import com.hybrid.ffa.menus.ChangeSpawnMenu;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import net.hybrid.core.utility.bookgui.BookUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SpawnLocationCommand extends PlayerCommand {

    public SpawnLocationCommand() {
        super("spawnlocation");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        GameMapManager manager = FreeForAllPlugin.getInstance().getGameMapManager();

        if (manager.getIsInArena().contains(player.getUniqueId())) {
            player.sendMessage(CC.translate("&c&lCURRENTLY PLAYING! &cYou cannot change kit while currently playing."));
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 10, -2);
            return;
        }

        if (args.length == 0) {
            SoundManager.playSound(player, "CLICK");
            BookUtil.openPlayer(player, ChangeSpawnMenu.getChangeSpawnBook());
            return;
        }

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 12, 2);

        if (args[0].equalsIgnoreCase("winter")) {
            player.sendMessage(CC.translate("&a&lSWAPPING LOCATION! &aYour location has been changed to &6" + "Winter" + "!"));
            player.teleport(LocationUtil.readLocation(FreeForAllPlugin.getInstance().getConfig().getConfigurationSection("spawnLocations.winterLoc")));

            manager.getSpawnLocation().remove(player.getUniqueId());
            manager.getSpawnLocation().put(player.getUniqueId(), "winter");
            return;
        }

        if (args[0].equalsIgnoreCase("plains")) {
            player.sendMessage(CC.translate("&a&lSWAPPING LOCATION! &aYour location has been changed to &6" + "Plains" + "!"));
            player.teleport(LocationUtil.readLocation(FreeForAllPlugin.getInstance().getConfig().getConfigurationSection("spawnLocations.plainsLoc")));

            manager.getSpawnLocation().remove(player.getUniqueId());
            manager.getSpawnLocation().put(player.getUniqueId(), "plains");
            return;
        }

        if (args[0].equalsIgnoreCase("desert")) {
            player.sendMessage(CC.translate("&a&lSWAPPING LOCATION! &aYour location has been changed to &6" + "Desert" + "!"));
            player.teleport( LocationUtil.readLocation(FreeForAllPlugin.getInstance().getConfig().getConfigurationSection("spawnLocations.desertLoc")));

            manager.getSpawnLocation().remove(player.getUniqueId());
            manager.getSpawnLocation().put(player.getUniqueId(), "desert");
            return;
        }

        if (args[0].equalsIgnoreCase("jungle")) {
            player.sendMessage(CC.translate("&a&lSWAPPING LOCATION! &aYour location has been changed to &6" + "Jungle" + "!"));
            player.teleport(LocationUtil.readLocation(FreeForAllPlugin.getInstance().getConfig().getConfigurationSection("spawnLocations.jungleLoc")));

            manager.getSpawnLocation().remove(player.getUniqueId());
            manager.getSpawnLocation().put(player.getUniqueId(), "jungle");
            return;
        }

        player.sendMessage(CC.translate("&c&lUNKNOWN LOCATION! &cThe system does not recognize this location, are you sure you spelled it right?"));
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 10, -2);
    }
}
