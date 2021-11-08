package com.hybrid.ffa.managers;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.data.CachedUser;
import com.hybrid.ffa.utils.PlayerKit;
import net.hybrid.core.utility.CC;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class KitManager {

    public void loadKitFromConfig(Player player, String kitName, int level) {
        FileConfiguration config = FreeForAllPlugin.getInstance().getKitsConfig();

        final String path = kitName.toLowerCase().replace(" ", "_") + "." + getClosestLevel(level) + ".items.";
        ConfigurationSection itemSection = config.getConfigurationSection(path.substring(0, path.length() - 1));

        for (String s : itemSection.getKeys(false)) {
            ItemStack item = new ItemStack(Material.valueOf(s.toUpperCase()),
                    config.getInt(path + s.toUpperCase() + ".amount"));

            ItemMeta firstMeta = item.getItemMeta();
            firstMeta.spigot().setUnbreakable(true);
            firstMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(firstMeta);

            if (!config.getString(path + s.toUpperCase() + ".displayName").equalsIgnoreCase("")) {
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(config.getString(path + s.toUpperCase() + ".displayName").replace("&", "§"));
                item.setItemMeta(meta);
            }

            if (config.getConfigurationSection(path + s.toUpperCase() + ".enchantments") != null && !config.get(path + s.toUpperCase() + ".enchantments").equals("")) {
                for (String enchantment : config.getConfigurationSection(path + s.toUpperCase() + ".enchantments").getKeys(false)) {
                    item.addUnsafeEnchantment(Enchantment.getByName(enchantment.toUpperCase()),
                            config.getInt(path + s.toUpperCase() + ".enchantments." + enchantment));
                }
            }

            if (config.getStringList(path + s.toUpperCase() + ".itemFlags") != null && !config.get(path + s.toUpperCase() + ".itemFlags").equals("")) {
                ItemMeta meta = item.getItemMeta();

                for (String itemFlag : config.getStringList(path + s.toUpperCase() + ".itemFlags")) {
                    meta.addItemFlags(ItemFlag.valueOf(itemFlag));
                }

                item.setItemMeta(meta);
            }

            if (config.get(path + s.toUpperCase() + ".potionEffects") != null) {
                PotionMeta meta = (PotionMeta) item.getItemMeta();
                ConfigurationSection typeSection = config.getConfigurationSection(path + s.toUpperCase() + ".potionEffects");
                ArrayList<PotionEffect> effects = new ArrayList<>();

                for (String potion : typeSection.getKeys(false)) {
                    int duration = config.getInt(path + s.toUpperCase() + ".potionEffects." + potion + ".duration");
                    int amp = config.getInt(path + s.toUpperCase() + ".potionEffects." + potion + ".amplifier");
                    boolean ambient = config.getBoolean(path + s.toUpperCase() + ".potionEffects." + potion + ".ambient");
                    boolean particles = config.getBoolean(path + s.toUpperCase() + ".potionEffects." + potion + ".particles");

                    effects.add(new PotionEffect(PotionEffectType.getByName(potion.toUpperCase())
                            , duration, amp, ambient, particles));
                }

                meta.setMainEffect(effects.get(0).getType());

                for (PotionEffect effect : effects) {
                    meta.addCustomEffect(effect, true);
                }

                item.setItemMeta(meta);
            }

            if (config.getStringList(path + s.toUpperCase() + ".lore") != null && !config.get(path + s.toUpperCase() + ".lore").equals("")) {
                ItemMeta meta = item.getItemMeta();
                ArrayList<String> lore;

                if (meta.hasLore()) {
                    lore = new ArrayList<>(meta.getLore());
                } else {
                    lore = new ArrayList<>();
                }

                for (String line : config.getStringList(path + s.toUpperCase() + ".lore")) {
                    lore.add(line.replace("&", "§"));
                }

                meta.setLore(lore);
                item.setItemMeta(meta);
            }

            if (item.getType().name().endsWith("_HELMET")) {
                player.getInventory().setHelmet(item);

            } else if (item.getType().name().endsWith("_CHESTPLATE")) {
                player.getInventory().setChestplate(item);

            } else if (item.getType().name().endsWith("_LEGGINGS")) {
                player.getInventory().setLeggings(item);

            } else if (item.getType().name().endsWith("_BOOTS")) {
                player.getInventory().setBoots(item);

            } else {
                player.getInventory().setItem(config.getInt(path + s.toUpperCase() + ".slot"), item);
            }
        }
    }

    public void loadKitFancy(Player player, PlayerKit playerKit, int level) {
        player.closeInventory();
        player.getInventory().clear();

        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));

        player.setLevel(level);
        int expRequired = FreeForAllPlugin.getInstance().getGameMapManager().getExpMaxRequired(player.getUniqueId(), playerKit);

        CachedUser user = FreeForAllPlugin.getInstance().getUserManager().getCachedUser(player.getUniqueId());
        player.setExp(user.getKitExp(playerKit).floatValue() / (float) expRequired);

        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 12, 2);
        player.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 8, 2);
        player.playSound(player.getLocation(), Sound.CLICK, 8, 1);

        FileConfiguration config = FreeForAllPlugin.getInstance().getKitsConfig();
        int near = getClosestLevel(level);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        if (config.getConfigurationSection(playerKit.name().toLowerCase() + "." + near + ".potionBuffs") != null) {
            ConfigurationSection section = config.getConfigurationSection(playerKit.name().toLowerCase() + "." + near + ".potionBuffs");
            String path = playerKit.name().toLowerCase() + "." + near + ".potionBuffs.";

            for (String buff : section.getKeys(false)) {
                int amp = config.getInt(path + buff + ".amp");
                boolean perm = config.getBoolean(path + buff + ".perm");

                int duration = 3600;
                if (perm) {
                    duration = 999999;
                }

                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.getByName(buff),
                        duration, amp
                ));
            }
        }

        loadKitFromConfig(player, playerKit.name(), level);
        FreeForAllPlugin.getInstance().getGameMapManager().getCurrentKit().put(player.getUniqueId(), playerKit);
        FreeForAllPlugin.getInstance().getGameMapManager().getLastKitUsed().put(player.getUniqueId(), playerKit);

        player.sendMessage(CC.translate(
                "&a&lKIT LOADED! &aYou loaded the kit &6" + playerKit.getDisplayName() + "&a. Now go out and fight!"
        ));
    }

    public static int getClosestLevel(int fromLevel) {
        if (fromLevel < 10) {
            return 1;
        }

        if (fromLevel < 25) {
            return 10;
        }

        if (fromLevel < 40) {
            return 25;
        }

        if (fromLevel < 75) {
            return 40;
        }

        if (fromLevel < 100) {
            return 75;
        }

        return 100;
    }
}












