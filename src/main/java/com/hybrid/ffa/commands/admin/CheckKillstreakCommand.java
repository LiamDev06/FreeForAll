package com.hybrid.ffa.commands.admin;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.data.CachedUser;
import com.hybrid.ffa.data.UserManager;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CheckKillstreakCommand extends PlayerCommand {

    public CheckKillstreakCommand() {
        super("checkkillstreak");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid usage: /checkkillstreak <player>");
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

        CachedUser user = userManager.getCachedUser(offlinePlayer.getUniqueId());
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 10, 2);
        hybridPlayer.sendMessage("&aLifetime Longest Killstreak: &6" + user.getLongestKillStreak());
        hybridPlayer.sendMessage("&aCurrent Killstreak: &6" +
                FreeForAllPlugin.getInstance().getGameMapManager().getKillStreak().get(offlinePlayer.getUniqueId()));
    }
}











