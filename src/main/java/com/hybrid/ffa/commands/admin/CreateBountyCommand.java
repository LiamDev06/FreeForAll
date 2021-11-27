package com.hybrid.ffa.commands.admin;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.bounty.BountySystem;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CreateBountyCommand extends PlayerCommand {

    public CreateBountyCommand() {
        super("createbounty");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid usage: /createbounty <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            hybridPlayer.sendMessage("&cThis player is not online!");
            return;
        }

        BountySystem system = FreeForAllPlugin.getInstance().getBountySystem();
        if (system.hasBounty(player)) {
            hybridPlayer.sendMessage("&cThis player already has a bounty on them!");
            return;
        }

        if (!FreeForAllPlugin.getInstance().getGameMapManager().getIsInArena().contains(target.getUniqueId())) {
            hybridPlayer.sendMessage("&cThe player needs to be in the arena for a bounty to active on them!");
            return;
        }

        system.createNewBounty(target.getWorld(), target.getName());
        hybridPlayer.sendMessage("&c[ADMIN DEBUG] &6Created a new manual bounty on '" + target.getName() + "'.");
    }

}











