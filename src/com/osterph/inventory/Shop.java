package com.osterph.inventory;

import com.osterph.lagerhalle.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class Shop {

    private HashMap<ItemStack, String> prices = new HashMap<>();

    private void fillPrices() {
        HashMap<Enchantment, Integer> level = new HashMap<>();
        level.put(Enchantment.ARROW_DAMAGE, 1);

        prices.put(ItemManager.newItem(Material.STONE_SWORD, "§7Stein Schwert", "", 0), "10-Apfel");
        prices.put(ItemManager.newItem(Material.BOW, "§cGuter Bogen", "", 0, level), "10-Karotten");


    }
    public static void openShop(Player p, SHOPTYPE type) {
        Inventory shop = null;

        switch(type) {
            case CHOOSE:
                shop = Bukkit.createInventory(null, 9*6, "§a§b§cShop");
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
                shop.setItem(4, ItemManager.newItem(Material.STONE_SWORD, "§cWaffe-Shop", "", 0));
                shop.setItem(20, ItemManager.newItem(Material.STONE_SWORD, "§aStein-Schwert", "", 0));

                break;
        }

        p.openInventory(shop);
    }


    private HashMap<ItemStack, Integer> weapon = new HashMap<>();
    private HashMap<ItemStack, Integer> armor = new HashMap<>();
    private HashMap<ItemStack, Integer> tools = new HashMap<>();
    private HashMap<ItemStack, Integer> blocks = new HashMap<>();
    private HashMap<ItemStack, Integer> sonstiges = new HashMap<>();


    private void fillShop(Inventory inv, Player p, SHOPTYPE type) {
        fillHash(type);
        switch(type) {
            case WEAPON:
                for(ItemStack item : weapon.keySet()) {
                    ItemMeta meta = item.getItemMeta();
                    String[] a = prices.get(item).split("-");

                    item.setItemMeta(meta);
                    inv.setItem(weapon.get(item), item);
                }
                break;
            case ARMOR:
                break;
            case TOOLS:
                break;
            case BLOCKS:
                break;
            case SONSTIGES:
                break;
        }

    }

    private void fillHash(SHOPTYPE type) {
        switch(type) {
            case WEAPON:
                break;
            case ARMOR:
                break;
            case TOOLS:
                break;
            case BLOCKS:
                break;
            case SONSTIGES:
                break;
        }

    }

    public enum SHOPTYPE {
        CHOOSE, WEAPON, ARMOR, TOOLS, BLOCKS, SONSTIGES;
    }

    private static ItemStack[] getStandardGUI(boolean back) {
        Inventory inv = Bukkit.createInventory(null, 9*6, "");
        ItemStack glass = ItemManager.newItem(Material.STAINED_GLASS_PANE, " ", "", 7);
        ItemStack bglass = ItemManager.newItem(Material.STAINED_GLASS_PANE, " ", "", 15);
        ItemStack backI = ItemManager.newItem(Material.ARROW, "§cZurück", "", 0);
        ItemStack close = ItemManager.newItem(Material.STAINED_GLASS_PANE, "§cSchließen", "", 14);

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

    private enum Ressourcen {
        Apfel, Melone, Karotte,
    }

}
