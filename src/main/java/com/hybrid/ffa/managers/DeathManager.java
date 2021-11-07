package com.hybrid.ffa.managers;

import com.connorlinfoot.titleapi.TitleAPI;
import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.data.User;
import com.hybrid.ffa.utils.HotbarItems;
import com.hybrid.ffa.utils.LocationUtil;
import com.hybrid.ffa.utils.PlayerKit;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.actionbar.ActionbarAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathManager implements Listener {

    @EventHandler
    public void onPlayerFFAKilled(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getFinalDamage() >= ((Player) event.getEntity()).getHealth())) return;
        event.setCancelled(true);
        if (event.getEntity().getUniqueId() == event.getDamager().getUniqueId()) return;

        GameMapManager manager = FreeForAllPlugin.getInstance().getGameMapManager();
        FileConfiguration config = FreeForAllPlugin.getInstance().getConfig();

        final Player died = (Player) event.getEntity();
        HybridPlayer hybridDied = new HybridPlayer(died.getUniqueId());
        User userDied = new User(died.getUniqueId());

        userDied.setDeaths(userDied.getDeaths() + 1);

        died.playSound(died.getLocation(), Sound.ENDERMAN_TELEPORT, 10, -2);
        died.playSound(died.getLocation(), Sound.ENDERDRAGON_HIT, 10, -1);
        TitleAPI.sendTitle(died, 20, 20 * 2, 20, "&c&lYOU DIED!", "&bRespawning in &65 &bseconds...");
        reset(died);

        new BukkitRunnable() {
            int value = 4;

            @Override
            public void run() {
                if (value == 0) {
                    respawn(died);

                    this.cancel();
                    return;
                }

                if (value == 1) {
                    TitleAPI.clearTitle(died);
                    TitleAPI.sendTitle(died, 1, 20 * 2, 1, "&c&lYOU DIED!", "&bRespawning in &6" + value + " &bsecond...");
                } else {
                    TitleAPI.clearTitle(died);
                    TitleAPI.sendTitle(died, 1, 20 * 2, 1, "&c&lYOU DIED!", "&bRespawning in &6" + value + " &bseconds...");
                }

                died.playSound(died.getLocation(), Sound.CLICK, 10, 3);
                value--;
            }

        }.runTaskTimer(FreeForAllPlugin.getInstance(), 20, 20);


        if (event.getDamager() instanceof Player) {
            Player killer = (Player) event.getDamager();
            HybridPlayer hybridKiller = new HybridPlayer(killer.getUniqueId());
            User userKilled = new User(killer.getUniqueId());
            PlayerKit kit = manager.getCurrentKit().get(killer.getUniqueId());
            hybridKiller.getNetworkLevelingManager().setExp(
                    hybridKiller.getNetworkLevelingManager().getExp() + 10
            );

            killer.playSound(killer.getLocation(), Sound.NOTE_PLING, 10, 2);
            killer.playSound(killer.getLocation(), Sound.NOTE_PIANO, 10, 2);
            killer.playSound(killer.getLocation(), Sound.ANVIL_BREAK, 10, 2);

            userKilled.setKills(userKilled.getKills() + 1);
            manager.getKillStreak().replace(killer.getUniqueId(), manager.getKillStreak().get(killer.getUniqueId()) + 1);
            if (manager.getKillStreak().get(killer.getUniqueId()) > userKilled.getLongestKillStreak()) {
                hybridKiller.sendMessage("&7&m-------------------------------");
                hybridKiller.sendMessage("&a&lNEW LIFETIME KILLSTREAK RECORD!");
                hybridKiller.sendMessage("&aYou reached a new lifetime killstreak record of &6" + manager.getKillStreak().get(killer.getUniqueId()) + " &akills!");
                hybridKiller.sendMessage("&7&m-------------------------------");

                TitleAPI.sendTitle(killer, 15, 40, 15, "&6&lNEW RECORD", "&bNew killstreak record of " + manager.getKillStreak().get(killer.getUniqueId()));

                killer.playSound(killer.getLocation(), Sound.ENDERDRAGON_GROWL, 10, -1);
                userKilled.setLongestKillStreak(manager.getKillStreak().get(killer.getUniqueId()));
            }

            String addText = "";
            String hybridAdd = "&2[HYBRID] &7+&a10EXP &2for kill in FFA!";
            if (manager.getKillStreak().get(killer.getUniqueId()) < 5) {
                ActionbarAPI.sendActionBar(killer, "§6+" + config.getInt("gameOptions.coins.kill") + " coins for kill", 40);
                addText = " &7[&6+" + config.getInt("gameOptions.kitExp.kill") + " KIT EXP&7]";

                int addExp = config.getInt("gameOptions.kitExp.kill");

                userKilled.addKitExp(manager.getCurrentKit().get(killer.getUniqueId()), addExp);
                userKilled.addCoins(config.getInt("gameOptions.coins.kill"));
                userKilled.addLifetimeExp(addExp);

            } else if (manager.getKillStreak().get(killer.getUniqueId()) >= 5 && manager.getKillStreak().get(killer.getUniqueId()) < 10) {
                ActionbarAPI.sendActionBar(killer, "§6+" + (config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_5")) + " coins for kill &8(Killstreak Bonus)", 40);
                addText = " &7[&6+" + (config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_5")) + " KIT EXP&7]";

                int addExp = config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_5");

                userKilled.addKitExp(manager.getCurrentKit().get(killer.getUniqueId()), addExp);
                userKilled.addCoins(config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_5"));
                userKilled.addLifetimeExp(addExp);
            }

            if (manager.getKillStreak().get(killer.getUniqueId()) >= 10 && manager.getKillStreak().get(killer.getUniqueId()) < 20) {
                ActionbarAPI.sendActionBar(killer, "§6+" + (config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_10")) + " coins for kill &8(Killstreak Bonus)", 40);
                addText = " &7[&6+" + (config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_10")) + " KIT EXP&7]";

                int addExp = config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_10");

                userKilled.addKitExp(manager.getCurrentKit().get(killer.getUniqueId()), addExp);
                userKilled.addCoins(config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_10"));
                userKilled.addLifetimeExp(addExp);
            }

            if (manager.getKillStreak().get(killer.getUniqueId()) >= 20) {
                ActionbarAPI.sendActionBar(killer, "§6+" + (config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_20")) + " coins for kill &8(Killstreak Bonus)", 40);
                addText = " &7[&6+" + (config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_20")) + " KIT EXP&7]";

                int addExp = config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_20");

                userKilled.addKitExp(manager.getCurrentKit().get(killer.getUniqueId()), addExp);
                userKilled.addCoins(config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_20"));
                userKilled.addLifetimeExp(addExp);
            }

            hybridKiller.sendMessage("&eYou killed " + hybridDied.getRankManager().getRank().getPrefixSpace() + died.getName() +
                    addText);
            hybridKiller.sendMessage(hybridAdd);

            hybridDied.sendMessage("&eYou got killed by " + hybridKiller.getRankManager().getRank().getPrefixSpace() + killer.getName());

            for (Player target : Bukkit.getOnlinePlayers()) {
                if (target.getUniqueId() != killer.getUniqueId() && target.getUniqueId() != died.getUniqueId()) {
                    target.sendMessage(CC.translate(
                            hybridDied.getRankManager().getRank().getPrefixSpace() + died.getName() +
                                    " &ewas killed by " + hybridKiller.getRankManager().getRank().getPrefixSpace() + killer.getName()
                    ));
                }
            }

            for (int i = 0; i<7; i++) {
                if (userKilled.getKitExp(kit) >= manager.getExpMaxRequired(killer.getUniqueId(), kit)) {
                    userKilled.resetExp(kit);
                    userKilled.setKitLevel(kit, (userKilled.getKitLevel(kit) + 1));
                }
            }

            killer.setLevel(userKilled.getKitLevel(kit));
            if (userKilled.getKitLevel(kit) == 100) {
                killer.setExp(9.999f);
            } else {
               int expRequired = FreeForAllPlugin.getInstance().getGameMapManager().getExpMaxRequired(killer.getUniqueId(), kit);
                killer.setExp(new User(killer.getUniqueId()).getKitExp(kit).floatValue() / (float) expRequired);
            }
        }

        else if (event.getDamager() instanceof Arrow) {
            Player killer = (Player) ((Arrow) event.getDamager()).getShooter();
            HybridPlayer hybridKiller = new HybridPlayer(killer.getUniqueId());
            User userKilled = new User(killer.getUniqueId());
            PlayerKit kit = manager.getCurrentKit().get(killer.getUniqueId());
            int addExp;

            killer.playSound(killer.getLocation(), Sound.NOTE_PLING, 10, 2);
            killer.playSound(killer.getLocation(), Sound.NOTE_PIANO, 10, 2);
            killer.playSound(killer.getLocation(), Sound.ANVIL_BREAK, 10, 2);

            userKilled.setKills(userKilled.getKills() + 1);
            manager.getKillStreak().replace(killer.getUniqueId(), manager.getKillStreak().get(killer.getUniqueId()) + 1);
            if (manager.getKillStreak().get(killer.getUniqueId()) > userKilled.getLongestKillStreak()) {
                hybridKiller.sendMessage("&7&m-------------------------------");
                hybridKiller.sendMessage("&a&lNEW LIFETIME KILLSTREAK RECORD!");
                hybridKiller.sendMessage("&aYou reached a new lifetime killstreak record of &6" + manager.getKillStreak().get(killer.getUniqueId()) + " &akills!");
                hybridKiller.sendMessage("&7&m-------------------------------");

                TitleAPI.sendTitle(killer, 15, 40, 15, "&6&lNEW RECORD", "&bNew killstreak record of " + manager.getKillStreak().get(killer.getUniqueId()));

                killer.playSound(killer.getLocation(), Sound.ENDERDRAGON_GROWL, 10, -1);
                userKilled.setLongestKillStreak(manager.getKillStreak().get(killer.getUniqueId()));
            }

            String addText = "";
            String hybridAdd = "&2[HYBRID] &7+&a10EXP &2for kill in FFA!";
            if (manager.getKillStreak().get(killer.getUniqueId()) < 5) {
                ActionbarAPI.sendActionBar(killer, "§6+" + config.getInt("gameOptions.coins.kill") + " coins for kill", 60);
                addText = " &7[&6+" + config.getInt("gameOptions.kitExp.kill") + " KIT EXP&7]";

                addExp = config.getInt("gameOptions.kitExp.kill");

                userKilled.addKitExp(manager.getCurrentKit().get(killer.getUniqueId()), addExp);
                userKilled.addCoins(config.getInt("gameOptions.coins.kill"));
                userKilled.addLifetimeExp(addExp);

            } else if (manager.getKillStreak().get(killer.getUniqueId()) >= 5 && manager.getKillStreak().get(killer.getUniqueId()) < 10) {
                ActionbarAPI.sendActionBar(killer, "§6+" + (config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_5")) + " coins for kill §8(Killstreak Bonus)", 60);
                addText = " &7[&6+" + (config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_5")) + " KIT EXP&7]";

               addExp = config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_5");

                userKilled.addKitExp(manager.getCurrentKit().get(killer.getUniqueId()), addExp);
                userKilled.addCoins(config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_5"));
                userKilled.addLifetimeExp(addExp);
            }

            if (manager.getKillStreak().get(killer.getUniqueId()) >= 10 && manager.getKillStreak().get(killer.getUniqueId()) < 20) {
                ActionbarAPI.sendActionBar(killer, "§6+" + (config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_10")) + " coins for kill §8(Killstreak Bonus)", 60);
                addText = " &7[&6+" + (config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_10")) + " KIT EXP&7]";

                addExp = config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_10");

                userKilled.addKitExp(manager.getCurrentKit().get(killer.getUniqueId()), addExp);
                userKilled.addCoins(config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_10"));
                userKilled.addLifetimeExp(addExp);
            }

            if (manager.getKillStreak().get(killer.getUniqueId()) >= 20) {
                ActionbarAPI.sendActionBar(killer, "§6+" + (config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_20")) + " coins for kill §8(Killstreak Bonus)", 60);
                addText = " &7[&6+" + (config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_20")) + " KIT EXP&7]";

                addExp = config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_20");

                userKilled.addKitExp(manager.getCurrentKit().get(killer.getUniqueId()), addExp);
                userKilled.addCoins(config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_20"));
                userKilled.addLifetimeExp(addExp);
            }

            hybridKiller.sendMessage("&eYou shot " + hybridDied.getRankManager().getRank().getPrefixSpace() + died.getName() + " &eto death!" + addText);
            hybridKiller.sendMessage(hybridAdd);
            hybridDied.sendMessage(hybridKiller.getRankManager().getRank().getPrefixSpace() + killer.getName() + "&e shot you to death using a bow!");

            for (Player target : Bukkit.getOnlinePlayers()) {
                if (target.getUniqueId() != killer.getUniqueId() && target.getUniqueId() != died.getUniqueId()) {
                    target.sendMessage(CC.translate(
                            hybridDied.getRankManager().getRank().getPrefixSpace() + died.getName() +
                                    " &ewas shot by " + hybridKiller.getRankManager().getRank().getPrefixSpace() + killer.getName()
                    ));
                }
            }

            for (int i = 0; i<7; i++) {
                if (userKilled.getKitExp(kit) >= manager.getExpMaxRequired(killer.getUniqueId(), kit)) {
                    userKilled.resetExp(kit);
                    userKilled.setKitLevel(kit, (userKilled.getKitLevel(kit) + 1));
                }
            }

            killer.setLevel(userKilled.getKitLevel(kit));
            if (userKilled.getKitLevel(kit) == 100) {
                killer.setExp(9.999f);
            } else {
                int expRequired = FreeForAllPlugin.getInstance().getGameMapManager().getExpMaxRequired(killer.getUniqueId(), kit);
                killer.setExp(new User(killer.getUniqueId()).getKitExp(kit).floatValue() / (float) expRequired);
            }
        } else {
            hybridDied.sendMessage("&eYou died.");

            for (Player target : Bukkit.getOnlinePlayers()) {
                if (target.getUniqueId() != died.getUniqueId()) {
                    target.sendMessage(CC.translate(
                            hybridDied.getRankManager().getRank().getPrefixSpace() + died.getName() +
                                    " &edied."
                    ));
                }
            }
        }
    }

    public void reset(Player died) {
        died.setGameMode(GameMode.SPECTATOR);
        died.teleport(died.getLocation().add(0, 2, 0));
        for (PotionEffect effect : died.getActivePotionEffects()) {
            died.removePotionEffect(effect.getType());
        }
        died.setCustomNameVisible(false);

        FreeForAllPlugin.getInstance().getGameMapManager().getKillStreak().replace(died.getUniqueId(), 0);

        PlayerInventory inv = died.getInventory();
        inv.clear();
        inv.setHelmet(new ItemStack(Material.AIR));
        inv.setChestplate(new ItemStack(Material.AIR));
        inv.setLeggings(new ItemStack(Material.AIR));
        inv.setBoots(new ItemStack(Material.AIR));

        died.setHealth(20);
        died.setHealthScale(20);
        died.setFoodLevel(20);
        died.setLevel(0);
        died.setExp(0);
        died.setAllowFlight(true);
        died.setFlying(true);
    }

    public void respawn(Player died) {
        TitleAPI.clearTitle(died);
        TitleAPI.sendTitle(died, 1, 30, 1, "&a&lRESPAWNED!", "&aYou respawned again!");
        died.playSound(died.getLocation(), Sound.NOTE_PLING, 10, 1);
        died.sendMessage(CC.translate("&aYou respawned!"));
        died.setCustomNameVisible(true);
        died.setGameMode(GameMode.ADVENTURE);

        PlayerInventory inv = died.getInventory();
        inv.clear();
        inv.setHelmet(new ItemStack(Material.AIR));
        inv.setChestplate(new ItemStack(Material.AIR));
        inv.setLeggings(new ItemStack(Material.AIR));
        inv.setBoots(new ItemStack(Material.AIR));

        died.setHealth(20);
        died.setHealthScale(20);
        died.setFoodLevel(20);
        died.setLevel(0);
        died.setExp(0);
        died.setAllowFlight(false);
        died.setFlying(false);

        for (PotionEffect effect : died.getActivePotionEffects()) {
            died.removePotionEffect(effect.getType());
        }

        died.teleport(LocationUtil.readLocation(
                FreeForAllPlugin.getInstance().getConfig().getConfigurationSection(
                        "spawnLocations." + FreeForAllPlugin.getInstance().getGameMapManager().getSpawnLocation().get(died.getUniqueId()) + "Loc"
                )));

        FreeForAllPlugin.getInstance().getGameMapManager().getCurrentKit().remove(died.getUniqueId());

        died.getInventory().setItem(0, HotbarItems.getKitSelector());
        died.getInventory().setItem(1, HotbarItems.getProfile(died));
        died.getInventory().setItem(2, HotbarItems.getCosmetics());
        died.getInventory().setItem(8, HotbarItems.changeLocation());

        if (FreeForAllPlugin.getInstance().getGameMapManager().getLastKitUsed().containsKey(died.getUniqueId())) {
            died.getInventory().setItem(4, HotbarItems.getLastKit(died));
        }
    }
}












