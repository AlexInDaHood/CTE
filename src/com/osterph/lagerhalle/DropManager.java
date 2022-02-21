package com.osterph.lagerhalle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DropManager {

    private DROP d;

    public DropManager(DROP d) {
        this.d = d;
    }

    private enum DROP {
        CARROT, MELON, APPLE
    }

    public ItemStack getItem(int amount) {
        switch (this.d) {
            case CARROT: {
                return new Carrot().getItem(amount);
            }
            case MELON: {
                return new Melon().getItem(amount);
            }
            case APPLE: {
                return new Apple().getItem(amount);
            }
        }
        return new ItemStack(Material.AIR);
    }

    public List<Location> getLocations() {
        switch (this.d) {
            case CARROT: {
                return new Carrot().getLocations();
            }
            case MELON: {
                return new Melon().getLocations();
            }
            case APPLE: {
                return new Apple().getLocations();
            }
        }
        return new ArrayList<>();
    }


    public void spawnItem(Location l) {
        switch (this.d) {
            case CARROT: {
                new Carrot().spawnCarrot(l);
                return;
            }
            case MELON: {
                new Melon().spawnMelon(l);
                return;
            }
            case APPLE: {
                new Apple().spawnApple(l);
                return;
            }
        }
    }

    public void spawnAllItems() {
        switch (this.d) {
            case CARROT: {
                new Carrot().spawnAllCarrots();
                return;
            }
            case MELON: {
                new Melon().spawnAllMelons();
                return;
            }
            case APPLE: {
                new Apple().spawnAllApples();
                return;
            }
        }
    }

}

class Carrot {
    public Carrot() {}

    public ItemStack getItem(int amount) {
        ItemStack i = new ItemStack(Material.CARROT_ITEM, amount);

        ItemMeta m = i.getItemMeta();
        m.setDisplayName("§6Karotte");
        i.setItemMeta(m);

        return i;
    }

    public List<Location> getLocations() {
        World w = Bukkit.getWorld("world");
        List<Location> l = new ArrayList<>();

        l.add(new Location(w, 0, 5, 0));

        return l;
    }

    public void spawnCarrot(Location loc) {
        loc.getWorld().dropItem(loc, getItem(1));
    }

    public void spawnAllCarrots() {
        for (Location l : getLocations()) {
            l.getWorld().dropItem(l, getItem(1));
        }
    }

}

class Melon {
    public Melon() {}

    public ItemStack getItem(int amount) {
        ItemStack i = new ItemStack(Material.MELON, amount);

        ItemMeta m = i.getItemMeta();
        m.setDisplayName("§aMelone");
        i.setItemMeta(m);

        return i;
    }

    public List<Location> getLocations() {
        World w = Bukkit.getWorld("world");
        List<Location> l = new ArrayList<>();

        l.add(new Location(w, 0, 5, 0));

        return l;
    }

    public void spawnMelon(Location loc) {
        loc.getWorld().dropItem(loc, getItem(1));
    }

    public void spawnAllMelons() {
        for (Location l : getLocations()) {
            l.getWorld().dropItem(l, getItem(1));
        }
    }

}

class Apple {
    public Apple() {}

    public ItemStack getItem(int amount) {
        ItemStack i = new ItemStack(Material.APPLE, amount);

        ItemMeta m = i.getItemMeta();
        m.setDisplayName("§cApfel");
        i.setItemMeta(m);

        return i;
    }

    public List<Location> getLocations() {
        World w = Bukkit.getWorld("world");
        List<Location> l = new ArrayList<>();

        l.add(new Location(w, 0, 5, 0));

        return l;
    }

    public void spawnApple(Location loc) {
        loc.getWorld().dropItem(loc, getItem(1));
    }

    public void spawnAllApples() {
        for (Location l : getLocations()) {
            l.getWorld().dropItem(l, getItem(1));
        }
    }

}
