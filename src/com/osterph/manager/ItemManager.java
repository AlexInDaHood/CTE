package com.osterph.manager;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class ItemManager {

    private Material m;
    private String ItemName;
    private int amount;
    private int data;
    private ItemMeta meta;
    private List<String> lores = new ArrayList<>();
    private boolean unbreakable;
    private boolean HIDEunbreakable;

    public ItemManager(Material m) {
        this.m = m;
    }

    public ItemManager withName(String ItemName) {
        this.ItemName = ItemName;
        return this;
    }

    public ItemManager withAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemManager withData(int data) {
        this.data = data;
        return this;
    }

    public ItemManager withMeta(ItemMeta meta) {
        this.meta = meta;
        return this;
    }

    public ItemManager withLores(String... lores) {
        this.lores = Arrays.asList(lores);
        return this;
    }

    public ItemManager unbreakable(boolean HideFlag) {
        this.unbreakable = true;
        this.HIDEunbreakable = HideFlag;
        return this;
    }

    public ItemStack complete() {
        ItemStack item = new ItemStack(this.m);
        ItemMeta meta = item.getItemMeta();
        if (this.meta != null) meta = this.meta;

        meta.spigot().setUnbreakable(unbreakable);
        if (HIDEunbreakable) meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        meta.setLore(this.lores);
        if (this.ItemName != null) meta.setDisplayName(this.ItemName);

        item.setItemMeta(meta);
        item.setAmount(amount);

        return item;
    }

    public static ItemStack newItem(Material item, String name, String lore, int id) {
        ItemStack stack;
        if(id <= 0) {
            stack = new ItemStack(item);
        } else {
            stack = new ItemStack(item, 1, (byte) id);
        }
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        if(!lore.isEmpty() && lore != "null" && lore != " ") {
            ArrayList<String> lr = new ArrayList<>();
            if(lore.contains("-/-")) {
                String[] la = lore.split("-/-");
                for(int i=0;i<la.length;i++) {
                    lr.add(la[i]);
                }
            } else {
                lr.add(lore);
            }
            meta.setLore(lr);
        }
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack newItem(Material item, String name, String lore, int id, HashMap<Enchantment, Integer> enchantments) {
        ItemStack stack;
        if(id <= 0) {
            stack = new ItemStack(item);
        } else {
            stack = new ItemStack(item, 1, (byte) id);
        }
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        if(!lore.isEmpty() && lore != "null" && lore != " " && lore != "") {
            ArrayList<String> lr = new ArrayList<>();
            if(lore.contains("-/-")) {
                String[] la = lore.split("-/-");
                for(int i=0;i<la.length;i++) {
                    lr.add(la[i]);
                }
            } else {
                lr.add(lore);
            }
            meta.setLore(lr);
        }
        if(enchantments != null) {
            for(Enchantment ench : enchantments.keySet()) {
                meta.addEnchant(ench, enchantments.get(ench), true);
            }
        }


        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack customHead(String name, String lore, String value) {
        ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        if(value.isEmpty()) return stack;
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", value));

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch(IllegalAccessException|IllegalArgumentException|NoSuchFieldException|SecurityException e) {
            e.printStackTrace();
        }

        meta.setDisplayName(name);
        if(!lore.isEmpty() && lore != "null" && lore != " " && lore != "") {
            ArrayList<String> lr = new ArrayList<>();
            if(lore.contains("-/-")) {
                String[] la = lore.split("-/-");
                for(int i=0;i<la.length;i++) {
                    lr.add(la[i]);
                }
            } else {
                lr.add(lore);
            }
            meta.setLore(lr);
        }
        stack.setItemMeta(meta);
        return stack;
    }

}
