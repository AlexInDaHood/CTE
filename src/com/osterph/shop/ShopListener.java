package com.osterph.shop;

import java.time.format.ResolverStyle;

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
import com.osterph.shop.Shop.Ressourcen;


public class ShopListener implements Listener {

    private CTESystem sys = CTE.INSTANCE.getSystem();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
    	if(e.getAction() == InventoryAction.NOTHING) return;
        if(e.getView().getTopInventory().getName().contains("§cShop")) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            ItemStack item = e.getCurrentItem();
            String name = item.getItemMeta().getDisplayName();
            if(e.getView().getTopInventory().getName().equals("§cShop")) {
                if(item.getType().equals(Material.ARROW)) {
                    p.closeInventory();
                } else if(name.contains("Waffen")) {
                	CTE.INSTANCE.getShop().openShop(p, Shop.SHOPTYPE.WEAPON);
                } else if(name.contains("Brustpanzer")) {
                	CTE.INSTANCE.getShop().openShop(p, Shop.SHOPTYPE.ARMOR);
                } else if(name.contains("Werkzeuge")) {
                	CTE.INSTANCE.getShop().openShop(p, Shop.SHOPTYPE.TOOLS);
                } else if(name.contains("Blöcke")) {
                	CTE.INSTANCE.getShop().openShop(p, Shop.SHOPTYPE.BLOCKS);
                } else if(name.contains("Extras")) {
                	CTE.INSTANCE.getShop().openShop(p, Shop.SHOPTYPE.SONSTIGES);
                }
            } else if(e.getView().getTopInventory().getName().contains("§cShop")){
                if(name.contains("Zurück")) {
                	CTE.INSTANCE.getShop().openShop(p, Shop.SHOPTYPE.CHOOSE);
                }
                if(item.getItemMeta().getLore() != null && item.getItemMeta().getLore().get(2).contains("§7Kosten§8: ")) {
                    ShopItem shop = CTE.INSTANCE.getShop().getShopItemByItemStack(item);
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
                                	if(shop.getShopItem().getType().equals(Material.CHAINMAIL_CHESTPLATE) || shop.getShopItem().getType().equals(Material.IRON_CHESTPLATE) ||shop.getShopItem().getType().equals(Material.DIAMOND_CHESTPLATE)) {
                                		if(!checkArmor(p, shop.getShopItem().getType())) {
                                			p.sendMessage(CTE.prefix + "§cDu kannst keine schlechtere oder die selbe Rüstung kaufen!");
                                			return;
                                		}
                                	}
                                    ItemStack b = new DropManager(shop.getRessource()).getItem(1);
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
                    } else {
                    	System.out.println("NULL");
                    }
                }
            }
        }
    }
    

    private boolean checkArmor(Player p, Material item) {
    	if(p.getInventory().getChestplate() == null) return true;
    	if(p.getInventory().getChestplate().getType().equals(Material.LEATHER_CHESTPLATE)) return true;
    	if(p.getInventory().getChestplate().getType().equals(Material.CHAINMAIL_CHESTPLATE) && !item.equals(Material.CHAINMAIL_CHESTPLATE)) return true;
    	if(p.getInventory().getChestplate().getType().equals(Material.IRON_CHESTPLATE) && (!item.equals(Material.CHAINMAIL_CHESTPLATE) && !item.equals(Material.IRON_CHESTPLATE))) return true;
    	if(p.getInventory().getChestplate().getType().equals(Material.DIAMOND_CHESTPLATE)) return false;
    	return false;
    }
    
    private boolean checkItem(Shop.Ressourcen re, int amount, Player p) {
        ItemStack m = new ItemStack(Material.BARRIER);
        m = new DropManager(re).getItem(1);
        return p.getInventory().containsAtLeast(m, amount);
    }

}
