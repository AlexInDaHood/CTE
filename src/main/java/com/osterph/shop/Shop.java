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
        items.add(new ShopItem(Material.STONE_SWORD, "§fSteinschwert", "Verteile mit diesem Schwert mehr Schaden an Gegner.", 1, 10, Ressourcen.Apfel, null, 19, SHOPTYPE.WEAPON));
        items.add(new ShopItem(Material.IRON_SWORD, "§fEisenschwert", "Verteile mit diesem Schwert mehr Schaden an Gegner.", 1, 7, Ressourcen.Melone, null, 21, SHOPTYPE.WEAPON));
        HashMap<Enchantment, Integer> ench = new HashMap<>();
        ench.put(Enchantment.DAMAGE_ALL, 1);
        items.add(new ShopItem(Material.DIAMOND_SWORD, "§fDiamantschwert", "Verteile mit diesem Schwert mehr Schaden an Gegner.", 1, 4, Ressourcen.Karotte, null, 23, SHOPTYPE.WEAPON));
        ench = new HashMap<>();
        ench.put(Enchantment.KNOCKBACK, 1);
        items.add(new ShopItem(Material.STICK, "§fRückstoß Stock", "Verteile mit diesen Stock mehr Rückstoß an Gegner.", 1, 8, Ressourcen.Melone, ench, 25, SHOPTYPE.WEAPON));
        items.add(new ShopItem(Material.BOW, "§fBogen", "Schieße Gegner auf weiter Distanz ab.", 1, 12, Ressourcen.Melone, null, 29, SHOPTYPE.WEAPON));
        ench = new HashMap<>();
        ench.put(Enchantment.ARROW_DAMAGE, 1);
        items.add(new ShopItem(Material.BOW, "§fGuter Bogen", "Schieße Gegner auf weite Distanz ab.", 1, 24, Ressourcen.Melone, ench, 31, SHOPTYPE.WEAPON));
        items.add(new ShopItem(Material.ARROW, "§fPfeile", "Kaufe Pfeile um einen Bogen zu benutzen.", 8, 2, Ressourcen.Melone, null, 33, SHOPTYPE.WEAPON));

        //ARMOR
        items.add(new ShopItem(Material.CHAINMAIL_CHESTPLATE, "§fKettenbrustpanzer", "Bekomme weniger Schaden durch gegnerische Angriffe.\n§a§o+ Wird nach dem Tod beibehalten.", 1, 40, Ressourcen.Apfel, null, 20, SHOPTYPE.ARMOR));
        items.add(new ShopItem(Material.IRON_CHESTPLATE, "§fEisenbrustpanzer", "Bekomme weniger Schaden durch gegnerische Angriffe.\n§a§o+ Wird nach dem Tod beibehalten.", 1, 12, Ressourcen.Melone, null, 22, SHOPTYPE.ARMOR));
        items.add(new ShopItem(Material.DIAMOND_CHESTPLATE, "§fDiamantbrustpanzer", "Bekomme weniger Schaden durch gegnerische Angriffe.\n§a§o+ Wird nach dem Tod beibehalten.", 1, 6, Ressourcen.Karotte, null, 24, SHOPTYPE.ARMOR));

        //TOOLS
        items.add(new ShopItem(Material.STONE_PICKAXE, "§fSteinspitzhacke", "Baue Stein-artige Blöcke schneller ab.", 1, 10, Ressourcen.Apfel, null, 20, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.IRON_PICKAXE, "§fEisenspitzhacke", "Baue Stein-artige Blöcke schneller ab.", 1, 30, Ressourcen.Apfel, null, 29, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.DIAMOND_PICKAXE, "§fDiamantspitzhacke", "Baue Stein-artige Blöcke schneller ab.", 1, 8, Ressourcen.Melone, null, 38, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.SHEARS, "§fSchere", "Baue Wollblöcke schneller ab.", 1, 10, Ressourcen.Apfel, null, 31, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.STONE_AXE, "§fSteinaxt", "Baue Holzblöcke schneller ab.", 1, 10, Ressourcen.Apfel, null, 24, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.IRON_AXE, "§fEisenaxt", "Baue Holzblöcke schneller ab.", 1, 30, Ressourcen.Apfel, null, 33, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.DIAMOND_AXE, "§fDiamantaxt", "Baue Holzblöcke schneller ab.", 1, 8, Ressourcen.Melone, null, 42, SHOPTYPE.TOOLS));

        //Blocks
        items.add(new ShopItem(Material.WOOL, "§fWolle", "Empfohlen um dich zu anderen Inseln zu bauen.", 16, 4, Ressourcen.Apfel, null, 20, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.WOOD, "§fHolz", "Empfohlen um dein Ei zu verteidigen.", 16, 4, Ressourcen.Melone, null, 21, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.SANDSTONE, "§fSandstein", "Empfohlen um dein Ei zu verteidigen.", 16, 12, Ressourcen.Apfel, null, 23, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.ENDER_STONE, "§fEndstein", "Empfohlen um dein Ei zu verteidigen.", 8, 24, Ressourcen.Apfel, null, 24, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.OBSIDIAN, "§fObsidian", "Empfohlen um dein Ei zu verteidigen.", 2, 4, Ressourcen.Karotte, null, 30, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.WEB, "§fSpinnweben", "Empfohlen um dein Ei zu verteidigen.", 2, 12, Ressourcen.Apfel, null, 31, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.LADDER, "§fLeiter", "Empfohlen um Fallschaden zu vermeiden.", 16, 4, Ressourcen.Apfel, null, 32, SHOPTYPE.BLOCKS));

        //Extras
        items.add(new ShopItem(Material.GOLDEN_APPLE, "§6Goldapfel", "Gibt dir einen temporären Regenerations-Effekt.", 1, 3, Ressourcen.Melone, null, 20, SHOPTYPE.SONSTIGES));
        items.add(new ShopItem(Material.TNT, "§cTNT", "Empfohlen um die gegnerische Ei-Verteidigung zu zerstören.", 1, 6, Ressourcen.Melone, null, 22, SHOPTYPE.SONSTIGES));
        items.add(new ShopItem(Material.FIREBALL, "§cFeuerkugel", "Empfohlen um gegnerische Spieler oder Wege zu sprengen.\n§c§o- Schwächere Explosion als TNT.", 1, 40, Ressourcen.Apfel, null, 24, SHOPTYPE.SONSTIGES));
        items.add(new ShopItem(Material.ENDER_PEARL, "§5Enderperle", "Empfohlen zur schnellen Transportation von Ressourcen.\n§c§o- Kann nicht als Ei-Träger geworfen werden.", 1, 4, Ressourcen.Karotte, null, 29, SHOPTYPE.SONSTIGES));
        items.add(new ShopItem(Material.BLAZE_ROD, "§fRettungsplattform", "Erzeugt eine Glas-Plattform unter dir.\nSetzt deinen Fallschaden zurück.", 1, 3, Ressourcen.Karotte, null, 30, SHOPTYPE.SONSTIGES));
        items.add(new ShopItem(Material.BRICK, "§fBrücke", "Erzeugt einen Weg aus Glas unter dir.\n§c§o- Bricht ab, wenn der Weg auf einen Block trifft.\n§c§o- Bricht ab, wenn 30 Blöcke gesetzt wurden.", 1, 40, Ressourcen.Apfel, null, 32, SHOPTYPE.SONSTIGES));
        items.add(new ShopItem(Material.ENDER_PORTAL_FRAME, "§5Mysteriöses Portal", "Teleportiert dich in deine Basis zurück.\n§c§o- Kann nicht als Ei-Träger verwendet werden.", 1, 6, Ressourcen.Karotte, null, 33, SHOPTYPE.SONSTIGES));

    }

    public void openShop(Player p, SHOPTYPE type) {
        Inventory shop = null;
        switch(type) {
            case CHOOSE:
                shop = Bukkit.createInventory(null, 9*6, "§cShop");
                shop.setContents(getStandardGUI(false));
                shop.setItem(4, new ItemManager(Material.EMERALD).withName("§cShop").complete());
                shop.setItem(20, new ItemManager(Material.STONE_SWORD).withName("§7Waffen").complete());
                shop.setItem(22, new ItemManager(Material.IRON_CHESTPLATE).withName("§7Brustpanzer").complete());
                shop.setItem(24, new ItemManager(Material.DIAMOND_PICKAXE).withName("§7Werkzeuge").complete());
                shop.setItem(30, new ItemManager(Material.ENDER_STONE).withName("§7Blöcke").complete());
                shop.setItem(32, new ItemManager(Material.TNT).withName("§7Extras").complete());
                break;
            case WEAPON:
                shop = Bukkit.createInventory(null, 9*6, "§cShop §8» §eWaffen");
                shop.setContents(getStandardGUI(true));
                shop.setItem(4, new ItemManager(Material.STONE_SWORD).withName("§7Waffen").complete());
                for(ShopItem item : items) {
                    if(item.getType() == type) {
                        shop.setItem(item.getSlot(), item.getShopItem());
                    }
                }
                break;
            case ARMOR:
                shop = Bukkit.createInventory(null, 9*5, "§cShop §8» §eBrustpanzer");
                shop.setContents(getStandardGUI45(true));
                shop.setItem(4, new ItemManager(Material.IRON_CHESTPLATE).withName("§7Brustpanzer").complete());
                for(ShopItem item : items) {
                    if(item.getType() == type) {
                        shop.setItem(item.getSlot(), item.getShopItem());
                    }
                }
                break;
            case TOOLS:
                shop = Bukkit.createInventory(null, 9*6, "§cShop §8» §eWerkzeuge");
                shop.setContents(getStandardGUI(true));
                shop.setItem(4, new ItemManager(Material.DIAMOND_PICKAXE).withName("§7Werkzeuge").complete());
                for(ShopItem item : items) {
                    if(item.getType() == type) {
                        shop.setItem(item.getSlot(), item.getShopItem());
                    }
                }
                break;
            case BLOCKS:
                shop = Bukkit.createInventory(null, 9*6, "§cShop §8» §eBlöcke");
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
                shop = Bukkit.createInventory(null, 9*6, "§cShop §8» §eExtras");
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
