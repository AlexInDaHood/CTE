package com.osterph.lagerhalle;

import java.util.ArrayList;
import java.util.List;

import com.osterph.dev.devCMD;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.manager.ItemManager;
import com.osterph.manager.ScoreboardManager;
import com.osterph.shop.Shop;

public class TeamSelector implements Listener {

    public ItemStack team = new ItemManager(Material.BED).withName("§eTeam-Auswahl").complete();
    private CTESystem sys = CTE.INSTANCE.getSystem();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.PHYSICAL)) return;
        if (e.getPlayer().getItemInHand() == null) return;
        if (e.getPlayer().getItemInHand().getItemMeta() == null) return;
        if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;
        if(e.getPlayer().getItemInHand().isSimilar(team)) {
            openTeams(e.getPlayer());
        } else if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("§bDev-Settings")) {
            devCMD.openInventory(e.getPlayer());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getSlot() == -999) return;
        if (e.getCurrentItem() == null) return;
        if (e.getView().getTopInventory() == null) return;
        if (e.getClickedInventory() != e.getView().getTopInventory()) return;
        Inventory inv = e.getView().getTopInventory();
        ItemStack item = e.getCurrentItem();

        if (inv.getName() == null || !inv.getName().equals("§8» §eTeam-Auswahl")) return;
        e.setCancelled(true);
        if (item.getType().equals(Material.BARRIER)) {
            p.playSound(p.getLocation(), Sound.VILLAGER_NO, 0.6f, 1);
            p.sendMessage(CTE.prefix + "Dieses Team ist zu voll.");
            return;
        }
        if (!item.getType().equals(Material.WOOL)) return;

        if (item.getItemMeta().getDisplayName() == null) return;
        String itemName = item.getItemMeta().getDisplayName();

        if (itemName.equals("§cTEAM ROT")) {
            if (sys.red.contains(p)) {
                sys.teams.put(p, CTESystem.TEAM.DEFAULT);
                sys.red.remove(p);
                p.sendMessage(CTE.prefix + "Du hast Team §cROT§e verlassen.");
            } else {
                sys.teams.put(p, CTESystem.TEAM.RED);
                sys.red.add(p);
                sys.blue.remove(p);
                p.sendMessage(CTE.prefix + "Du hast Team §cROT§e betreten.");
            }
        } else if (itemName.equals("§9TEAM BLAU")) {
            if (sys.blue.contains(p)) {
                sys.teams.put(p, CTESystem.TEAM.DEFAULT);
                sys.blue.remove(p);
                p.sendMessage(CTE.prefix + "Du hast Team §9BLAU§e verlassen.");
            } else {
                sys.teams.put(p, CTESystem.TEAM.BLUE);
                sys.blue.add(p);
                sys.red.remove(p);
                p.sendMessage(CTE.prefix + "Du hast Team §9BLAU§e betreten.");
            }
        }
        ScoreboardManager.refreshBoard();
        p.closeInventory();

        for (Player all: Bukkit.getOnlinePlayers()) {
            if (all.getOpenInventory().getTopInventory() == null || all.getOpenInventory().getTopInventory().getName() == null || !all.getOpenInventory().getTopInventory().getName().equals("§8» §eTeam-Auswahl")) continue;
            openTeams(all);
        }

    }

    private void openTeams(Player p) {

        Inventory inv = Bukkit.createInventory(null, 45, "§8» §eTeam-Auswahl");
        inv.setContents(Shop.getStandardGUI45(false));
        ItemMeta meta = new ItemManager(Material.BARRIER).complete().getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        List<String> redlores = new ArrayList<>();
        List<String> bluelores = new ArrayList<>();

        for (Player red: sys.red) {
            String c = red.equals(p) ? "§e" : "§7";
            redlores.add("§8- "+c+red.getName());
        }
        for (Player blue: sys.blue) {
            String c = blue.equals(p) ? "§e" : "§7";
            bluelores.add("§8- "+c+blue.getName());
        }

        ItemStack red = new ItemManager(Material.BARRIER).withName("§cTEAM ROT").withLores(redlores).complete();
        ItemStack blue = new ItemManager(Material.BARRIER).withName("§9TEAM BLAU").withLores(bluelores).complete();

        if (sys.teams.get(p).equals(CTESystem.TEAM.RED)) {
            red = new ItemManager(Material.WOOL).withData(14).withLores(redlores).withMeta(meta).withName("§cTEAM ROT").complete();
        } else if (sys.red.size() == 0 || sys.red.size() <= sys.blue.size()) {
            red = new ItemManager(Material.WOOL).withData(14).withLores(redlores).withName("§cTEAM ROT").complete();
        }

        if (sys.teams.get(p).equals(CTESystem.TEAM.BLUE)) {
            blue = new ItemManager(Material.WOOL).withData(11).withLores(bluelores).withMeta(meta).withName("§9TEAM BLAU").complete();
        } else if (sys.blue.size() == 0 || sys.blue.size() <= sys.red.size()) {
            blue = new ItemManager(Material.WOOL).withData(11).withLores(bluelores).withName("§9TEAM BLAU").complete();
        }

        if (sys.teams.get(p).equals(CTESystem.TEAM.RED) && sys.red.size()-1 < sys.blue.size()) {
            red = new ItemManager(Material.WOOL).withData(14).withLores(redlores).withMeta(meta).withName("§cTEAM ROT").complete();
            blue = new ItemManager(Material.BARRIER).withName("§9TEAM BLAU").withLores(bluelores).complete();
        }
        if (sys.teams.get(p).equals(CTESystem.TEAM.BLUE) && sys.blue.size()-1 < sys.red.size()) {
            blue = new ItemManager(Material.WOOL).withData(11).withLores(bluelores).withMeta(meta).withName("§9TEAM BLAU").complete();
            red = new ItemManager(Material.BARRIER).withName("§cTEAM ROT").withLores(redlores).complete();
        }

        inv.setItem(21, blue);
        inv.setItem(23, red);

        p.openInventory(inv);
    }
}
