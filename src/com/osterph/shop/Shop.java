package com.osterph.shop;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.osterph.manager.ItemManager;

public class Shop {

    public static ArrayList<ShopItem> items;

    
    public Shop() {
    	items = new ArrayList<>();
    	fillPrices();
    	
    }
    
    private void fillPrices() {
        //WEAPON
        items.add(new ShopItem(Material.STONE_SWORD, "§7Steinschwert", "Macht mehr Damage als ein Holz schwert", 1, 10, Ressourcen.Apfel, null, 19, SHOPTYPE.WEAPON));
        items.add(new ShopItem(Material.IRON_SWORD, "§7Eisenschwert", "Macht mehr Damage als ein Stein schwert", 1, 7, Ressourcen.Melone, null, 21, SHOPTYPE.WEAPON));
        HashMap<Enchantment, Integer> ench = new HashMap<>();
        ench.put(Enchantment.DAMAGE_ALL, 1);
        items.add(new ShopItem(Material.DIAMOND_SWORD, "§7Diamantschwert", "Macht mehr Damage als ein Eisen schwert", 1, 4, Ressourcen.Karotte, null, 23, SHOPTYPE.WEAPON));
        ench = new HashMap<>();
        ench.put(Enchantment.KNOCKBACK, 1);
        items.add(new ShopItem(Material.STICK, "§7Rückstoß Stock", "Macht Rückstoß", 1, 8, Ressourcen.Melone, ench, 25, SHOPTYPE.WEAPON));
        items.add(new ShopItem(Material.BOW, "§7Bogen", "Halt ein Bogen oder so", 1, 12, Ressourcen.Melone, null, 29, SHOPTYPE.WEAPON));
        ench = new HashMap<>();
        ench.put(Enchantment.ARROW_DAMAGE, 1);
        items.add(new ShopItem(Material.BOW, "§7Guter Bogen", "Macht mehr Damage als ein normaler Bogen", 1, 24, Ressourcen.Melone, ench, 31, SHOPTYPE.WEAPON));
        items.add(new ShopItem(Material.ARROW, "§7Pfeile", "Munition für deinen Bogen", 8, 2, Ressourcen.Melone, null, 33, SHOPTYPE.WEAPON));

        //ARMOR
        items.add(new ShopItem(Material.CHAINMAIL_CHESTPLATE, "§7Kettenbrustpanzer", "Damit hälst du mehr Schläge aus", 1, 40, Ressourcen.Apfel, null, 29, SHOPTYPE.ARMOR));
        items.add(new ShopItem(Material.IRON_CHESTPLATE, "§7Eisenbrustpanzer", "Damit hälst du mehr Schläge aus", 1, 12, Ressourcen.Melone, null, 31, SHOPTYPE.ARMOR));
        items.add(new ShopItem(Material.DIAMOND_CHESTPLATE, "§7Diamantbrustpanzer", "Damit hälst du mehr Schläge aus", 1, 6, Ressourcen.Karotte, null, 33, SHOPTYPE.ARMOR));

        //TOOLS
        items.add(new ShopItem(Material.STONE_PICKAXE, "§7Steinspitzhacke", "Damit kannst du schneller Steine abbauen", 1, 10, Ressourcen.Apfel, null, 20, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.IRON_PICKAXE, "§7Eisenspitzhacke", "Damit kannst du schneller Steine abbauen", 1, 30, Ressourcen.Apfel, null, 29, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.DIAMOND_PICKAXE, "§7Diamantspitzhacke", "Damit kannst du schneller Steine abbauen", 1, 8, Ressourcen.Melone, null, 38, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.SHEARS, "§7Schere", "Damit kannst du schneller Wolle abbauen", 1, 10, Ressourcen.Apfel, null, 31, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.STONE_AXE, "§7Steinaxt", "Damit kannst du schneller Holz abbauen", 1, 10, Ressourcen.Apfel, null, 24, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.IRON_AXE, "§7Eisenaxt", "Damit kannst du schneller Holz abbauen", 1, 30, Ressourcen.Apfel, null, 33, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.DIAMOND_AXE, "§7Diamantaxt", "Damit kannst du schneller Holz abbauen", 1, 8, Ressourcen.Melone, null, 42, SHOPTYPE.TOOLS));

        //Blocks
        items.add(new ShopItem(Material.WOOL, "§7Wolle", "Zum bauen", 16, 4, Ressourcen.Apfel, null, 20, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.WOOD, "§7Holz", "Zum bauen", 16, 4, Ressourcen.Melone, null, 21, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.SANDSTONE, "§7Sandstein", "Zum bauen", 16, 12, Ressourcen.Apfel, null, 23, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.ENDER_STONE, "§7Endstein", "Zum bauen", 8, 24, Ressourcen.Apfel, null, 24, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.OBSIDIAN, "§7Obsidian", "Zum bauen", 2, 4, Ressourcen.Karotte, null, 30, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.WEB, "§7Spinnweben", "Zum bauen", 2, 12, Ressourcen.Apfel, null, 31, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.LADDER, "§7Leiter", "Zum bauen", 16, 4, Ressourcen.Apfel, null, 32, SHOPTYPE.BLOCKS));

        //Extras
        items.add(new ShopItem(Material.GOLDEN_APPLE, "§7Goldapfel", "Heilt dich", 1, 3, Ressourcen.Melone, null, 20, SHOPTYPE.SONSTIGES));
        items.add(new ShopItem(Material.TNT, "§7TNT", "Halt ein Wassereimer?", 1, 6, Ressourcen.Melone, null, 22, SHOPTYPE.SONSTIGES));
        items.add(new ShopItem(Material.FIREBALL, "§7Feuerkugel", ":/", 1, 40, Ressourcen.Apfel, null, 24, SHOPTYPE.SONSTIGES));
        items.add(new ShopItem(Material.ENDER_PEARL, "§7Enderperle", "UwU", 1, 4, Ressourcen.Karotte, null, 30, SHOPTYPE.SONSTIGES));
        items.add(new ShopItem(Material.BLAZE_ROD, "§7Rettungsplattform", "Rettet dich beim runterfallen", 1, 6, Ressourcen.Karotte, null, 32, SHOPTYPE.SONSTIGES));

    }

    public void openShop(Player p, SHOPTYPE type) {
        Inventory shop = null;
        switch(type) {
            case CHOOSE:
                shop = Bukkit.createInventory(null, 9*6, "§cShop");
                shop.setContents(getStandardGUI(false));
                shop.setItem(4, new ItemManager(Material.EMERALD).withName("§cShop").withEnchantment(Enchantment.DURABILITY, 1, true).complete());
                shop.setItem(20, new ItemManager(Material.STONE_SWORD).withName("§7Waffen").complete());
                shop.setItem(22, new ItemManager(Material.IRON_CHESTPLATE).withName("§7Brustpanzer").complete());
                shop.setItem(24, new ItemManager(Material.DIAMOND_PICKAXE).withName("§7Werkzeuge").complete());
                shop.setItem(39, new ItemManager(Material.ENDER_STONE).withName("§7Blöcke").complete());
                shop.setItem(41, new ItemManager(Material.TNT).withName("§7Extras").complete());
                break;
            case WEAPON:
                shop = Bukkit.createInventory(null, 9*6, "§cShop §8» §2Waffen");
                shop.setContents(getStandardGUI(true));
                shop.setItem(4, new ItemManager(Material.STONE_SWORD).withName("§7Waffen").complete());
                for(ShopItem item : items) {
                    if(item.getType() == type) {
                        shop.setItem(item.getSlot(), item.getShopItem());
                    }
                }
                break;
            case ARMOR:
                shop = Bukkit.createInventory(null, 9*6, "§cShop §8» §2Brustpanzer");
                shop.setContents(getStandardGUI(true));
                shop.setItem(4, new ItemManager(Material.IRON_CHESTPLATE).withName("§7Brustpanzer").complete());
                for(ShopItem item : items) {
                    if(item.getType() == type) {
                        shop.setItem(item.getSlot(), item.getShopItem());
                    }
                }
                break;
            case TOOLS:
                shop = Bukkit.createInventory(null, 9*6, "§cShop §8» §2Werkzeuge");
                shop.setContents(getStandardGUI(true));
                shop.setItem(4, new ItemManager(Material.DIAMOND_PICKAXE).withName("§7Werkzeuge").complete());
                for(ShopItem item : items) {
                    if(item.getType() == type) {
                        shop.setItem(item.getSlot(), item.getShopItem());
                    }
                }
                break;
            case BLOCKS:
                shop = Bukkit.createInventory(null, 9*6, "§cShop §8» §2Blöcke");
                shop.setContents(getStandardGUI(true));
                shop.setItem(4, new ItemManager(Material.ENDER_STONE).withName("§7Blöcke").complete());
                for(ShopItem item : items) {
                    if(item.getType() == type) {
                        ItemStack i = item.getInventoryItem(0);
                        if(!i.getType().equals(Material.WOOL)) {
                            shop.setItem(item.getSlot(), item.getShopItem());
                        } else {
                            shop.setItem(item.getSlot(), item.getShopItem());
                        }
                    }
                }
                break;
            case SONSTIGES:
                shop = Bukkit.createInventory(null, 9*6, "§cShop §8» §2Extras");
                shop.setContents(getStandardGUI(true));
                shop.setItem(4, new ItemManager(Material.TNT).withName("§7Extras").complete());
                for(ShopItem item : items) {
                    if(item.getType() == type) {
                        shop.setItem(item.getSlot(), item.getShopItem());
                    }
                }
                break;
        }

        p.openInventory(shop);
    }



    public enum SHOPTYPE {
        CHOOSE, WEAPON, ARMOR, TOOLS, BLOCKS, SONSTIGES;
    }

    public ItemStack[] getStandardGUI(boolean back) {
        Inventory inv = Bukkit.createInventory(null, 9*6, "");
        ItemStack glass = new ItemManager(Material.STAINED_GLASS_PANE).withName("§7").withData(7).complete();
        ItemStack bglass = new ItemManager(Material.STAINED_GLASS_PANE).withName("§7").withData(15).complete();
        ItemStack backI = new ItemManager(Material.ARROW).withName("§cZurück").complete();
        ItemStack close = new ItemManager(Material.ARROW).withName("§cSchließen").complete();

        for(int i =0;i<inv.getSize();i++) {
            inv.setItem(i, glass);
        }
        inv.setItem(0, bglass);
        inv.setItem(1, bglass);
        inv.setItem(7, bglass);
        inv.setItem(8, bglass);
        inv.setItem(9, bglass);
        inv.setItem(17, bglass);
        inv.setItem(36, bglass);
        inv.setItem(44, bglass);
        if(back) {
            inv.setItem(49, backI);
        } else {
            inv.setItem(49, close);
        }
        inv.setItem(45, bglass);
        inv.setItem(46, bglass);
        inv.setItem(52, bglass);
        inv.setItem(53, bglass);
        return inv.getContents();
    }

    public ItemStack[] getStandardGUI45(boolean back) {
        Inventory inv = Bukkit.createInventory(null, 9*5, "");
        ItemStack glass = new ItemManager(Material.STAINED_GLASS_PANE).withName("§7").withData(7).complete();
        ItemStack bglass = new ItemManager(Material.STAINED_GLASS_PANE).withName("§7").withData(15).complete();
        ItemStack backI = new ItemManager(Material.ARROW).withName("§cZurück").complete();
        ItemStack close = new ItemManager(Material.ARROW).withName("§cSchließen").complete();

        for(int i =0;i<inv.getSize();i++) {
            inv.setItem(i, glass);
        }
        inv.setItem(0, bglass);
        inv.setItem(1, bglass);
        inv.setItem(7, bglass);
        inv.setItem(8, bglass);
        inv.setItem(9, bglass);
        inv.setItem(17, bglass);
        inv.setItem(27, bglass);
        inv.setItem(35, bglass);
        if(back) {
            inv.setItem(40, backI);
        } else {
            inv.setItem(40, close);
        }
        inv.setItem(36, bglass);
        inv.setItem(37, bglass);
        inv.setItem(43, bglass);
        inv.setItem(44, bglass);
        return inv.getContents();
    }

    public enum Ressourcen {
        Apfel, Melone, Karotte,
    }

    public ShopItem getShopItemByItemStack(ItemStack item) {
        ShopItem a = null;
        for(ShopItem i : items) {
            if(i.getShopItem().isSimilar(item)) {
                a = i;
            }
        }
        return a;
    }

    public ShopItem getShopItemByMaterial(Material mat) {
    	ShopItem a = null;
    	for(ShopItem i : items) {
    		if(i.getInventoryItem(1).getType().equals(mat)) {
    			return i;
    		}
    	}
    	
    	return a;
    }
    
}
