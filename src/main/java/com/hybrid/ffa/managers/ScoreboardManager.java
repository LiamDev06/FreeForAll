package com.hybrid.ffa.managers;

import com.hybrid.ffa.data.User;
import com.hybrid.ffa.FreeForAllPlugin;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.ScoreHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private static final GameMapManager gameMapManager = FreeForAllPlugin.getInstance().getGameMapManager();
    private static final Map<UUID, Integer> tasks = new HashMap<>();
    private static int taskId;

    public static void setId(UUID uuid, int id) {
        tasks.put(uuid, id);
    }

    public static boolean hasId(UUID uuid) {
        return tasks.containsKey(uuid);
    }

    public static void stop(UUID uuid) {
        Bukkit.getScheduler().cancelTask(tasks.get(uuid));
        tasks.remove(uuid);
    }

    public static void start(Player player) {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(FreeForAllPlugin.getInstance(), new Runnable() {

            final UUID uuid = player.getUniqueId();
            int count = 0;
            final int max = 49;
            final ScoreHelper scoreHelper = ScoreHelper.getByPlayer(player);
            final String gold = CC.translate("&6&l");
            final String yellow = CC.translate("&e&l");

            @Override
            public void run() {
                if (!hasId(uuid) && player != null) {
                    setId(uuid, taskId);
                }

                if (count == max) {
                    count = 0;
                }

                switch (count) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 41:
                    case 42:
                    case 43:
                    case 34:
                    case 35:
                    case 36:
                    case 19:
                        scoreHelper.setTitle("&e&lFREE FOR ALL");
                        break;
                    case 9:
                        scoreHelper.setTitle(gold + "F" + yellow + "REE FOR ALL");
                        break;
                    case 10:
                        scoreHelper.setTitle(yellow + "F" + gold + "R" + yellow + "EE FOR ALL");
                        break;
                    case 11:
                        scoreHelper.setTitle(yellow + "FR" + gold + "E" + yellow + "E FOR ALL");
                        break;
                    case 12:
                        scoreHelper.setTitle(yellow + "FRE" + gold + "E" + yellow + " FOR ALL");
                        break;
                    case 13:
                        scoreHelper.setTitle(yellow + "FREE " + gold + "F" + yellow + "OR ALL");
                        break;
                    case 14:
                        scoreHelper.setTitle(yellow + "FREE F" + gold + "O" + yellow + "R ALL");
                        break;
                    case 15:
                        scoreHelper.setTitle(yellow + "FREE FO" + gold + "R" + yellow + " ALL");
                        break;
                    case 16:
                        scoreHelper.setTitle(yellow + "FREE FOR " + gold + "A" + yellow + "LL");
                        break;
                    case 17:
                        scoreHelper.setTitle(yellow + "FREE FOR A" + gold + "L" + yellow + "L");
                        break;
                    case 18:
                        scoreHelper.setTitle(yellow + "FREE FOR AL" + gold + "L");
                        break;
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                            scoreHelper.setTitle("&d&lFREE FOR ALL");
                        break;
                }
                count++;
            }
        }, 0, 3);
    }

    public static void createScoreboard(Player player) {
        ScoreHelper helper = ScoreHelper.createScore(player);
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        User user = new User(player.getUniqueId());

        helper.setTitle("&e&lFREE FOR ALL");
        helper.setSlot(15, "");
        helper.setSlot(14, "&bCoins: &f" + user.getCoins());
        helper.setSlot(13, "&bTotal EXP: &f" + user.getLifetimeExp().intValue());
        helper.setSlot(12, "");
        helper.setSlot(11, "&bKills: &f" + user.getKills());
        helper.setSlot(10, "&bDeaths: &f" + user.getDeaths());

        if (user.getKills() == 0 && user.getDeaths() == 0) {
            helper.setSlot(9, "&bK/D: &fN/A");

        } else if (user.getDeaths() == 0) {
            helper.setSlot(9, "&bK/D: &f" + user.getKills());

        } else {
            helper.setSlot(9, "&bK/D: &f" + user.getKD());
        }

        helper.setSlot(8, "");
        helper.setSlot(7, "&bKit Level: &f[N/A]");
        helper.setSlot(6, "&bKit EXP: &f[N/A]");
        helper.setSlot(5, "");
        helper.setSlot(4, "&bPrestige: " + user.getPrestige());
        helper.setSlot(3, "&bRank: " + hybridPlayer.getRankManager().getRank().getDisplayName());
        helper.setSlot(2, "");
        helper.setSlot(1, "&ehybridplays.com");
    }

    public static void updateScoreboard(Player player) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        User user = new User(player.getUniqueId());

        if (ScoreHelper.hasScore(player)) {
            ScoreHelper helper = ScoreHelper.getByPlayer(player);
            helper.setSlot(14, "&bCoins: &f" + user.getCoins());
            helper.setSlot(13, "&bTotal EXP: &f" + user.getLifetimeExp().intValue());
            helper.setSlot(11, "&bKills: &f" + user.getKills());
            helper.setSlot(10, "&bDeaths: &f" + user.getDeaths());

            if (user.getKills() == 0 && user.getDeaths() == 0) {
                helper.setSlot(9, "&bK/D: &fN/A");

            } else if (user.getDeaths() == 0) {
                helper.setSlot(9, "&bK/D: &f" + user.getKills());

            } else {
                helper.setSlot(9, "&bK/D: &f" + user.getKD());
            }

            if (gameMapManager.getCurrentKit().containsKey(player.getUniqueId())) {
                if (user.getKitLevel(gameMapManager.getCurrentKit().get(player.getUniqueId())) == 100) {
                    helper.setSlot(7, "&bKit Level: &f" + user.getKitLevel(gameMapManager.getCurrentKit().get(player.getUniqueId())) + " &8(Max LVL)");
                    helper.setSlot(6, "&bKit EXP: &fN/A &8(Max LVL)");
                } else {
                    helper.setSlot(7, "&bKit Level: &f" + user.getKitLevel(gameMapManager.getCurrentKit().get(player.getUniqueId())));
                    helper.setSlot(6, "&bKit EXP: &f" + user.getKitExp(gameMapManager.getCurrentKit().get(player.getUniqueId())).intValue() + "/" + gameMapManager.getExpMaxRequired(
                            player.getUniqueId(), gameMapManager.getCurrentKit().get(player.getUniqueId())
                    ));
                }
            } else {
                helper.setSlot(7, "&bKit Level: &f[N/A]");
                helper.setSlot(6, "&bKit EXP: &f[N/A]");
            }

            helper.setSlot(4, "&bPrestige: " + user.getPrestige());
            helper.setSlot(3, "&bRank: " + hybridPlayer.getRankManager().getRank().getDisplayName());
        }
    }
}












