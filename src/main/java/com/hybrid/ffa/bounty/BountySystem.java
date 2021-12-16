package com.hybrid.ffa.bounty;

import com.connorlinfoot.titleapi.TitleAPI;
import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.data.CachedUser;
import com.hybrid.ffa.managers.GameMapManager;
import com.hybrid.ffa.utils.PlayerKit;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class BountySystem {

    private final HashMap<UUID, Bounty> bountyCache;
    public static HashMap<UUID, ArmorStand> stands;
    private static final int amount = 20;

    public BountySystem() {
        bountyCache = new HashMap<>();
        stands = new HashMap<>();
    }

    public HashMap<UUID, Bounty> getBountyCache() {
        return bountyCache;
    }

    public boolean hasBounty(Player player) {
        return bountyCache.containsKey(player.getUniqueId());
    }

    public Bounty getBounty(Player player) {
        return bountyCache.get(player.getUniqueId());
    }

    public void createNewBounty(World world, String name) {
        Player target = null;

        if (!name.equalsIgnoreCase("IGNORE")) {
            target = Bukkit.getPlayer(name);
        }

        for (Player online : world.getPlayers()) {
            TitleAPI.sendTitle(online, 10, 40, 20, "&6&lBOUNTY TIME", "&7Selecting bounty...");
        }

        Player finalTarget = target;
        new BukkitRunnable() {
            int count = 0;
            int times = 0;

            Player localTarget = finalTarget;

            @Override
            public void run() {
                if (count == world.getPlayers().size()) count = 0;

                if (this.times == amount) {
                    this.cancel();

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ArrayList<Player> list = new ArrayList<>(world.getPlayers());
                            GameMapManager manager = FreeForAllPlugin.getInstance().getGameMapManager();

                            for (Player target : world.getPlayers()) {
                                if (hasBounty(target) || !manager.getIsInArena().contains(target.getUniqueId()) || !manager.getCurrentKit().containsKey(target.getUniqueId())) {
                                    list.remove(target);
                                }
                            }

                            if (!(list.size() >= 1)) {
                                for (Player online : world.getPlayers()) {
                                    TitleAPI.clearTitle(online);
                                    TitleAPI.sendTitle(online, 25, 70, 30, "&c&lBOUNTY FAILED", "&7Too few players are currently playing");
                                }

                                this.cancel();
                                return;
                            }

                            int random = new Random().nextInt(list.size());

                            if (localTarget != null) {
                                localTarget = finalTarget;
                            } else {
                                localTarget = Bukkit.getPlayer(list.get(random).getUniqueId());
                            }

                            String s = new HybridPlayer(localTarget.getUniqueId()).getRankManager().getRank().getPrefixSpace() + localTarget.getName();

                            Bukkit.getLogger().info("New bounty created and placed on >> " + localTarget.getName().toUpperCase());
                            for (Player online : world.getPlayers()) {
                                TitleAPI.clearTitle(online);
                                TitleAPI.sendTitle(online, 25, 70, 30, s, "&b&lBOUNTY SELECTED!");

                                if (online.getUniqueId() != localTarget.getUniqueId()) {
                                    online.sendMessage(CC.translate("&7&m------------------------------------"));
                                    online.sendMessage(CC.translate("              &6&lBOUNTY!"));
                                    online.sendMessage("   ");
                                    online.sendMessage(CC.translate("&aA new bounty was selected and placed on " + s + "&a!"));
                                    online.sendMessage(CC.translate("&aHunt 'em down to earn &6Coins &aand &bEXP&a!"));
                                    online.sendMessage(CC.translate("&7&m------------------------------------"));

                                    online.playSound(localTarget.getLocation(), Sound.ENDERDRAGON_GROWL, 12, -1);
                                }
                            }

                            AtomicInteger count = new AtomicInteger();
                            Bukkit.getScheduler().runTaskTimer(FreeForAllPlugin.getInstance(), () -> {
                                if (count.get() == 6) return;

                                localTarget.playEffect(
                                        localTarget.getLocation(),
                                        Effect.LARGE_SMOKE,
                                        Effect.LARGE_SMOKE.getData()
                                );

                                localTarget.playEffect(
                                        localTarget.getLocation(),
                                        Effect.EXPLOSION_HUGE,
                                        Effect.EXPLOSION_HUGE.getData()
                                );

                                localTarget.playEffect(
                                        localTarget.getLocation(),
                                        Effect.MOBSPAWNER_FLAMES,
                                        Effect.MOBSPAWNER_FLAMES.getData()
                                );

                                count.getAndIncrement();

                                localTarget.playSound(localTarget.getLocation(), Sound.ENDERDRAGON_WINGS, 7, 1);
                                localTarget.playSound(localTarget.getLocation(), Sound.ENDERMAN_IDLE, 7, 1);
                            }, 0, 20);

                            localTarget.playSound(localTarget.getLocation(), Sound.LEVEL_UP, 13, 1);
                            localTarget.playSound(localTarget.getLocation(), Sound.ENDERDRAGON_GROWL, 12, -1);

                            localTarget.sendMessage(CC.translate("&7&m------------------------------------"));
                            localTarget.sendMessage(CC.translate("              &6&lBOUNTY!"));
                            localTarget.sendMessage("   ");
                            localTarget.sendMessage(CC.translate("&e&lYOU WERE SELECTED FOR THE BOUNTY!"));
                            localTarget.sendMessage(CC.translate("&aStay alive to earn &6Coins &aand &bEXP&a!"));
                            localTarget.sendMessage(CC.translate("&7&m------------------------------------"));

                            ArmorStand as = localTarget.getWorld().spawn(localTarget.getLocation().add(0, .1, 0), ArmorStand.class);
                            as.setBasePlate(false);
                            as.setArms(false);
                            as.setVisible(false);
                            as.setCanPickupItems(false);
                            as.setGravity(false);
                            as.setCustomNameVisible(true);
                            as.setCustomName(CC.translate("&3&l&kLLL&6&lBOUNTY TARGET&3&l&kLLL"));
                            stands.put(localTarget.getUniqueId(), as);

                            bountyCache.put(localTarget.getUniqueId(), new Bounty(localTarget.getUniqueId()));
                        }
                    }.runTaskLater(FreeForAllPlugin.getInstance(), 15);
                }

                for (Player online : world.getPlayers()) {
                    TitleAPI.sendTitle(online, 1, 40, 1,
                            new HybridPlayer(world.getPlayers().get(count).getUniqueId()).getRankManager().getRank().getPrefixSpace() + Bukkit.getPlayer(world.getPlayers().get(count).getUniqueId()).getName()
                            , "&7Selecting bounty...");

                    online.playSound(online.getLocation(), Sound.NOTE_BASS, 10, 2);
                    online.playSound(online.getLocation(), Sound.NOTE_BASS_GUITAR, 10, 2);
                }

                times++;
                count++;
            }
        }.runTaskTimer(FreeForAllPlugin.getInstance(), 40, 5);
    }

    public void bountyExpired(Player player) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        CachedUser user = FreeForAllPlugin.getInstance().getUserManager().getCachedUser(player.getUniqueId());

        for (Player target : player.getWorld().getPlayers()) {
            if (target.getUniqueId() != player.getUniqueId()) {
                target.sendMessage(CC.translate("&c&lBOUNTY EXPIRED! &eThe bounty on " + hybridPlayer.getRankManager().getRank().getPrefixSpace() + player.getName() + " &eexpired, no luck for you..."));
            }

            target.playSound(target.getLocation(), Sound.VILLAGER_NO, 10, 1);
            target.playSound(target.getLocation(), Sound.NOTE_PIANO, 10, -1);
        }

        player.sendMessage(CC.translate("&a&lBOUNTY COMPLETED! &eYou completed the bounty and earned &6+500 Coins &eand &b+1000EXP&e!"));
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 10, 1);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 1);

        user.setLifetimeExp(user.getLifetimeExp() + 1000);
        user.addCoins(500);
        user.addKitExp(FreeForAllPlugin.getInstance().getGameMapManager().getCurrentKit().get(player.getUniqueId()), 1000);

        GameMapManager manager = FreeForAllPlugin.getInstance().getGameMapManager();
        PlayerKit kit = manager.getCurrentKit().get(player.getUniqueId());

        for (int i = 0; i<7; i++) {
            if (user.getKitExp(kit) >= manager.getExpMaxRequired(player.getUniqueId(), kit)
                    && !(user.getKitLevel(kit) >= 100)) {
                user.resetExp(kit);
                user.setKitLevel(kit, (user.getKitLevel(kit) + 1));
            }
        }

        if (user.getKitLevel(kit) >= 100) {
            player.setExp(0.9999f);
            player.setLevel(100);

        } else {
            int expRequired = manager.getExpMaxRequired(player.getUniqueId(), kit);

            player.setExp(user.getKitExp(kit).floatValue() / (float) expRequired);
            player.setLevel(user.getKitLevel(kit));
        }

        bountyCache.remove(player.getUniqueId());
    }

}










