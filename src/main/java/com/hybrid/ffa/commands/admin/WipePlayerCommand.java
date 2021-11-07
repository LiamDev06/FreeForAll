package com.hybrid.ffa.commands.admin;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.data.User;
import com.hybrid.ffa.managers.GameMapManager;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class WipePlayerCommand extends PlayerCommand {

    public WipePlayerCommand() {
        super("wipeplayer");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isSeniorModerator()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "no_permission"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cPlease insert a player to wipe. Valid Usage: /wipeplayer <player>");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());
        if (!hybridTarget.hasJoinedServerBefore()) {
            hybridPlayer.sendMessage("&c&lNEVER JOINED! &cThis player has never played on Hybrid before!");
            return;
        }

        User user = new User(offlinePlayer.getUniqueId());
        if (user.getUserFile().delete()) {
            new User(offlinePlayer.getUniqueId());

            GameMapManager manager = FreeForAllPlugin.getInstance().getGameMapManager();
            manager.getKillStreak().replace(offlinePlayer.getUniqueId(), 0);
            manager.getLastKitUsed().remove(offlinePlayer.getUniqueId());

            hybridPlayer.sendMessage("&a&lUSER WIPED! &aYou just thanos snapped &6" + offlinePlayer.getName() + "&a's statistics from earth. Be gone...");
            SoundManager.playSound(player, "NOTE_PLING");
        } else {
            hybridPlayer.sendMessage("&c&lWIPE FAILED! &cSomething went wrong and " + offlinePlayer.getName() + " could not be wiped!");
        }
    }
}


