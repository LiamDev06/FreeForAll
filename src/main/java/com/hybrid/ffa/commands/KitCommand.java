package com.hybrid.ffa.commands;

import com.hybrid.ffa.managers.KitManager;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.entity.Player;

public class KitCommand extends PlayerCommand {

    public KitCommand() {
        super("kit", "kits");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {

        if (args.length <= 1) {
            player.sendMessage(CC.translate("&cInsert a kit and level!"));
            return;
        }

        KitManager.loadKitFromConfig(player, args[0], Integer.parseInt(args[1]));
        player.sendMessage(CC.translate("&aTesting Complete."));
    }

}








