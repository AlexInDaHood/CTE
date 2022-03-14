package com.osterph.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.cte.CTESystem.GAMESTATE;
import com.osterph.lagerhalle.LocationLIST;
import com.osterph.manager.ItemManager;
import com.osterph.manager.ScoreboardManager;

public class EggListener implements Listener {

    private CTESystem sys = CTE.INSTANCE.getSystem();

    @EventHandler
    private void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (sys.teams.get(p).equals(CTESystem.TEAM.SPEC) || sys.teams.get(p).equals(CTESystem.TEAM.DEFAULT)) {
            e.setCancelled(true);
            return;
        }
        if (!sys.gamestate.equals(GAMESTATE.RUNNING)) {
            e.setCancelled(true);
            return;
        }
        if (e.getClickedBlock() == null) return;
        if (!e.getClickedBlock().getType().equals(Material.DRAGON_EGG)) return;
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (!p.isSneaking() || p.getItemInHand() == null) e.setCancelled(true);
        }
        if (!e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;

        e.setCancelled(true);
        String stolenegg = "";
        if (e.getClickedBlock().getLocation().equals(CTE.INSTANCE.getLocations().blueEGG())) {
            if (sys.teams.get(p).equals(CTESystem.TEAM.BLUE)) {
                p.sendMessage(CTE.prefix + "Das ist das falsche Ei...");
                return;
            }
            sys.sendAllMessage(CTE.prefix+"§c" + p.getName() + " §ehat das §9blaue Ei§e gestohlen!");
            sys.sendAllSound(Sound.WITHER_DEATH, 1, 1);
            sys.BLUE_EGG = CTESystem.EGG_STATE.STOLEN;
            stolenegg = "blue";
        } else if (e.getClickedBlock().getLocation().equals(CTE.INSTANCE.getLocations().redEGG())) {
            if (sys.teams.get(p).equals(CTESystem.TEAM.RED)) {
                p.sendMessage(CTE.prefix + "Das ist das falsche Ei...");
                return;
            }
            sys.sendAllMessage(CTE.prefix+"§9" + p.getName() + " §ehat das §crote Ei§e gestohlen!");
            sys.sendAllSound(Sound.WITHER_DEATH, 1, 1);
            sys.RED_EGG = CTESystem.EGG_STATE.STOLEN;
            stolenegg = "red";
        } else {
            p.sendMessage(CTE.prefix + "Ich denke nicht dass die einfach so rumliegen sollten...");
            return;
        }
        ItemStack headEgg = null;
        if(stolenegg.equals("red")) {
            headEgg = ItemManager.customHead("§cRotes-Ei", "", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTZiYTk5ODdmNzM4ZTZkNzVkM2IwMmMzMGQxNDgwYTM2MDU5M2RkYjQ2NGJkMWM4MWFiYjlkNzFkOWU2NTZjMCJ9fX0=");
        } else {
            headEgg = ItemManager.customHead("§9Blaues-Ei", "", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTZiYTk5ODdmNzM4ZTZkNzVkM2IwMmMzMGQxNDgwYTM2MDU5M2RkYjQ2NGJkMWM4MWFiYjlkNzFkOWU2NTZjMCJ9fX0=");
        }
        p.getInventory().setHelmet(headEgg);

        e.getClickedBlock().setType(Material.AIR);
        for (Player all: Bukkit.getOnlinePlayers()) {
            ScoreboardManager.refreshBoard(all);
        }
    }

    @EventHandler
    private void onDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        if (blockedItems().contains(item.getType())) {
            e.setCancelled(true);
            return;
        }
        Player p = e.getPlayer();

        if (p.getInventory().contains(Material.DIAMOND_SWORD)||p.getInventory().contains(Material.IRON_SWORD)||p.getInventory().contains(Material.STONE_SWORD)||p.getInventory().contains(Material.WOOD_SWORD)) return;
        p.getInventory().addItem(new ItemManager(Material.WOOD_SWORD).withName("§7Holzschwert").unbreakable(true).complete());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getSlot() == -999) return;
        if (e.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) return;
        if (e.getCurrentItem()== null) return;

        if (notClickable().contains(e.getCurrentItem().getType())) e.setCancelled(true);
    }

    private List<Material> blockedItems() {
        List<Material> list = new ArrayList<>();

        list.add(Material.WOOD_SWORD);
        list.add(Material.WOOD_PICKAXE);
        list.add(Material.STONE_PICKAXE);
        list.add(Material.IRON_PICKAXE);
        list.add(Material.DIAMOND_PICKAXE);
        list.add(Material.WOOD_AXE);
        list.add(Material.STONE_AXE);
        list.add(Material.IRON_AXE);
        list.add(Material.DIAMOND_AXE);
        list.add(Material.SHEARS);
        list.add(Material.LEATHER_HELMET);
        list.add(Material.LEATHER_CHESTPLATE);
        list.add(Material.LEATHER_LEGGINGS);
        list.add(Material.LEATHER_BOOTS);
        list.add(Material.CHAINMAIL_CHESTPLATE);
        list.add(Material.IRON_CHESTPLATE);
        list.add(Material.DIAMOND_CHESTPLATE);
        list.add(Material.SKULL_ITEM);

        return list;
    }

    private List<Material> notClickable() {
        List<Material> list = new ArrayList<>();

        list.add(Material.LEATHER_HELMET);
        list.add(Material.LEATHER_CHESTPLATE);
        list.add(Material.LEATHER_LEGGINGS);
        list.add(Material.LEATHER_BOOTS);
        list.add(Material.CHAINMAIL_CHESTPLATE);
        list.add(Material.IRON_CHESTPLATE);
        list.add(Material.DIAMOND_CHESTPLATE);
        list.add(Material.SKULL_ITEM);

        return list;
    }
}