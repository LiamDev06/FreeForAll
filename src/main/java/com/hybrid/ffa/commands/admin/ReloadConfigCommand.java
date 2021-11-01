package com.hybrid.ffa.commands.admin;

import com.hybrid.ffa.FreeForAllPlugin;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.entity.Player;

public class ReloadConfigCommand extends PlayerCommand {

    public ReloadConfigCommand() {
        super("reloadconfig");
    }

    @Override
    public void onPlayerCommand(Player player, String[] strings) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        try {
            FreeForAllPlugin.getInstance().reloadConfig();
            SoundManager.playSound(player, "NOTE_PIANO");
            hybridPlayer.sendMessage("&a&lSUCCESSFUL RELOAD! &aThe game config has been successfully reloaded without any errors.");

        } catch (Exception exception) {
            hybridPlayer.sendMessage("&c&lRELOAD ERROR! &cSomething went wrong while trying to reload the game config!");
            exception.printStackTrace();
        }
    }

}













