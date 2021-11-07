package com.hybrid.ffa.listeners;

import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.hybrid.core.utility.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCListener implements Listener {

    @EventHandler
    public void onNPCRightClick(NPCRightClickEvent event) {
        NPC npc = event.getNPC();
        Player player = event.getClicker();

        // KITS Click
        if (npc.getName().equalsIgnoreCase(CC.translate("&e&lKits"))) {
            player.chat("/kit");
        }

        // SHOP AND PERKS Click
        if (npc.getName().equalsIgnoreCase(CC.translate("&b&lShop and Perks"))) {
            player.sendMessage(CC.translate("&cThis is still under development..."));
        }

        // PERSONAL PROFILE Click
        if (npc.getName().equalsIgnoreCase(CC.translate("&d&lPersonal Stats"))) {
            player.sendMessage(CC.translate("&cMaybe just look at the scoreboard, eh? This is (also) under development... o_o"));
        }
    }

    @EventHandler
    public void onNPCLeftClick(NPCLeftClickEvent event) {
        NPC npc = event.getNPC();
        Player player = event.getClicker();

        // KITS Click
        if (npc.getName().equalsIgnoreCase(CC.translate("&e&lKits"))) {
            player.chat("/kit");
        }

        // SHOP AND PERKS Click
        if (npc.getName().equalsIgnoreCase(CC.translate("&b&lShop and Perks"))) {
            player.sendMessage(CC.translate("&cThis is still under development..."));
        }

        // PERSONAL PROFILE Click
        if (npc.getName().equalsIgnoreCase(CC.translate("&d&lPersonal Stats"))) {
            player.sendMessage(CC.translate("&cMaybe just look at the scoreboard, eh? This is (also) under development... o_o"));
        }
    }

}








