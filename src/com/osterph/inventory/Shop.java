package com.osterph.inventory;

import com.osterph.manager.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class Shop {

    public static ArrayList<ShopItem> items = new ArrayList<>();

    private static void fillPrices() {
        //WEAPON
        items.add(new ShopItem(Material.STONE_SWORD, "§7Stein Schwert", "Macht mehr Damage als ein Holz schwert", 1, 10, Ressourcen.Melone, null, 20, SHOPTYPE.WEAPON));
        items.add(new ShopItem(Material.IRON_SWORD, "§7Eisen Schwert", "Macht mehr Damage als ein Stein schwert", 1, 20, Ressourcen.Melone, null, 22, SHOPTYPE.WEAPON));
        items.add(new ShopItem(Material.DIAMOND_SWORD, "§7Diamant Schwert", "Macht mehr Damage als ein Eisen schwert", 1, 30, Ressourcen.Melone, null, 24, SHOPTYPE.WEAPON));
        items.add(new ShopItem(Material.BOW, "§7Bogen", "Halt ein Bogen oder so", 1, 5, Ressourcen.Melone, null, 29, SHOPTYPE.WEAPON));
        HashMap<Enchantment, Integer> ench = new HashMap<>();
        ench.put(Enchantment.ARROW_DAMAGE, 2);
        items.add(new ShopItem(Material.BOW, "§7Guter Bogen", "Macht mehr Damage als ein normaler Bogen", 1, 50, Ressourcen.Melone, ench, 31, SHOPTYPE.WEAPON));
        items.add(new ShopItem(Material.ARROW, "§7Pfeile", "Munition für dein Bogen", 8, 20, Ressourcen.Melone, null, 33, SHOPTYPE.WEAPON));

        //ARMOR
        items.add(new ShopItem(Material.CHAINMAIL_CHESTPLATE, "§cKetten Rüstung", "Damit hälst du mehr Schläge aus", 1, 10, Ressourcen.Melone, null, 29, SHOPTYPE.ARMOR));
        items.add(new ShopItem(Material.IRON_CHESTPLATE, "§cEisen Rüstung", "Damit hälst du mehr Schläge aus", 1, 10, Ressourcen.Melone, null, 31, SHOPTYPE.ARMOR));
        items.add(new ShopItem(Material.DIAMOND_CHESTPLATE, "§cDiamant Rüstung", "Damit hälst du mehr Schläge aus", 1, 10, Ressourcen.Melone, null, 33, SHOPTYPE.ARMOR));

        //TOOLS
        items.add(new ShopItem(Material.STONE_PICKAXE, "§7Stein Spitzhacke", "Damit kannst du schneller Steine abbauen", 1, 10, Ressourcen.Apfel, null, 20, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.IRON_PICKAXE, "§7Eisen Spitzhacke", "Damit kannst du schneller Steine abbauen", 1, 10, Ressourcen.Apfel, null, 29, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.DIAMOND_PICKAXE, "§7Diamant Spitzhacke", "Damit kannst du schneller Steine abbauen", 1, 10, Ressourcen.Apfel, null, 38, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.SHEARS, "§7Schere", "Damit kannst du schneller Wolle abbauen", 1, 10, Ressourcen.Apfel, null, 31, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.STONE_AXE, "§7Stein Axt", "Damit kannst du schneller Holz abbauen", 1, 10, Ressourcen.Apfel, null, 24, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.IRON_AXE, "§7Eisen Axt", "Damit kannst du schneller Holz abbauen", 1, 10, Ressourcen.Apfel, null, 33, SHOPTYPE.TOOLS));
        items.add(new ShopItem(Material.DIAMOND_AXE, "§7Diamant Axt", "Damit kannst du schneller Holz abbauen", 1, 10, Ressourcen.Apfel, null, 42, SHOPTYPE.TOOLS));

        //Blocks
        items.add(new ShopItem(Material.WOOL, "§7Wolle", "Zum bauen", 16, 10, Ressourcen.Melone, null, 20, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.WOOD, "§7Holz", "Zum bauen", 6, 10, Ressourcen.Melone, null, 21, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.SANDSTONE, "§7Sandstein", "Zum bauen", 8, 10, Ressourcen.Melone, null, 23, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.ENDER_STONE, "§7Endstone", "Zum bauen", 4, 10, Ressourcen.Melone, null, 24, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.OBSIDIAN, "§7Obsidian", "Zum bauen", 2, 10, Ressourcen.Melone, null, 30, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.WEB, "§7Cobweb", "Zum bauen", 1, 10, Ressourcen.Melone, null, 31, SHOPTYPE.BLOCKS));
        items.add(new ShopItem(Material.LADDER, "§7Leiter", "Zum bauen", 16, 10, Ressourcen.Melone, null, 32, SHOPTYPE.BLOCKS));

        //Extras
        items.add(new ShopItem(Material.GOLDEN_APPLE, "§7Goldapfel", "Heilt dich", 1, 20, Ressourcen.Karotte, null, 20, SHOPTYPE.SONSTIGES));
        items.add(new ShopItem(Material.WATER_BUCKET, "§7Wassereimer", "Halt ein Wassereimer?", 1, 20, Ressourcen.Melone, null, 22, SHOPTYPE.SONSTIGES));
        items.add(new ShopItem(Material.EGG, "§7Bridgeegg", "Halt ein Wassereimer?", 1, 20, Ressourcen.Melone, null, 24, SHOPTYPE.SONSTIGES));
        items.add(new ShopItem(Material.ENDER_PEARL, "§7Enderperle", "UwU", 1, 20, Ressourcen.Apfel, null, 30, SHOPTYPE.SONSTIGES));
        items.add(new ShopItem(Material.BLAZE_ROD, "§7Rettungsplattform", "Rettet dich beim runterfallen", 1, 20, Ressourcen.Melone, null, 32, SHOPTYPE.SONSTIGES));

    }

    public static void openShop(Player p, SHOPTYPE type) {
        fillPrices();
        Inventory shop = null;

        switch(type) {
            case CHOOSE:
                shop = Bukkit.createInventory(null, 9*6, "§cShop");
                shop.setContents(getStandardGUI(false));
                shop.setItem(4, ItemManager.newItem(Material.EMERALD, "§cShop", "", 0));
                shop.setItem(20, ItemManager.newItem(Material.STONE_SWORD, "§7Waffen", "", 0));
                shop.setItem(22, ItemManager.newItem(Material.IRON_CHESTPLATE, "§7Rüstung", "", 0));
                shop.setItem(24, ItemManager.newItem(Material.IRON_PICKAXE, "§7Tools", "", 0));
                shop.setItem(39, ItemManager.newItem(Material.SANDSTONE, "§7Blöcke", "", 2));
                shop.setItem(41, ItemManager.newItem(Material.TNT, "§7Extras", "", 0));
                break;
            case WEAPON:
                shop = Bukkit.createInventory(null, 9*6, "§cShop §8» §cWeapon");
                shop.setContents(getStandardGUI(true));
                shop.setItem(4, ItemManager.newItem(Material.STONE_SWORD, "§cWaffen-Shop", "", 0));
                for(ShopItem item : items) {
                    if(item.getType() == type) {
                        shop.setItem(item.getSlot(), item.getShopItem());
                    }
                }
                break;
            case ARMOR:
                shop = Bukkit.createInventory(null, 9*6, "§cShop §8» §cArmor");
                shop.setContents(getStandardGUI(true));
                shop.setItem(4, ItemManager.newItem(Material.IRON_CHESTPLATE, "§cRüstung-Shop", "", 0));
                for(ShopItem item : items) {
                    if(item.getType() == type) {
                        shop.setItem(item.getSlot(), item.getShopItem());
                    }
                }
                break;
            case TOOLS:
                shop = Bukkit.createInventory(null, 9*6, "§cShop §8» §cTools");
                shop.setContents(getStandardGUI(true));
                shop.setItem(4, ItemManager.newItem(Material.IRON_PICKAXE, "§cTool-Shop", "", 0));
                for(ShopItem item : items) {
                    if(item.getType() == type) {
                        shop.setItem(item.getSlot(), item.getShopItem());
                    }
                }
                break;
            case BLOCKS:
                shop = Bukkit.createInventory(null, 9*6, "§cShop §8» §cBlocks");
                shop.setContents(getStandardGUI(true));
                shop.setItem(4, ItemManager.newItem(Material.SANDSTONE, "§cBlöcke-Shop", "", 2));
                for(ShopItem item : items) {
                    if(item.getType() == type) {
                        shop.setItem(item.getSlot(), item.getShopItem());
                    }
                }
                break;
            case SONSTIGES:
                shop = Bukkit.createInventory(null, 9*6, "§cShop §8» §cSonstiges");
                shop.setContents(getStandardGUI(true));
                shop.setItem(4, ItemManager.newItem(Material.ENDER_PEARL, "§cSonstiges-Shop", "", 0));
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

    private static ItemStack[] getStandardGUI(boolean back) {
        Inventory inv = Bukkit.createInventory(null, 9*6, "");
        ItemStack glass = ItemManager.newItem(Material.STAINED_GLASS_PANE, " ", "", 7);
        ItemStack bglass = ItemManager.newItem(Material.STAINED_GLASS_PANE, " ", "", 15);
        ItemStack backI = ItemManager.newItem(Material.ARROW, "§cZurück", "", 0);
        ItemStack close = ItemManager.newItem(Material.ARROW, "§cSchließen", "", 0);

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

    public enum Ressourcen {
        Apfel, Melone, Karotte,
    }

    public static ShopItem getShopItemByItemStack(ItemStack item) {
        ShopItem a = null;
        for(ShopItem i : items) {
            if(i.getShopItem().isSimilar(item)) {
                a = i;
            }
        }
        return a;
    }

}
