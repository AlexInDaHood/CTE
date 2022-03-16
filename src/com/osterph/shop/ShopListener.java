package com.osterph.shop;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.manager.DropManager;


public class ShopListener implements Listener {

    private CTESystem sys = CTE.INSTANCE.getSystem();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
    	if(e.getAction() == InventoryAction.NOTHING) return;
        if(e.getView().getTopInventory().getName().contains("§aShop")) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            ItemStack item = e.getCurrentItem();
            String name = item.getItemMeta().getDisplayName();
            if(e.getView().getTopInventory().getName().equals("§aShop")) {
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
                if(item.getItemMeta().getLore() != null && item.getItemMeta().getLore().get(0).contains("§7Kosten: ")) {
                    ShopItem shop = Shop.getShopItemByItemStack(item);
                    if(shop != null) {
                        if(checkItem(shop.getRessource(), shop.cost, p)) {
                            if(p.getInventory().getContents().length == 36) {
                                boolean isFull = true;
                                for(ItemStack a : p.getInventory().getContents()) {
                                    if(a == null) {
                                        isFull = false;
                                        break;
                                    }
                                    if(a.isSimilar(shop.getInventoryItem(a.getDurability()))) {
                                        if(a.getAmount()+shop.getAmount() < a.getMaxStackSize()) {
                                            isFull = false;
                                            break;
                                        }
                                    }
                                }
                                if(isFull) {
                                    p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
                                    p.sendMessage(CTE.prefix + "§cDein Inventar ist voll!");
                                } else {
                                    ItemStack b = null;
                                    switch(shop.getRessource()) {
                                        case Karotte:
                                            b = new DropManager(DropManager.DROP.CARROT).getItem(1);
                                            break;
                                        case Melone:
                                            b = new DropManager(DropManager.DROP.MELON).getItem(1);
                                            break;
                                        case Apfel:
                                            b = new DropManager(DropManager.DROP.APPLE).getItem(1);
                                            break;
                                    }
                                    p.sendMessage(CTE.prefix + "§eDu hast §6" + shop.getName() + " §efür " + shop.getCost() + " " + shop.ResourceToString() + " §egekauft!");
                                    ItemStack[] list = p.getInventory().getContents();
                                    int cost = shop.getCost();
                                    for (ItemStack i : list) {
                                        if (cost == 0) break;
                                        if (i == null) continue;
                                        if (i.getAmount() == 0) continue;
                                        if (i.getItemMeta() == null) continue;
                                        if (i.getItemMeta().getDisplayName() == null) continue;
                                        if (!i.getItemMeta().getDisplayName().equals(b.getItemMeta().getDisplayName())) continue;
                                        while (i.getAmount() > 0 && cost > 0) {
                                        	cost--;
                                        	if(i.getAmount() == 1) {
                                        		p.getInventory().remove(i);
                                        		break;
                                        	} else {
                                        		i.setAmount(i.getAmount()-1);
                                        	}
                                        }
                                    }
                                    switch (shop.getInventoryItem(0).getType()) {
                                        case CHAINMAIL_CHESTPLATE:
                                        case IRON_CHESTPLATE:
                                        case DIAMOND_CHESTPLATE:
                                            p.getInventory().setChestplate(shop.getInventoryItem(0));
                                            return;
                                        case WOOL:
                                            if (sys.teams.get(p).equals(CTESystem.TEAM.RED)) {
                                                p.getInventory().addItem(shop.getInventoryItem(14));
                                            } else if (sys.teams.get(p).equals(CTESystem.TEAM.BLUE)) {
                                                p.getInventory().addItem(shop.getInventoryItem(11));
                                            }
                                            return;
                                        case SANDSTONE:
                                            p.getInventory().addItem(shop.getInventoryItem(2));
                                            return;
                                        case STONE_SWORD:
                                        case IRON_SWORD:
                                        case DIAMOND_SWORD:
                                        	p.getInventory().remove(Material.WOOD_SWORD);
                                        	p.getInventory().addItem(shop.getInventoryItem(0));
                                        	return;
                                    }
                                    p.getInventory().addItem(shop.getInventoryItem(0));
                                }
                            } else {
                                switch (shop.getInventoryItem(0).getType()) {
                                    case CHAINMAIL_CHESTPLATE:
                                    case IRON_CHESTPLATE:
                                    case DIAMOND_CHESTPLATE:
                                        p.getInventory().setChestplate(shop.getInventoryItem(0));
                                        return;
                                    case WOOL:
                                        if (sys.teams.get(p).equals(CTESystem.TEAM.RED)) {
                                            p.getInventory().setChestplate(shop.getInventoryItem(14));
                                        } else if (sys.teams.get(p).equals(CTESystem.TEAM.BLUE)) {
                                            p.getInventory().setChestplate(shop.getInventoryItem(11));
                                        }
                                        return;
                                }
                                p.getInventory().addItem(shop.getInventoryItem(0));
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
