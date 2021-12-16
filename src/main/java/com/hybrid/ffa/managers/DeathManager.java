package com.hybrid.ffa.managers;

import com.connorlinfoot.titleapi.TitleAPI;
import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.bounty.BountySystem;
import com.hybrid.ffa.utils.HotbarItems;
import com.hybrid.ffa.utils.LocationUtil;
import com.hybrid.ffa.utils.PlayerKit;
import com.hybrid.ffa.data.CachedUser;
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
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;

public class DeathManager implements Listener {

    @EventHandler
    public void onPlayerFFAKilled(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getFinalDamage() >= ((Player) event.getEntity()).getHealth())) return;
        event.setCancelled(true);

        GameMapManager manager = FreeForAllPlugin.getInstance().getGameMapManager();
        FileConfiguration config = FreeForAllPlugin.getInstance().getConfig();
        BountySystem bountySystem = FreeForAllPlugin.getInstance().getBountySystem();

        final Player died = (Player) event.getEntity();
        HybridPlayer hybridDied = new HybridPlayer(died.getUniqueId());
        CachedUser userDied = FreeForAllPlugin.getInstance().getUserManager().getCachedUser(died.getUniqueId());

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

        Player killer;

        if (event.getDamager() instanceof Player) {
            killer = (Player) event.getDamager();
        }

        else if (event.getDamager() instanceof Arrow) {
            killer = (Player) ((Arrow) event.getDamager()).getShooter();
        }

        else if (event.getDamager() instanceof Snowball) {
            killer = (Player) ((Snowball) event.getDamager()).getShooter();
        }

        else {
            hybridDied.sendMessage("&eYou died.");

            for (Player target : Bukkit.getOnlinePlayers()) {
                if (target.getUniqueId() != died.getUniqueId()) {
                    target.sendMessage(CC.translate(
                            hybridDied.getRankManager().getRank().getPrefixSpace() + died.getName() +
                                    " &edied."
                    ));
                }
            }
            return;
        }

        HybridPlayer hybridKiller = new HybridPlayer(killer.getUniqueId());
        CachedUser userKiller = FreeForAllPlugin.getInstance().getUserManager().getCachedUser(killer.getUniqueId());
        PlayerKit kit = manager.getCurrentKit().get(killer.getUniqueId());
        String hybridAdd = "&2[HYBRID] &7+&a10EXP &2for kill in FFA!";
        boolean shouldSendAll = true;

        if (bountySystem.hasBounty(died)) {
            for (Player target : died.getWorld().getPlayers()) {
                if (target.getUniqueId() != died.getUniqueId() && target.getUniqueId() != killer.getUniqueId()) {
                    target.sendMessage(CC.translate("&6&lBOUNTY DEAD! " + hybridKiller.getRankManager().getRank().getPrefixSpace() + killer.getName() + " &edestroyed " + hybridDied.getRankManager().getRank().getPrefixSpace() + died.getName() + " &eand won the bounty!"));
                }
            }

            shouldSendAll = false;
            int coinsAdd = 500;
            int expAdd = 1000;

            userKiller.addKitExp(manager.getCurrentKit().get(userKiller.getUuid()), expAdd);
            userKiller.setLifetimeExp(userKiller.getLifetimeExp() + expAdd);
            userKiller.addCoins(coinsAdd);
            killer.playSound(killer.getLocation(), Sound.ENDERDRAGON_GROWL, 13, 1);

            died.sendMessage(CC.translate("&c&lBOUNTY LOST! &cYou were killed while on bounty, better luck next time..."));
            killer.sendMessage(CC.translate("&6&lBOUNTY KILLED! &eYou killed the bounty. &7[&6+" + coinsAdd + " COINS &8- &b+" + expAdd + "EXP&7]"));
            bountySystem.getBountyCache().remove(died.getUniqueId());
        }

        if (manager.getKillStreak().containsKey(died.getUniqueId()) && manager.getKillStreak().get(died.getUniqueId()) > 0) {
            hybridDied.sendMessage("&8>> &7You lost a &6" + manager.getKillStreak().get(died.getUniqueId()) + " &7killstreak due to " + hybridKiller.getRankManager().getRank().getColor() + killer.getName() + "&7!");
            manager.getKillStreak().replace(died.getUniqueId(), 0);
        }

        if (userKiller.getKitLevel(kit) >= 100) {
            hybridKiller.getNetworkLevelingManager().setExp(
                    hybridKiller.getNetworkLevelingManager().getExp() + 50
            );

            hybridAdd = "&2[HYBRID] &7+&a50EXP &2for kill in FFA! &8(500% BONUS)";
        } else {
            hybridKiller.getNetworkLevelingManager().setExp(
                    hybridKiller.getNetworkLevelingManager().getExp() + 10
            );
        }

        int addExp;

        killer.playSound(killer.getLocation(), Sound.NOTE_PLING, 10, 2);
        killer.playSound(killer.getLocation(), Sound.NOTE_PIANO, 10, 2);
        killer.playSound(killer.getLocation(), Sound.ANVIL_BREAK, 10, 2);

        ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE, 1);
        ItemMeta goldenMeta = goldenApple.getItemMeta();
        goldenMeta.setDisplayName(CC.translate("&2Survivor Apple"));
        goldenMeta.setLore(Collections.singletonList(CC.translate("&7Earned for each kill!")));
        goldenApple.setItemMeta(goldenMeta);

        killer.getInventory().addItem(goldenApple);

        userKiller.setKills(userKiller.getKills() + 1);
        manager.getKillStreak().replace(killer.getUniqueId(), manager.getKillStreak().get(killer.getUniqueId()) + 1);
        if (manager.getKillStreak().get(killer.getUniqueId()) > userKiller.getLongestKillStreak()) {
            hybridKiller.sendMessage("&7&m-------------------------------");
            hybridKiller.sendMessage("&a&lNEW LIFETIME KILLSTREAK RECORD!");
            hybridKiller.sendMessage("&aYou reached a new lifetime killstreak record of &6" + manager.getKillStreak().get(killer.getUniqueId()) + " &akills!");
            hybridKiller.sendMessage("&7&m-------------------------------");

            TitleAPI.sendTitle(killer, 15, 40, 15, "&6&lNEW RECORD", "&bNew killstreak record of " + manager.getKillStreak().get(killer.getUniqueId()));

            killer.playSound(killer.getLocation(), Sound.ENDERDRAGON_GROWL, 10, -1);
            userKiller.setLongestKillStreak(manager.getKillStreak().get(killer.getUniqueId()));
        }

        String addText = "";
        if (manager.getKillStreak().get(killer.getUniqueId()) < 5) {
            ActionbarAPI.sendActionBar(killer, "§6+" + config.getInt("gameOptions.coins.kill") + " coins for kill", 40);
            addExp = config.getInt("gameOptions.kitExp.kill");
            userKiller.addCoins(config.getInt("gameOptions.coins.kill"));

            if (userKiller.getKitLevel(kit) >= 100) {
                addText = " &7[&e+" + config.getInt("gameOptions.kitExp.kill") + " LIFETIME EXP&7]";
            } else {
                addText = " &7[&6+" + config.getInt("gameOptions.kitExp.kill") + " KIT EXP&7]";

                userKiller.addKitExp(manager.getCurrentKit().get(killer.getUniqueId()), addExp);
            }

            userKiller.addLifetimeExp(addExp);

        } else if (manager.getKillStreak().get(killer.getUniqueId()) >= 5 && manager.getKillStreak().get(killer.getUniqueId()) < 10) {
            ActionbarAPI.sendActionBar(killer, "§6+" + (config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_5")) + " coins for kill §8(Killstreak Bonus)", 40);
            addExp = config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_5");
            userKiller.addCoins(config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_5"));

            if (userKiller.getKitLevel(kit) >= 100) {
                addText = " &7[&e+" + (config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_5")) + " LIFETIME EXP&7]";
            } else {
                addText = " &7[&6+" + (config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_5")) + " KIT EXP&7]";

                userKiller.addKitExp(manager.getCurrentKit().get(killer.getUniqueId()), addExp);
            }

            userKiller.addLifetimeExp(addExp);
        }

        if (manager.getKillStreak().get(killer.getUniqueId()) >= 10 && manager.getKillStreak().get(killer.getUniqueId()) < 20) {
            ActionbarAPI.sendActionBar(killer, "§6+" + (config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_10")) + " coins for kill §8(Killstreak Bonus)", 40);
            addExp = config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_10");
            userKiller.addCoins(config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_10"));

            if (userKiller.getKitLevel(kit) >= 100) {
                addText = " &7[&e+" + (config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_10")) + " LIFETIME EXP&7]";
            } else {
                addText = " &7[&6+" + (config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_10")) + " KIT EXP&7]";

                userKiller.addKitExp(manager.getCurrentKit().get(killer.getUniqueId()), addExp);
            }

            userKiller.addLifetimeExp(addExp);
        }

        if (manager.getKillStreak().get(killer.getUniqueId()) >= 20) {
            ActionbarAPI.sendActionBar(killer, "§6+" + (config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_20")) + " coins for kill §8(Killstreak Bonus)", 40);
            addExp = config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_20");
            userKiller.addCoins(config.getInt("gameOptions.coins.kill") + config.getInt("gameOptions.coins.winstreakOf_20"));

            if (userKiller.getKitLevel(kit) >= 100) {
                addText = " &7[&e+" + (config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_20")) + " LIFETIME EXP&7]";
            } else {
                addText = " &7[&6+" + (config.getInt("gameOptions.kitExp.kill") + config.getInt("gameOptions.kitExp.winstreakOf_20")) + " KIT EXP&7]";

                userKiller.addKitExp(manager.getCurrentKit().get(killer.getUniqueId()), addExp);
            }

            userKiller.addLifetimeExp(addExp);
        }

        if (event.getDamager() instanceof Player && shouldSendAll) {
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
        }

        if (event.getDamager() instanceof Arrow && shouldSendAll) {
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
        }

        if (event.getDamager() instanceof Snowball && shouldSendAll) {
            hybridKiller.sendMessage("&eYou snowballed " + hybridDied.getRankManager().getRank().getPrefixSpace() + died.getName() + " &eto death!" + addText);
            hybridKiller.sendMessage(hybridAdd);
            hybridDied.sendMessage(hybridKiller.getRankManager().getRank().getPrefixSpace() + killer.getName() + "&e snowballed you to death with a snowball, lol!");

            for (Player target : Bukkit.getOnlinePlayers()) {
                if (target.getUniqueId() != killer.getUniqueId() && target.getUniqueId() != died.getUniqueId()) {
                    target.sendMessage(CC.translate(
                            hybridDied.getRankManager().getRank().getPrefixSpace() + died.getName() +
                                    " &ewas snowballed by " + hybridKiller.getRankManager().getRank().getPrefixSpace() + killer.getName()
                    ));
                }
            }
        }

        if (!(userKiller.getKitLevel(kit) >= 100)) {
            for (int i = 0; i<7; i++) {
                if (userKiller.getKitExp(kit) >= manager.getExpMaxRequired(killer.getUniqueId(), kit)
                    && !(userKiller.getKitLevel(kit) >= 100)) {
                    userKiller.resetExp(kit);
                    userKiller.setKitLevel(kit, (userKiller.getKitLevel(kit) + 1));
                }
            }
        }

        if (userKiller.getKitLevel(kit) >= 100) {
            killer.setExp(0.9999f);
            killer.setLevel(100);

        } else {
            int expRequired = manager.getExpMaxRequired(killer.getUniqueId(), kit);

            killer.setExp(userKiller.getKitExp(kit).floatValue() / (float) expRequired);
            killer.setLevel(userKiller.getKitLevel(kit));
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getFinalDamage() >= ((Player) event.getEntity()).getHealth())) return;
        event.setCancelled(true);

        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.FIRE) {
            final Player died = (Player) event.getEntity();
            HybridPlayer hybridDied = new HybridPlayer(died.getUniqueId());
            CachedUser userDied = FreeForAllPlugin.getInstance().getUserManager().getCachedUser(died.getUniqueId());

            hybridDied.sendMessage("&eYou burned to death!");
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

            for (Player target : Bukkit.getOnlinePlayers()) {
                if (target.getUniqueId() != died.getUniqueId()) {
                    target.sendMessage(CC.translate(
                            hybridDied.getRankManager().getRank().getPrefixSpace() + died.getName() +
                                    " &eburned to death."
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

        GameMapManager manager = FreeForAllPlugin.getInstance().getGameMapManager();
        FileConfiguration config = FreeForAllPlugin.getInstance().getConfig();

        if (manager.getRegionSpawnLock().equalsIgnoreCase("RESET")) {
            died.teleport(LocationUtil.readLocation(
                    config.getConfigurationSection(
                            "spawnLocations." + manager.getSpawnLocation().get(died.getUniqueId()) + "Loc"
                    )));
        } else {
            String newLocation = manager.getRegionSpawnLock();

            died.teleport(LocationUtil.readLocation(
                    config.getConfigurationSection("spawnLocations." + newLocation + "Loc")));
            manager.getSpawnLocation().replace(died.getUniqueId(), newLocation);
        }

        died.setGameMode(GameMode.ADVENTURE);
        manager.getCurrentKit().remove(died.getUniqueId());

        died.getInventory().setItem(0, HotbarItems.getKitSelector());
        died.getInventory().setItem(1, HotbarItems.getProfile(died));
        died.getInventory().setItem(2, HotbarItems.getCosmetics());
        died.getInventory().setItem(8, HotbarItems.changeLocation());

        if (manager.getLastKitUsed().containsKey(died.getUniqueId())) {
            died.getInventory().setItem(4, HotbarItems.getLastKit(died));
        }
    }
}












