package com.hybrid.ffa.commands;

import com.hybrid.ffa.FreeForAllPlugin;
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

        if (FreeForAllPlugin.getInstance().gameMapManager().getIsInArena().contains(player.getUniqueId())) {
            player.sendMessage(CC.translate("&c&lCURRENTLY PLAYING! &cYou cannot change kit while currently playing."));
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 10, -2);
            return;
        }

        if (args.length == 0) {
            player.sendMessage(CC.translate("&c&lERROR! &cMissing arguments, valid usage is: /kit <kit>"));
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 10, -2);
            return;
        }

        String kitName = "";
        boolean value = true;

        if (args.length == 1) {
            kitName = args[0].toUpperCase();
            value = false;
        }

        for (String s : args) {
            kitName = s.toUpperCase() + "_";
        }

        if (value) kitName = kitName.substring(0, kitName.length() - 1);
        PlayerKit playerKit;

        try {
            playerKit = PlayerKit.valueOf(kitName);
        } catch (Exception exception) {
            player.sendMessage(CC.translate("&c&lINVALID KIT! &cThe kit you entered is not valid. Use &6/kits &cto see all the kits available."));
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 10, -2);
            return;
        }

        FreeForAllPlugin.getInstance().getKitManager().loadKitFancy(player, playerKit, 1);
    }
}








