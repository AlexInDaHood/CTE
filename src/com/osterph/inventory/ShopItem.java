package com.osterph.inventory;

import com.osterph.cte.CTESystem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopItem {

    Material mat;
    String name;
    String description;
    int cost;
    Shop.Ressourcen ressource;
    int amount;
    HashMap<Enchantment, Integer> enchants;
    int slot;
    Shop.SHOPTYPE type;


    public ShopItem(Material mat, String name, String description, int amount, int cost, Shop.Ressourcen resource, HashMap<Enchantment, Integer> enchants, int slot, Shop.SHOPTYPE type) {
        this.mat = mat;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.ressource = resource;
        this.amount = amount;
        if(enchants != null) {
            this.enchants = enchants;
        }
        this.slot = slot;
        this.type = type;
    }

    public ItemStack getInventoryItem() {
        ItemStack item = new ItemStack(mat, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        //ArrayList<String> lore = new ArrayList<>();
        //lore.add("§7"+description);
        //meta.setLore(lore);
        if(enchants != null) {
            for (Enchantment ench : enchants.keySet()) {
                meta.addEnchant(ench, enchants.get(ench), true);
            }
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getShopItem(int id) {
            ItemStack item = new ItemStack(mat, amount, (byte) 0);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            ArrayList<String> lore = new ArrayList<>();
            lore.add("§7Cost: " + ResourceToColor() + cost + " " + ResourceToString());
            lore.add(" ");
            lore.add("§7" + description);
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            if (enchants != null) {
                for (Enchantment ench : enchants.keySet()) {
                    meta.addEnchant(ench, enchants.get(ench), true);
                }
            }
            item.setItemMeta(meta);
            return item;
    }

    private String ResourceToString() {
        String a = "";
        if(amount > 1) {
            switch (ressource) {
                case Apfel:
                    a = "Äpfel";
                    break;
                case Melone:
                    a = "Melonen";
                    break;
                case Karotte:
                    a = "Karotten";
                    break;
            }
        } else {
            switch (ressource) {
                case Apfel:
                    a = "Apfel";
                    break;
                case Melone:
                    a = "Melone";
                    break;
                case Karotte:
                    a = "Karotte";
                    break;
            }
        }
        return a;
    }

    private String ResourceToColor() {
        String a = "";
        switch(ressource) {
            case Apfel:
                a = "§c";
                break;
            case Melone:
                a = "§a";
                break;
            case Karotte:
                a = "§6";
                break;
        }
        return a;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEnchants(HashMap<Enchantment, Integer> enchants) {
        this.enchants = enchants;
    }

    public void setMat(Material mat) {
        this.mat = mat;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRessource(Shop.Ressourcen ressource) {
        this.ressource = ressource;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Material getMat() {
        return mat;
    }

    public int getAmount() {
        return amount;
    }

    public int getCost() {
        return cost;
    }

    public int getSlot() {
        return slot;
    }

    public Shop.Ressourcen getRessource() {
        return ressource;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public HashMap<Enchantment, Integer> getEnchants() {
        return enchants;
    }

    public Shop.SHOPTYPE getType() {
        return type;
    }

    //getters und setters





}
