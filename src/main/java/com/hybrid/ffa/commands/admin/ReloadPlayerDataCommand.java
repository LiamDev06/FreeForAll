package com.hybrid.ffa.commands.admin;

import com.hybrid.ffa.data.User;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ReloadPlayerDataCommand extends PlayerCommand {

    public ReloadPlayerDataCommand() {
        super("reloadplayerdata");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cYou need to specify a player, use /reloadplayerdata <player>");
            SoundManager.playSound(player, "ENDERMAN_TELEPORT");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());
        if (!hybridTarget.hasJoinedServerBefore()) {
            hybridPlayer.sendMessage("&c&lNEVER JOINED! &cThis player has never played on Hybrid before!");
            return;
        }

        try {
            new User(offlinePlayer.getUniqueId()).reloadConfig();
            SoundManager.playSound(player, "NOTE_PIANO");
            hybridPlayer.sendMessage("&a&lSUCCESSFUL RELOAD! &aThe player data config has been successfully reloaded without any errors for player &6" + offlinePlayer.getName() + "&a!");

        } catch (Exception exception) {
            hybridPlayer.sendMessage("&c&lRELOAD ERROR! &cSomething went wrong while trying to reload the player data config for player &6" + offlinePlayer.getName() + "&c!");
            exception.printStackTrace();
        }
    }

}
