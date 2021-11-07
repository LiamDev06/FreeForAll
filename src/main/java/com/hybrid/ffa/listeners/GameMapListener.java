package com.hybrid.ffa.listeners;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.data.User;
import com.hybrid.ffa.managers.GameMapManager;
import com.hybrid.ffa.utils.HotbarItems;
import com.hybrid.ffa.utils.LocationUtil;
import net.hybrid.core.commands.admin.KaboomCommand;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class GameMapListener implements Listener {

    private final ArrayList<UUID> noEnterMessage = new ArrayList<>();
    private static ArrayList<Material> disableInteract;

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
    public void disableFarmLandTramp(PlayerInteractEvent event) {
        if (event.getPlayer() == null) return;
        if (inBuild(event.getPlayer())) return;

        if (event.getClickedBlock() != null && disableInteract.contains(event.getClickedBlock().getType())) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
        }

        if (event.getItem() != null && disableInteract.contains(event.getItem().getType())) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
        }
    }

    @EventHandler
    public void onLobbyEnterCheck(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        GameMapManager manager = FreeForAllPlugin.getInstance().getGameMapManager();
        Location midLocation = new Location(player.getWorld(), 89, 85, 127);

        if (player.getLocation().getBlockY() <= 45 && !inBuild(player)) {
            player.teleport(LocationUtil.readLocation(
                    FreeForAllPlugin.getInstance().getConfig()
                            .getConfigurationSection("spawnLocations."
                                    + manager.getSpawnLocation().get(player.getUniqueId()) + "Loc")
            ));

            SoundManager.playSound(player, "ENDERMAN_TELEPORT");
        }

        if (player.getLocation().getBlockY() >= 105 && !inBuild(player) && !KaboomCommand.kaboomNoFallDamage.contains(player.getUniqueId())) {
            player.teleport(LocationUtil.readLocation(
                    FreeForAllPlugin.getInstance().getConfig()
                            .getConfigurationSection("spawnLocations."
                                    + manager.getSpawnLocation().get(player.getUniqueId()) + "Loc")
            ));

            SoundManager.playSound(player, "ENDERMAN_TELEPORT");
        }

        if (player.getLocation().distance(midLocation) > 75) {
            manager.getIsInArena().remove(player.getUniqueId());
        }

        if (player.getLocation().distance(midLocation) < 76) {
            if (!manager.getIsInArena().contains(player.getUniqueId())) {
                manager.getIsInArena().add(player.getUniqueId());
            }
        }

        if (!inBuild(player) && !manager.getCurrentKit().containsKey(player.getUniqueId())) {
            if (player.getLocation().distance(midLocation) < 76) {
                player.setVelocity(player.getLocation().getDirection().multiply(-2).setY(1));

                if (!noEnterMessage.contains(player.getUniqueId())) {
                    noEnterMessage.remove(player.getUniqueId());
                    noEnterMessage.add(player.getUniqueId());

                    player.sendMessage(CC.translate("&c&lNO KIT SELECTED! &cYou need to select a kit before entering the arena!"));

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            noEnterMessage.remove(player.getUniqueId());
                        }

                    }.runTaskLater(FreeForAllPlugin.getInstance(), 40);
                }

                SoundManager.playErrorSound(player);
            }

            if (player.getLocation().distance(midLocation) < 74) {
                player.teleport(LocationUtil.readLocation(
                        FreeForAllPlugin.getInstance().getConfig()
                        .getConfigurationSection("spawnLocations."
                        + manager.getSpawnLocation().get(player.getUniqueId()) + "Loc")
                ));

                SoundManager.playSound(player, "ENDERMAN_TELEPORT");
            }
        }
    }

    @EventHandler
    public void onLobbyPVPCheck(EntityDamageByEntityEvent event) {
        GameMapManager gameMapManager = FreeForAllPlugin.getInstance().getGameMapManager();
        Location midLocation = new Location(event.getEntity().getWorld(), 89, 85, 127);

        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player hit = (Player) event.getEntity();
            Player damage = (Player) event.getDamager();

            if (!gameMapManager.getCurrentKit().containsKey(hit.getUniqueId()) && !gameMapManager.getCurrentKit().containsKey(damage.getUniqueId())) {
                event.setCancelled(true);
            }

            if (hit.getLocation().distance(midLocation) > 75) {
                event.setCancelled(true);
            }
        }

        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
            Player hit = (Player) event.getEntity();
            Player damage = (Player) ((Arrow) event.getDamager()).getShooter();

            if (!gameMapManager.getCurrentKit().containsKey(hit.getUniqueId()) && !gameMapManager.getCurrentKit().containsKey(damage.getUniqueId())) {
                event.setCancelled(true);
            }

            if (hit.getLocation().distance(midLocation) > 75) {
                event.setCancelled(true);
            }
        }

        if (event.getEntity() instanceof Player && event.getDamager() instanceof FishHook) {
            Player hit = (Player) event.getEntity();
            Player damage = (Player) ((FishHook) event.getDamager()).getShooter();

            if (!gameMapManager.getCurrentKit().containsKey(hit.getUniqueId()) && !gameMapManager.getCurrentKit().containsKey(damage.getUniqueId())) {
                event.setCancelled(true);
            }

            if (hit.getLocation().distance(midLocation) > 75) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLobbyInventoryMove(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (!event.getCurrentItem().hasItemMeta()) return;
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        if (FreeForAllPlugin.getInstance().getGameMapManager().getIsInArena().contains(player.getUniqueId())) return;
        if (inBuild(player)) return;

        String name = event.getCurrentItem().getItemMeta().getDisplayName();

        try {
            if (name.equals(HotbarItems.getKitSelector().getItemMeta().getDisplayName()) ||
                    name.equals(HotbarItems.getProfile(player).getItemMeta().getDisplayName()) ||
                    name.equals(HotbarItems.getCosmetics().getItemMeta().getDisplayName()) ||
                    name.equals(HotbarItems.getLastKit(player).getItemMeta().getDisplayName()) ||
                    name.equals(HotbarItems.changeLocation().getItemMeta().getDisplayName())) {
                event.setCancelled(true);
            }
        } catch (NullPointerException ignored) {}
    }

    @EventHandler
    public void disableItemFrameBreak(HangingBreakByEntityEvent event) {
        if (event.getEntity() instanceof ItemFrame) {
            if (event.getRemover() instanceof FishHook) event.setCancelled(true);
            if (!inBuild(event.getRemover())) event.setCancelled(true);
        }

        if (event.getEntity() instanceof ArmorStand) {
            if (!inBuild(event.getRemover())) event.setCancelled(true);
        }
    }

    @EventHandler
    public void armorStandInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        if (event.getRightClicked() instanceof ArmorStand) {
            if (!inBuild(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame
           && !((ItemFrame)event.getRightClicked()).getItem().getType().equals(Material.AIR)
            && !inBuild(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }

        if (event.getRightClicked() instanceof ItemFrame && !inBuild(event.getPlayer())) {
            event.setCancelled(true);
        }

        if (event.getRightClicked() instanceof ArmorStand && !inBuild(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void disableRemoveItemFrameItems(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ItemFrame) {
            if (event.getDamager() instanceof Player) {
                if (!inBuild(event.getDamager())) event.setCancelled(true);
            }

            if (event.getDamager() instanceof Projectile) {
                if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                    if (!inBuild((Player) ((Projectile) event.getDamager()).getShooter())) {
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (event.getEntity() instanceof ArmorStand) {
            if (event.getDamager() instanceof Player) {
                if (!inBuild(event.getDamager())) event.setCancelled(true);
            }

            if (event.getDamager() instanceof Projectile) {
                if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                    if (!inBuild((Player) ((Projectile) event.getDamager()).getShooter())) {
                        event.setCancelled(true);
                    }
                }
            }

            if (event.getDamager() instanceof FishHook) {
                if (((FishHook) event.getDamager()).getShooter() instanceof Player) {
                    if (!inBuild((Player) ((FishHook) event.getDamager()).getShooter())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHotBarItemClick(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;

        Player player = event.getPlayer();
        String displayName = event.getItem().getItemMeta().getDisplayName();

        try {
            if (displayName.equalsIgnoreCase(HotbarItems.getKitSelector().getItemMeta().getDisplayName())) {
                player.chat("/kit");
                return;
            }

            if (displayName.equalsIgnoreCase(HotbarItems.getProfile(player).getItemMeta().getDisplayName())) {
                player.sendMessage(CC.translate("&cThis is still under development!"));
                SoundManager.playSound(player, "NOTE_BASS");
                return;
            }

            if (displayName.equalsIgnoreCase(HotbarItems.getCosmetics().getItemMeta().getDisplayName())) {
                player.sendMessage(CC.translate("&cThis is still under development!"));
                SoundManager.playSound(player, "NOTE_BASS");
                return;
            }

            if (displayName.equalsIgnoreCase(HotbarItems.getLastKit(player).getItemMeta().getDisplayName())) {
                FreeForAllPlugin.getInstance().getKitManager().loadKitFancy(
                        player, FreeForAllPlugin.getInstance().getGameMapManager().getLastKitUsed().get(player.getUniqueId()),
                        new User(player.getUniqueId()).getKitLevel(FreeForAllPlugin.getInstance().getGameMapManager().getLastKitUsed().get(player.getUniqueId()))
                );
                return;
            }

            if (displayName.equalsIgnoreCase(HotbarItems.changeLocation().getItemMeta().getDisplayName())) {
                player.chat("/spawnlocation");
            }
        } catch (Exception ignored) {}
    }

    @EventHandler
    public void sendBowDamageMessage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            Player hit = (Player) event.getEntity();
            Player shooter = (Player) ((Arrow) event.getDamager()).getShooter();

            HybridPlayer hybridShooter = new HybridPlayer(shooter.getUniqueId());
            HybridPlayer hybridHit = new HybridPlayer(hit.getUniqueId());

            User userShooter = new User(shooter.getUniqueId());

            if (!(event.getFinalDamage() >= hit.getHealth())) {
                new BukkitRunnable(){
                    @Override
                    public void run(){
                        userShooter.setArrowsHit(userShooter.getArrowsHit() + 1);
                        shooter.playSound(shooter.getLocation(), Sound.NOTE_PIANO, 12, 2);

                        int amount = (int) hit.getHealth();
                        if (amount <= 0) {
                            amount = 1;
                        }

                        hybridShooter.sendMessage(CC.translate("&7➤ You hit " + hybridHit.getRankManager().getRank().getColor() +
                                hit.getName() + " &7down to &6" + amount + "❤"));
                    }
                }.runTaskLater(FreeForAllPlugin.getInstance(), 5);
            }
        }
    }

    @EventHandler
    public void onBowShot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player && event.getBow().getType() == Material.BOW) {
            Player player = (Player) event.getEntity();
            User user = new User(player.getUniqueId());

            user.setArrowsShot(user.getArrowsShot() + 1);
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

    public static void init() {
        disableInteract = new ArrayList<>();

        disableInteract.add(Material.SOIL);
        disableInteract.add(Material.WORKBENCH);
        disableInteract.add(Material.ANVIL);
        disableInteract.add(Material.NOTE_BLOCK);
        disableInteract.add(Material.HOPPER);
        disableInteract.add(Material.TRAP_DOOR);
        disableInteract.add(Material.IRON_TRAPDOOR);
        disableInteract.add(Material.WOOD_PLATE);
        disableInteract.add(Material.STONE_PLATE);
        disableInteract.add(Material.IRON_PLATE);
        disableInteract.add(Material.GOLD_PLATE);
        disableInteract.add(Material.DISPENSER);
        disableInteract.add(Material.DROPPER);
        disableInteract.add(Material.STONE_BUTTON);
        disableInteract.add(Material.WOOD_BUTTON);
        disableInteract.add(Material.MINECART);
        disableInteract.add(Material.FENCE_GATE);
        disableInteract.add(Material.SPRUCE_FENCE_GATE);
        disableInteract.add(Material.CHEST);
        disableInteract.add(Material.ITEM_FRAME);
        disableInteract.add(Material.ARMOR_STAND);
    }
}
