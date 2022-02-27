package com.osterph.inventory;

import com.osterph.cte.CTE;
import com.osterph.manager.DropManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ShopListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getView().getTopInventory().getName().contains("§cShop") && e.getAction() != InventoryAction.NOTHING) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            ItemStack item = e.getCurrentItem();
            String name = item.getItemMeta().getDisplayName();
            if(e.getView().getTopInventory().getName() == "§cShop") {
                if(item.getType().equals(Material.ARROW)) {
                    p.closeInventory();
                } else if(name.contains("Waffen")) {
                    Shop.openShop(p, Shop.SHOPTYPE.WEAPON);
                } else if(name.contains("Rüstung")) {
                    Shop.openShop(p, Shop.SHOPTYPE.ARMOR);
                } else if(name.contains("Tools")) {
                    Shop.openShop(p, Shop.SHOPTYPE.TOOLS);
                } else if(name.contains("Blöcke")) {
                    Shop.openShop(p, Shop.SHOPTYPE.BLOCKS);
                } else if(name.contains("Extras")) {
                    Shop.openShop(p, Shop.SHOPTYPE.SONSTIGES);
                }
            } else {
                if(name.contains("Zurück")) {
                    Shop.openShop(p, Shop.SHOPTYPE.CHOOSE);
                }
                System.out.println("1");
                if(item.getItemMeta().getLore() != null && item.getItemMeta().getLore().get(0).contains("§7Cost: ")) {
                    System.out.println("2");
                    ShopItem shop = Shop.getShopItemByItemStack(item);
                    if(shop != null) {
                        System.out.println("3");
                        if(checkItem(shop.getRessource(), shop.cost, p)) {
                            System.out.println("4");
                            if(p.getInventory().getContents().length == 36) {
                                boolean isFull = true;
                                for(ItemStack a : p.getInventory().getContents()) {
                                    if(a.isSimilar(shop.getInventoryItem())) {
                                        if(a.getAmount() < a.getMaxStackSize()) {
                                            isFull = false;
                                        }
                                    }
                                }
                                if(isFull) {
                                    p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
                                    p.sendMessage(CTE.prefix + "§cDein Inventar ist voll!");
                                } else {
                                    p.getInventory().addItem(shop.getInventoryItem());
                                    p.sendMessage(CTE.prefix + "§aDu hast §e" + shop.getName() + " §a für " + shop.getCost() + " " + shop.getRessource() + " §agekauft!");
                                }
                            } else {
                                p.getInventory().addItem(shop.getInventoryItem());
                            }
                        } else {
                            p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
                            p.sendMessage(CTE.prefix + "§cDieses Item kannst du nicht bezahlen!");
                        }
                    }
                }
            }
        }
    }

    private boolean checkItem(Shop.Ressourcen re, int amount, Player p) {
        ItemStack m = new ItemStack(Material.BARRIER);
        switch (re) {
            case Karotte:
                m = new DropManager(DropManager.DROP.CARROT).getItem(1);
                break;
            case Apfel:
                m = new DropManager(DropManager.DROP.APPLE).getItem(1);
                break;
            case Melone:
                m = new DropManager(DropManager.DROP.MELON).getItem(1);
                break;
        }

        return p.getInventory().containsAtLeast(m, amount);
    }

}
