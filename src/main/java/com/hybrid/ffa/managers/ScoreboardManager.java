package com.hybrid.ffa.managers;

import com.hybrid.ffa.data.User;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.ScoreHelper;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScoreboardManager {

    public static void createScoreboard(Player player) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        User user = new User(player.getUniqueId());

        ScoreHelper helper = ScoreHelper.createScore(player);
        helper.setTitle("&2&lHybrid Network");
        helper.setSlot(16, "&7" + getDate());
        helper.setSlot(15, "");
        helper.setSlot(14, "&aCoins: &f" + user.getCoins());
        helper.setSlot(13, "&aTotal EXP: &f" + user.getLifetimeExp());
        helper.setSlot(12, "");
        helper.setSlot(11, "&aKills: &f" + user.getKills());
        helper.setSlot(10, "&aDeaths: &f" + user.getDeaths());

        if (user.getKills() == 0 && user.getDeaths() == 0) {
            helper.setSlot(9, "&aK/D: &fN/A");

        } else if (user.getDeaths() == 0) {
            helper.setSlot(9, "&aK/D: &f" + user.getKills());

        } else {
            helper.setSlot(9, "&aK/D: &f" + user.getKD());
        }

        helper.setSlot(8, "");
        helper.setSlot(7, "&aKit Level: &fNo Kit Selected");
        helper.setSlot(6, "&aKit EXP: &fNo Kit Selected");
        helper.setSlot(5, "");
        helper.setSlot(4, "&aRank: " + hybridPlayer.getRankManager().getRank().getDisplayName());
        helper.setSlot(3, "&aMap: &f%%alfiMapName%%");
        helper.setSlot(2, "");
        helper.setSlot(1, "&ehybridplays.com");
    }

    public static void updateScoreboard(Player player) {

    }

    private static String getDate() {
        DateTimeFormatter year = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter month = DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter day = DateTimeFormatter.ofPattern("dd");
        LocalDateTime now = LocalDateTime.now();

        // MM dd yyyy
        return month.format(now) + "/" + day.format(now) + "/" + year.format(now).substring(2);
    }
}












