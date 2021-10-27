package com.hybrid.ffa.managers;

import com.hybrid.ffa.FreeForAllPlugin;
import net.hybrid.core.utility.CC;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class KitManager {

    public static void loadKitFromConfig(Player player, String kitName, int level) {
        FileConfiguration config = FreeForAllPlugin.getInstance().getKitsConfig();
        final String path = kitName.toLowerCase().replace(" ", "_") + "." + level + ".items.";
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

            player.getInventory().setItem(config.getInt(path + s.toUpperCase() + ".slot"), item);
        }
    }
}
