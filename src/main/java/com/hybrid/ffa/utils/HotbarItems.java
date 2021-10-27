package com.hybrid.ffa.utils;

import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class HotbarItems {

    public static ItemStack getKitSelector(){
        return new ItemBuilder(Material.PAPER)
                .setDisplayName("&b&lKit Selector")
                .setLore(
                        CC.translate("&7Select your starting kit."),
                        "",
                        CC.translate("&6Right click to view")
                )
                .build();
    }

    public static ItemStack getProfile(Player player){
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        meta.setDisplayName(CC.translate("&e&lProfile"));
        meta.setOwner(player.getName());

        ArrayList<String> lore = new ArrayList<String>();
        lore.add(CC.translate("&7View your FFA fighting profile."));
        lore.add("");
        lore.add(CC.translate("&6Right click to view"));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getCosmetics(){
        return new ItemBuilder(Material.BLAZE_POWDER)
                .setDisplayName("&2&lCosmetics")
                .setLore(
                        CC.translate("&7Unlock and view your FFA cosmetics."),
                        "",
                        CC.translate("&6Right click to view")
                )
                .build();
    }

    public static ItemStack getLastKit(Player player){
        return new ItemBuilder(Material.NETHER_STAR)
                .setDisplayName("&d&lUse Last Kit")
                .setLore(
                        CC.translate("&7Quick select your last used kit."),
                        "",
                        CC.translate("&6Right click to use")
                )
                .build();
    }

    public static ItemStack changeLocation(){
        return new ItemBuilder(Material.BOOK)
                .setDisplayName("&9&lChange Spawn")
                .setLore(
                        CC.translate("&7Change your FFA spawning area."),
                        "",
                        CC.translate("&6Right click to view")
                )
                .build();
    }

}
