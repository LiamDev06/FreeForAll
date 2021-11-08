package com.hybrid.ffa.commands.admin;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.data.CachedUser;
import com.hybrid.ffa.data.UserManager;
import com.hybrid.ffa.utils.PlayerKit;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class SetKitLevel extends PlayerCommand {

    public SetKitLevel() {
        super("setkitlevel");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid Usage: /setkitlevel <player> <kit> <level>");
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

        CachedUser userTarget = userManager.getCachedUser(offlinePlayer.getUniqueId());
        String who = hybridTarget.getRankManager().getRank().getPrefixSpace() + offlinePlayer.getName();

        if (args.length == 1) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid Usage: /setkitlevel <player> <kit> <level>");
            return;
        }

        PlayerKit playerKit;

        try {
            playerKit = PlayerKit.valueOf(args[1].toUpperCase());
        } catch (Exception exception) {
            player.sendMessage(CC.translate("&c&lINVALID KIT! &cThe kit you entered is not valid!"));
            return;
        }

        if (args.length == 2) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid Usage: /setkitlevel <player> <kit> <level>");
            return;
        }

        int level;
        try {
            level = Integer.parseInt(args[2]);
        } catch (Exception exception) {
            player.sendMessage(CC.translate("&c&lINVALID LEVEL! &cThe level you entered is not valid!"));
            return;
        }

        userTarget.setKitLevel(playerKit, level);
        hybridPlayer.sendMessage("&a&lKIT LEVEL SET! &aYou set the kit level of kit &6" + playerKit.getDisplayName() + " &ato &b" + level + " &afor " + who);
        SoundManager.playSound(player, "NOTE_PLING");
    }
}
















