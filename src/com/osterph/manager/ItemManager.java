package com.osterph.manager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class ItemManager {

    private Material m;
    private String ItemName;
    private int amount;
    private int data;
    private ItemMeta meta;
    private List<String> lores = new ArrayList<>();
    private boolean unbreakable;
    private boolean HIDEunbreakable;
    private HashMap<Enchantment, Integer> ench = new HashMap<>();
    private boolean HIDEench = false;

    public ItemManager(Material m) {
        this.m = m;
        this.amount = 1;
       this.data = 0;
        this.meta = null;
        this.ItemName = null;
        this.lores = new ArrayList<>();
        this.unbreakable = false;
        this.HIDEunbreakable = false;
        this.ench = new HashMap<>();
        this.HIDEench = false;
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

    public ItemManager withEnchantment(Enchantment e, int level, boolean hide) {
        this.ench.put(e, level);
        this.HIDEench = hide;
        return this;
    }

    public ItemManager withLores(String... lores) {
        this.lores = Arrays.asList(lores);
        return this;
    }

    public ItemManager withLores(List<String> lores) {
        this.lores = lores;
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
        if (HIDEench) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        meta.setLore(this.lores);
        if (this.ItemName != null) meta.setDisplayName(this.ItemName);

        item.setItemMeta(meta);
        item.setAmount(amount);
        item.setDurability((short) data);
        if (!ench.isEmpty()) {
            for (Enchantment e : ench.keySet()) {
                try {
                    item.addEnchantment(e, ench.get(e));
                } catch (Exception ex) {
                    item.addUnsafeEnchantment(e, ench.get(e));
                }
            }
        }

        return item;
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
