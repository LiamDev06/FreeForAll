package com.hybrid.ffa.commands;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.data.User;
import com.hybrid.ffa.menus.KitMenu;
import com.hybrid.ffa.utils.PlayerKit;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class KitCommand extends PlayerCommand {

    public KitCommand() {
        super("kit", "kits");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        if (FreeForAllPlugin.getInstance().getGameMapManager().getIsInArena().contains(player.getUniqueId())) {
            player.sendMessage(CC.translate("&c&lCURRENTLY PLAYING! &cYou cannot change kit while currently playing."));
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 10, -2);
            return;
        }

        if (args.length == 0) {
            player.sendMessage(CC.translate("&7Opening kit menu..."));
            KitMenu.openKitMenu(player);
            player.playSound(player.getLocation(), Sound.WOOD_CLICK, 8, 1);
            return;
        }

        String kitName = "";
        boolean value = true;

        if (args.length == 1) {
            kitName = args[0].toUpperCase();
            value = false;
        }

        if (value) {
            for (String s : args) {
                kitName = s.toUpperCase() + "_";
            }

            kitName = kitName.substring(0, kitName.length() - 1);
        }

        PlayerKit playerKit;

        try {
            playerKit = PlayerKit.valueOf(kitName);
        } catch (Exception exception) {
            player.sendMessage(CC.translate("&c&lINVALID KIT! &cThe kit you entered is not valid. Use &6/kits &cto see all the kits available."));
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 10, -2);
            return;
        }

        User user = new User(player.getUniqueId());
        if (!user.hasUnlockedKit(playerKit)) {
            player.sendMessage(CC.translate("&c&lNOT UNLOCKED! &cYou have not unlocked the &6" + playerKit.getDisplayName() + " &ckit yet!"));
            return;
        }

        FreeForAllPlugin.getInstance().getKitManager().loadKitFancy(player, playerKit,
                user.getKitLevel(playerKit));
    }
}








