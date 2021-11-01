package com.hybrid.ffa.listeners;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.utils.HotbarItems;
import net.hybrid.core.utility.HybridPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class GameMapListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!new HybridPlayer(player.getUniqueId()).getMetadataManager().isInBuildMode()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!new HybridPlayer(player.getUniqueId()).getMetadataManager().isInBuildMode()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(event.toWeatherState());
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!inBuild(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (!inBuild(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent event)  {
        if (event.getRegainReason() != EntityRegainHealthEvent.RegainReason.MAGIC &&
                event.getRegainReason() != EntityRegainHealthEvent.RegainReason.MAGIC_REGEN) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        EntityDamageEvent.DamageCause cause = event.getCause();

        if (cause == EntityDamageEvent.DamageCause.FALL
                || cause == EntityDamageEvent.DamageCause.SUFFOCATION
                || cause == EntityDamageEvent.DamageCause.DROWNING
                || cause == EntityDamageEvent.DamageCause.LIGHTNING
                || cause == EntityDamageEvent.DamageCause.CONTACT
                || cause == EntityDamageEvent.DamageCause.FIRE
                || cause == EntityDamageEvent.DamageCause.FIRE_TICK
                || cause == EntityDamageEvent.DamageCause.LAVA) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLobbyInventoryMove(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (FreeForAllPlugin.getInstance().getGameMapManager().getIsInArena().contains(player.getUniqueId())) return;
        if (inBuild(player)) return;

        String name = event.getCurrentItem().getItemMeta().getDisplayName();

        if (name.equals(HotbarItems.getKitSelector().getItemMeta().getDisplayName()) ||
                name.equals(HotbarItems.getProfile(player).getItemMeta().getDisplayName()) ||
                name.equals(HotbarItems.getCosmetics().getItemMeta().getDisplayName()) ||
                name.equals(HotbarItems.getLastKit(player).getItemMeta().getDisplayName()) ||
                name.equals(HotbarItems.changeLocation().getItemMeta().getDisplayName())) {
            event.setCancelled(true);
        }
    }

    private boolean inBuild(Player player) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        return hybridPlayer.getMetadataManager().isInBuildMode();
    }

    private boolean inBuild(Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
            return hybridPlayer.getMetadataManager().isInBuildMode();
        }
        return false;
    }

}
