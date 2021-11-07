package com.hybrid.ffa.menus;

import com.hybrid.ffa.FreeForAllPlugin;
import com.hybrid.ffa.data.User;
import com.hybrid.ffa.managers.GameMapManager;
import com.hybrid.ffa.utils.PlayerKit;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class KitMenu implements Listener {

    private static final String title = "Kits";

    public static void openKitMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54,"Kits");
        User user = new User(player.getUniqueId());
        GameMapManager manager = FreeForAllPlugin.getInstance().getGameMapManager();

        ItemStack swordsman = new ItemStack(Material.IRON_SWORD);
        ItemMeta swordsmanMeta = swordsman.getItemMeta();
        swordsmanMeta.setDisplayName(CC.translate("&aSwordsman &7(LVL " + user.getKitLevel(PlayerKit.SWORDSMAN) + ")"));
        ArrayList<String> swordsmanLore = new ArrayList<>();

        swordsmanLore.add(CC.translate("&8Player Kit"));
        swordsmanLore.add(" ");
        swordsmanLore.add(CC.translate("&7Your classic cold-blooded knight who"));
        swordsmanLore.add(CC.translate("&7only has their mind on coming out alive."));
        swordsmanLore.add(CC.translate("&7Use your sword to slaughter enemies"));
        swordsmanLore.add(CC.translate("&7with the help of some classic PVP items!"));
        swordsmanLore.add(" ");
        swordsmanLore.add(CC.translate("&fCurrently: &aUnlocked"));

        if (user.getKitLevel(PlayerKit.SWORDSMAN) == 100) {
            swordsmanLore.add(CC.translate("&fLevel: &6100 &8(Max LVL Reached)"));
        } else {
            swordsmanLore.add(CC.translate("&fLevel: &6" + user.getKitLevel(PlayerKit.SWORDSMAN)));
            swordsmanLore.add(CC.translate("&fEXP: &6" + user.getKitExp(PlayerKit.SWORDSMAN).intValue()) +
                    CC.translate("&7/&6") + manager.getExpMaxRequired(player.getUniqueId(), PlayerKit.SWORDSMAN));
        }

        swordsmanLore.add(" ");
        swordsmanLore.add(CC.translate("&eClick to use!"));

        swordsmanMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        swordsmanMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        swordsmanMeta.setLore(swordsmanLore);
        swordsman.setItemMeta(swordsmanMeta);


        ItemStack archer = new ItemStack(Material.BOW);
        ItemMeta archerMeta = swordsman.getItemMeta();
        archerMeta.setDisplayName(CC.translate("&aArcher &7(LVL " + user.getKitLevel(PlayerKit.ARCHER) + ")"));
        ArrayList<String> archerLore = new ArrayList<>();

        archerLore.add(CC.translate("&8Player Kit"));
        archerLore.add(" ");
        archerLore.add(CC.translate("&7One arrow, one kill- that's how"));
        archerLore.add(CC.translate("&7the archer does things. Use your"));
        archerLore.add(CC.translate("&7bow to silently kill off players"));
        archerLore.add(CC.translate("&7with the help of some classic PVP items!"));
        archerLore.add(" ");
        archerLore.add(CC.translate("&fCurrently: &aUnlocked"));

        if (user.getKitLevel(PlayerKit.ARCHER) == 100) {
            archerLore.add(CC.translate("&fLevel: &6100 &8(Max LVL Reached)"));
        } else {
            archerLore.add(CC.translate("&fLevel: &6" + user.getKitLevel(PlayerKit.ARCHER)));
            archerLore.add(CC.translate("&fEXP: &6" + user.getKitExp(PlayerKit.ARCHER).intValue()) +
                    CC.translate("&7/&6") + manager.getExpMaxRequired(player.getUniqueId(), PlayerKit.ARCHER));
        }

        archerLore.add(" ");
        archerLore.add(CC.translate("&eClick to use!"));

        archerMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        archerMeta.setLore(archerLore);
        archer.setItemMeta(archerMeta);


        ItemStack samurai = new ItemStack(Material.POTION);
        ItemMeta samuraiMeta = samurai.getItemMeta();
        samuraiMeta.setDisplayName(CC.translate("&aSamurai &7(LVL " + user.getKitLevel(PlayerKit.SAMURAI) + ")"));
        ArrayList<String> samuraiLore = new ArrayList<>();

        samuraiLore.add(CC.translate("&8Player Kit"));
        samuraiLore.add(" ");
        samuraiLore.add(CC.translate("&7Well trained ninja that has mastered"));
        samuraiLore.add(CC.translate("&7the art of PVP. Use your addon-affects"));
        samuraiLore.add(CC.translate("&7to ninja slice your enemies with the"));
        samuraiLore.add(CC.translate("&7help of classic PVP items!"));
        samuraiLore.add(" ");
        samuraiLore.add(CC.translate("&fCurrently: &aUnlocked"));

        if (user.getKitLevel(PlayerKit.SAMURAI) == 100) {
            samuraiLore.add(CC.translate("&fLevel: &6100 &8(Max LVL Reached)"));
        } else {
            samuraiLore.add(CC.translate("&fLevel: &6" + user.getKitLevel(PlayerKit.SAMURAI)));
            samuraiLore.add(CC.translate("&fEXP: &6" + user.getKitExp(PlayerKit.SAMURAI).intValue()) +
                    CC.translate("&7/&6") + manager.getExpMaxRequired(player.getUniqueId(), PlayerKit.SAMURAI));
        }

        samuraiLore.add(" ");
        samuraiLore.add(CC.translate("&eClick to use!"));

        samuraiMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);
        samuraiMeta.setLore(samuraiLore);
        samurai.setItemMeta(samuraiMeta);


        ItemStack tank = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta tankMeta = tank.getItemMeta();
        tankMeta.setDisplayName(CC.translate("&aTank &7(LVL " + user.getKitLevel(PlayerKit.TANK) + ")"));
        ArrayList<String> tankLore = new ArrayList<>();

        tankLore.add(CC.translate("&8Player Kit"));
        tankLore.add(" ");
        tankLore.add(CC.translate("&7This bully isn't afraid to be seen"));
        tankLore.add(CC.translate("&7and will hunt you down. Use your heavy"));
        tankLore.add(CC.translate("&7armor to counter enemies and kill them off"));
        tankLore.add(CC.translate("&7with some classic PVP items!"));
        tankLore.add(" ");
        tankLore.add(CC.translate("&fCurrently: &aUnlocked"));

        if (user.getKitLevel(PlayerKit.TANK) == 100) {
            tankLore.add(CC.translate("&fLevel: &6100 &8(Max LVL Reached)"));
        } else {
            tankLore.add(CC.translate("&fLevel: &6" + user.getKitLevel(PlayerKit.TANK)));
            tankLore.add(CC.translate("&fEXP: &6" + user.getKitExp(PlayerKit.TANK).intValue()) +
                    CC.translate("&7/&6") + manager.getExpMaxRequired(player.getUniqueId(), PlayerKit.TANK));
        }

        tankLore.add(" ");
        tankLore.add(CC.translate("&eClick to use!"));

        tankMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        tankMeta.setLore(tankLore);
        tank.setItemMeta(tankMeta);


        ItemStack wizard = new ItemStack(Material.BLAZE_ROD);
        ItemMeta wizardMeta = wizard.getItemMeta();
        ArrayList<String> wizardLore = new ArrayList<>();

        wizardLore.add(CC.translate("&8Player Kit"));
        wizardLore.add(CC.translate(""));
        wizardLore.add(CC.translate("&7This well trained magician will cast their"));
        wizardLore.add(CC.translate("&7spells to make you suffer. Use your magic"));
        wizardLore.add(CC.translate("&7powers to drain life out of enemies with"));
        wizardLore.add(CC.translate("&7the help of some classic PVP items!"));
        wizardLore.add(CC.translate(""));

        if (user.hasUnlockedKit(PlayerKit.WIZARD)) {
            wizardMeta.setDisplayName(CC.translate("&aWizard &7(LVL " + user.getKitLevel(PlayerKit.WIZARD) + ")"));

            wizardLore.add(CC.translate("&fCurrently: &aUnlocked"));
            wizardLore.add(CC.translate("&fLevel: &6" + user.getKitLevel(PlayerKit.WIZARD)));
            wizardLore.add(CC.translate("&fEXP: &6" + user.getKitExp(PlayerKit.WIZARD).intValue()) +
                    CC.translate("&7/&6") + manager.getExpMaxRequired(player.getUniqueId(), PlayerKit.WIZARD));
            wizardLore.add(CC.translate(""));
            wizardLore.add(CC.translate("&eClick to use!"));
        } else {
            wizardMeta.setDisplayName(CC.translate("&cWizard"));

            wizardLore.add(CC.translate("&fCurrently: &cLocked"));
            wizardLore.add(CC.translate(""));
            wizardLore.add(CC.translate("&cYou have not unlocked this!"));
        }

        wizardMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        wizardMeta.setLore(wizardLore);
        wizard.setItemMeta(wizardMeta);



        ItemStack wellRounded = new ItemStack(Material.LEATHER_HELMET);
        ItemMeta wellRoundedMeta = wellRounded.getItemMeta();
        ArrayList<String> wellRoundedLore = new ArrayList<>();

        wellRoundedLore.add(CC.translate("&8Player Kit"));
        wellRoundedLore.add(CC.translate(""));
        wellRoundedLore.add(CC.translate("&7This fella covers all the basics you'll"));
        wellRoundedLore.add(CC.translate("&7need to be a champion on the field. Use"));
        wellRoundedLore.add(CC.translate("&7'a little big of everything' to track down"));
        wellRoundedLore.add(CC.translate("&7your opponents and end their PVP carrier!"));
        wellRoundedLore.add(CC.translate(""));

        if (user.hasUnlockedKit(PlayerKit.WELL_ROUNDED)) {
            wellRoundedMeta.setDisplayName(CC.translate("&aWell Rounded &7(LVL " + user.getKitLevel(PlayerKit.WELL_ROUNDED) + ")"));

            wellRoundedLore.add(CC.translate("&fCurrently: &aUnlocked"));
            wellRoundedLore.add(CC.translate("&fCurrently: &aUnlocked"));
            wellRoundedLore.add(CC.translate("&fLevel: &6" + user.getKitLevel(PlayerKit.WELL_ROUNDED)));
            wellRoundedLore.add(CC.translate("&fEXP: &6" + user.getKitExp(PlayerKit.WELL_ROUNDED).intValue()) +
                    CC.translate("&7/&6") + manager.getExpMaxRequired(player.getUniqueId(), PlayerKit.WELL_ROUNDED));
            wellRoundedLore.add(CC.translate(""));
            wellRoundedLore.add(CC.translate("&eClick to use!"));
        } else {
            wellRoundedMeta.setDisplayName(CC.translate("&cWell Rounded"));

            wellRoundedLore.add(CC.translate("&fCurrently: &cLocked"));
            wellRoundedLore.add(CC.translate(""));
            wellRoundedLore.add(CC.translate("&cYou have not unlocked this!"));
        }

        wellRoundedMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        wellRoundedMeta.setLore(wellRoundedLore);
        wellRounded.setItemMeta(wellRoundedMeta);


        ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 7)
                .setDisplayName(" ").build();

        ItemStack kitEditor = new ItemBuilder(Material.ANVIL)
                .setDisplayName("&cKit Editor")
                .setLore(
                        CC.translate("&8Kit Utility"),
                        CC.translate(""),
                        CC.translate("&7The kit editor allows you to"),
                        CC.translate("&7personalize your kits to enhance"),
                        CC.translate("&7the way you play and win!"),
                        CC.translate(""),
                        CC.translate("&cComing soon!")
                ).build();

        ItemStack settings = new ItemBuilder(Material.REDSTONE_TORCH_ON)
                .setDisplayName("&cSettings")
                .setLore(
                        CC.translate("&8Kit Utility"),
                        CC.translate(""),
                        CC.translate("&7Customize the way you play"),
                        CC.translate("&7and personally options to"),
                        CC.translate("&7get the best experience possible."),
                        CC.translate(""),
                        CC.translate("&cComing soon!")
                ).build();

        ItemStack close = new ItemBuilder(Material.BARRIER).setDisplayName("&cClose").build();

        inv.setItem(48, settings);
        inv.setItem(49, close);
        inv.setItem(50, kitEditor);

        inv.setItem(19, swordsman);
        inv.setItem(21, archer);
        inv.setItem(23, samurai);
        inv.setItem(25, tank);
        inv.setItem(30, wizard);
        inv.setItem(32, wellRounded);

        for (int i = 0; i<9; i++) {
            inv.setItem(i, glass);
        }

        inv.setItem(47, glass);
        inv.setItem(46, glass);
        inv.setItem(45, glass);
        inv.setItem(51, glass);
        inv.setItem(52, glass);
        inv.setItem(53, glass);

        inv.setItem(9, glass);
        inv.setItem(17, glass);
        inv.setItem(18, glass);
        inv.setItem(26, glass);
        inv.setItem(27, glass);
        inv.setItem(35, glass);
        inv.setItem(36, glass);
        inv.setItem(44, glass);

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getView().getTitle().equalsIgnoreCase(CC.translate(title))) {
            if (event.getCurrentItem() == null) return;
            if (!event.getCurrentItem().hasItemMeta()) return;

            Player player = (Player) event.getWhoClicked();
            String displayName = CC.decolor(event.getCurrentItem().getItemMeta().getDisplayName());
            event.setCancelled(true);

            if (displayName.contains("Swordsman")) {
                player.closeInventory();
                player.updateInventory();

                player.chat("/kit swordsman");
                return;
            }

            if (displayName.contains("Archer")) {
                player.closeInventory();
                player.updateInventory();

                player.chat("/kit archer");
                return;
            }

            if (displayName.contains("Samurai")) {
                player.closeInventory();
                player.updateInventory();

                player.chat("/kit samurai");
                return;
            }

            if (displayName.contains("Tank")) {
                player.closeInventory();
                player.updateInventory();

                player.chat("/kit tank");
                return;
            }

            if (displayName.contains("Wizard")) {
                player.closeInventory();
                player.updateInventory();

                player.chat("/kit wizard");
                return;
            }

            if (displayName.contains("Well Rounded")) {
                player.closeInventory();
                player.updateInventory();

                player.chat("/kit well_rounded");
                return;
            }

            if (displayName.equalsIgnoreCase("Settings")) {
                player.sendMessage(CC.translate("&cComing soon!"));
                player.playSound(player.getLocation(), Sound.NOTE_BASS, 8, 1);
                return;
            }

            if (displayName.equalsIgnoreCase("Kit Editor")) {
                player.sendMessage(CC.translate("&cComing soon!"));
                player.playSound(player.getLocation(), Sound.NOTE_BASS, 8, 1);
                return;
            }

            if (displayName.equalsIgnoreCase("Close")) {
                player.closeInventory();
                player.updateInventory();
                player.playSound(player.getLocation(), Sound.NOTE_BASS, 8, 1);
            }
        }
    }
}













