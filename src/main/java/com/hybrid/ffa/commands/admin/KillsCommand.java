package com.hybrid.ffa.commands.admin;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.data.CachedUser;
import com.hybrid.ffa.data.UserManager;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class KillsCommand extends PlayerCommand {

    public KillsCommand() {
        super("kills");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid Usage: /kills <player> <check/set/add/remove> <(amount)>");
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
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid Usage: /kills <player> <check/set/add/remove> <(amount)>");
            return;
        }

        if (args[1].equalsIgnoreCase("check")) {
            hybridPlayer.sendMessage(hybridTarget.getRankManager().getRank().getPrefixSpace() + offlinePlayer.getName() + " &ahas &b" + userTarget.getKills() + "&a kills!");
            return;
        }

        if (args[1].equalsIgnoreCase("set")) {
            if (args.length > 2) {
                int value;

                try {
                    value = Integer.parseInt(args[2]);
                } catch (Exception exception) {
                    hybridPlayer.sendMessage("&c&lINVALID! &cInvalid amount value!");
                    return;
                }

                userTarget.setKills(value);
                hybridPlayer.sendMessage("&a&lKILLS SET! &aYou set &b" + value + " &akills to " + who + "&a!");
                SoundManager.playSound(player, "NOTE_PLING");

            } else {
                hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid Usage: /kills <player> <set> <amount>");
            }
            return;
        }

        if (args[1].equalsIgnoreCase("remove")) {
            if (args.length > 2) {
                int value;

                try {
                    value = Integer.parseInt(args[2]);
                } catch (Exception exception) {
                    hybridPlayer.sendMessage("&c&lINVALID! &cInvalid amount value!");
                    return;
                }

                userTarget.setKills(userTarget.getKills() - value);
                hybridPlayer.sendMessage("&a&lKILLS REMOVED! &aYou removed &b" + value + " &akills from " + who + "&a! They now have &6" + userTarget.getKills() + "&a kills!");
                SoundManager.playSound(player, "NOTE_PLING");

            } else {
                hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid Usage: /kills <player> <remove> <amount>");
            }
            return;
        }

        if (args[1].equalsIgnoreCase("add")) {
            if (args.length > 2) {
                int value;

                try {
                    value = Integer.parseInt(args[2]);
                } catch (Exception exception) {
                    hybridPlayer.sendMessage("&c&lINVALID! &cInvalid amount value!");
                    return;
                }

                userTarget.setKills(userTarget.getKills() + value);
                hybridPlayer.sendMessage("&a&lKILLS ADDED! &aYou added &b" + value + " &akills to " + who + "&a! They now have &6" + userTarget.getKills() + "&a kills!");
                SoundManager.playSound(player, "NOTE_PLING");

            } else {
                hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid Usage: /kills <player> <add> <amount>");
            }
            return;
        }

        hybridPlayer.sendMessage("&c&lINVALID SUB-COMMAND! &cThis sub-command does not exist! &cValid Usage: /kills <player> <check/set/add/remove> <(amount)>");

    }

}
