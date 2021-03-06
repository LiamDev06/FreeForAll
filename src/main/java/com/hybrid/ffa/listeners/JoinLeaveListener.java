package com.hybrid.ffa.listeners;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.bounty.BountySystem;
import com.hybrid.ffa.utils.HotbarItems;
import com.hybrid.ffa.utils.LocationUtil;
import com.hybrid.ffa.managers.GameMapManager;
import com.hybrid.ffa.managers.ScoreboardManager;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.ScoreHelper;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        final Player player = event.getPlayer();

        player.closeInventory();
        player.getInventory().clear();

        FreeForAllPlugin.getInstance().getUserManager().loadPlayerToCache(player.getUniqueId());

        player.setLevel(0);
        player.setExp(0);
        player.setHealthScale(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setGameMode(GameMode.ADVENTURE);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 12, 2);
        ScoreboardManager.createScoreboard(player);
        ScoreboardManager.start(player);

        for (PotionEffect effect : player.getActivePotionEffects()) {
             player.removePotionEffect(effect.getType());
        }

        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));

        player.getInventory().setItem(0, HotbarItems.getKitSelector());
        player.getInventory().setItem(1, HotbarItems.getProfile(player));
        player.getInventory().setItem(2, HotbarItems.getCosmetics());
        player.getInventory().setItem(8, HotbarItems.changeLocation());

        if (FreeForAllPlugin.getInstance().getGameMapManager().getLastKitUsed().containsKey(player.getUniqueId())) {
            player.getInventory().setItem(4, HotbarItems.getLastKit(player));
        }

        new BukkitRunnable(){
            @Override
            public void run(){
                player.sendMessage(CC.translate("&a&m----------------------------------"));
                player.sendMessage(CC.translate("&6&lWelcome to Hybrid FFA!"));
                player.sendMessage(CC.translate("&fGet out there and fight for your life, may the"));
                player.sendMessage(CC.translate("&fbest player of them all win! Go rookie!"));
                player.sendMessage(CC.translate("&a&m----------------------------------"));
            }
        }.runTaskLater(FreeForAllPlugin.getInstance(), 20);

        GameMapManager manager = FreeForAllPlugin.getInstance().getGameMapManager();
        manager.getSpawnLocation().remove(player.getUniqueId());
        manager.getKillStreak().put(player.getUniqueId(), 0);

        if (!manager.getRegionSpawnLock().equalsIgnoreCase("RESET")) {
            player.teleport(LocationUtil.readLocation(FreeForAllPlugin.getInstance().getConfig().getConfigurationSection("spawnLocations." +
                    manager.getRegionSpawnLock() + "Loc")));
            manager.getSpawnLocation().put(player.getUniqueId(),manager.getRegionSpawnLock());
            return;
        }

        List<Location> spawnLocations = new ArrayList<>();
        spawnLocations.add(LocationUtil.readLocation(FreeForAllPlugin.getInstance().getConfig().getConfigurationSection("spawnLocations.desertLoc")));
        spawnLocations.add(LocationUtil.readLocation(FreeForAllPlugin.getInstance().getConfig().getConfigurationSection("spawnLocations.jungleLoc")));
        spawnLocations.add(LocationUtil.readLocation(FreeForAllPlugin.getInstance().getConfig().getConfigurationSection("spawnLocations.plainsLoc")));
        spawnLocations.add(LocationUtil.readLocation(FreeForAllPlugin.getInstance().getConfig().getConfigurationSection("spawnLocations.winterLoc")));

        Random random = new Random();
        int pos = random.nextInt(spawnLocations.size());
        Location spawnLocation = spawnLocations.get(pos);

        player.teleport(spawnLocation);

        if (pos == 0) {
            player.sendMessage(CC.translate("&8&m---------------------------------------------------------"));
            player.sendMessage(CC.translate("&6&kjhg &7A random spawn location was chosen, you got &a" + "Desert" + "&7! &6&kjhg"));
            player.sendMessage(CC.translate("&8&m---------------------------------------------------------"));
            manager.getSpawnLocation().put(player.getUniqueId(), "desert");
        }

        if (pos == 1) {
            player.sendMessage(CC.translate("&8&m---------------------------------------------------------"));
            player.sendMessage(CC.translate("&6&kjhg &7A random spawn location was chosen, you got &a" + "Jungle" + "&7! &6&kjhg"));
            player.sendMessage(CC.translate("&8&m---------------------------------------------------------"));
            manager.getSpawnLocation().put(player.getUniqueId(), "jungle");
        }

        if (pos == 2) {
            player.sendMessage(CC.translate("&8&m---------------------------------------------------------"));
            player.sendMessage(CC.translate("&6&kjhg &7A random spawn location was chosen, you got &a" + "Plains" + "&7! &6&kjhg"));
            player.sendMessage(CC.translate("&8&m---------------------------------------------------------"));
            manager.getSpawnLocation().put(player.getUniqueId(), "plains");
        }

        if (pos == 3) {
            player.sendMessage(CC.translate("&8&m---------------------------------------------------------"));
            player.sendMessage(CC.translate("&6&kjhg &7A random spawn location was chosen, you got &a" + "Winter" + "&7! &6&kjhg"));
            player.sendMessage(CC.translate("&8&m---------------------------------------------------------"));
            manager.getSpawnLocation().put(player.getUniqueId(), "winter");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        final UUID uuid = event.getPlayer().getUniqueId();

        if (ScoreboardManager.hasId(event.getPlayer().getUniqueId())) {
            ScoreboardManager.stop(event.getPlayer().getUniqueId());
        }

        if (ScoreHelper.hasScore(event.getPlayer())) {
            ScoreHelper.removeScore(event.getPlayer());
        }

        FreeForAllPlugin.getInstance().getUserManager().offLoadPlayerFromCache(event.getPlayer().getUniqueId());

        GameMapManager manager = FreeForAllPlugin.getInstance().getGameMapManager();
        manager.getIsInArena().remove(uuid);
        manager.getLastKitUsed().remove(uuid);
        manager.getKillStreak().remove(uuid);
        manager.getCurrentKit().remove(uuid);
        manager.getSpawnLocation().remove(uuid);

        BountySystem bountySystem = FreeForAllPlugin.getInstance().getBountySystem();
        if (bountySystem.hasBounty(event.getPlayer())) {
            bountySystem.bountyExpired(event.getPlayer());
        }

        bountySystem.getBountyCache().remove(uuid);
        if (BountySystem.stands.containsKey(uuid)) {
            BountySystem.stands.get(uuid).remove();
            BountySystem.stands.remove(uuid);
        }

        if (ScoreboardManager.hasId(event.getPlayer().getUniqueId())) {
            ScoreboardManager.stop(event.getPlayer().getUniqueId());
        }

        if (ScoreHelper.hasScore(event.getPlayer())) {
            ScoreHelper.removeScore(event.getPlayer());
        }
    }
}
