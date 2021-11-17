package com.hybrid.ffa.commands.admin;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.managers.GameMapManager;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InArenaDebugCommand extends PlayerCommand {

    public InArenaDebugCommand() {
        super("inarenadebug");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid Usage: &6/inarenadebug <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        GameMapManager manager = FreeForAllPlugin.getInstance().getGameMapManager();

        if (target != null) {
            if (manager.getIsInArena().contains(target.getUniqueId())) {
                hybridPlayer.sendMessage("&eThis player is currently &a&lIN THE ARENA&e.");
            } else {
                hybridPlayer.sendMessage("&eThis player is currently &c&lNOT IN THE ARENA&e.");
            }

        } else {
            hybridPlayer.sendMessage("&cThis player is not online!");
        }

    }

}














