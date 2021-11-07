package com.hybrid.ffa.commands.admin;

import com.hybrid.ffa.data.User;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CoinsCommand extends PlayerCommand {

    public CoinsCommand() {
        super("coins");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid Usage: /coins <player> <check/set/add/remove> <(amount)>");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        HybridPlayer hybridTarget = new HybridPlayer(offlinePlayer.getUniqueId());
        if (!hybridTarget.hasJoinedServerBefore()) {
            hybridPlayer.sendMessage("&c&lNEVER JOINED! &cThis player has never played on Hybrid before!");
            return;
        }

        User userTarget = new User(offlinePlayer.getUniqueId());
        String who = hybridTarget.getRankManager().getRank().getPrefixSpace() + offlinePlayer.getName();

        if (args.length == 1) {
            hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid Usage: /coins <player> <check/set/add/remove> <(amount)>");
            return;
        }

        if (args[1].equalsIgnoreCase("check")) {
            hybridPlayer.sendMessage(hybridTarget.getRankManager().getRank().getPrefixSpace() + offlinePlayer.getName() + " &ahas &b" + userTarget.getCoins() + "&a coins!");
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

                userTarget.setCoins(value);
                hybridPlayer.sendMessage("&a&lCOINS SET! &aYou set &b" + value + " &acoins to " + who + "&a!");
                SoundManager.playSound(player, "NOTE_PLING");

            } else {
                hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid Usage: /coins <player> <set> <amount>");
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

                userTarget.setCoins(userTarget.getCoins() - value);
                hybridPlayer.sendMessage("&a&lCOINS REMOVED! &aYou removed &b" + value + " &acoins from " + who + "&a! They now have &6" + userTarget.getCoins() + "&a coins!");
                SoundManager.playSound(player, "NOTE_PLING");

            } else {
                hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid Usage: /coins <player> <remove> <amount>");
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

                userTarget.setCoins(userTarget.getCoins() + value);
                hybridPlayer.sendMessage("&a&lCOINS ADDED! &aYou added &b" + value + " &acoins to " + who + "&a! They now have &6" + userTarget.getCoins() + "&a coins!");
                SoundManager.playSound(player, "NOTE_PLING");

            } else {
                hybridPlayer.sendMessage("&c&lMISSING ARGUMENTS! &cValid Usage: /coins <player> <add> <amount>");
            }
            return;
        }

        hybridPlayer.sendMessage("&c&lINVALID SUB-COMMAND! &cThis sub-command does not exist! &cValid Usage: /coins <player> <check/set/add/remove> <(amount)>");

    }
}









