package com.hybrid.ffa.commands.admin;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.data.UserManager;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class UploadPlayerDataToConfigCommand extends PlayerCommand {

    public UploadPlayerDataToConfigCommand() {
        super("uploadplayerdata");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cYou need to specify a player, use /uploadplayerdata <player>");
            SoundManager.playSound(player, "ENDERMAN_TELEPORT");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());
        if (!hybridTarget.hasJoinedServerBefore()) {
            hybridPlayer.sendMessage("&c&lNEVER JOINED! &cThis player has never played on Hybrid before!");
            return;
        }

        UserManager userManager = FreeForAllPlugin.getInstance().getUserManager();

        if (!userManager.hasPlayedFFABefore(offlinePlayer.getUniqueId())) {
            hybridPlayer.sendMessage("&c&lNEVER PLAYED! &cThis player has never played Free For All before!");
            return;
        }

        try {
            userManager.offLoadPlayerFromCache(player.getUniqueId());
            userManager.loadPlayerToCache(player.getUniqueId());

            SoundManager.playSound(player, "NOTE_PIANO");
            hybridPlayer.sendMessage("&a&lSUCCESSFUL UPLOAD! &aYou uploaded the player data from &6" + offlinePlayer.getName() + "'s&a to the config without errors&a!");

        } catch (Exception exception) {
            hybridPlayer.sendMessage("&c&lRELOAD ERROR! &cSomething went wrong while trying to reload the player data config for player &6" + offlinePlayer.getName() + "&c!");
            exception.printStackTrace();
        }
    }

}
