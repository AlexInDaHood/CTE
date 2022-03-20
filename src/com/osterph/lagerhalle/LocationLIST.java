package com.osterph.lagerhalle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class LocationLIST {
    private World w = Bukkit.getWorld("world");
    
    public Location lobbySPAWN() {return new Location(w, 0, 30, 0, 90, 10);}

    public Location blueSPAWN() {return new Location(w, 1060.5, 100, 1000.5, 90, 10);} //
    
    public Location redSPAWN() {return new Location(w, 940.5, 100, 1000.5, 270, 10);} //

    public Location blueEGG() {return new Location(w, 1065, 102, 1000);} //
    public Location redEGG() {return new Location(w, 935, 102, 1000);} //

    public Location blueNPC() {return new Location(w, 1061.5, 100, 1006.5, -180, 0);}
    public Location redNPC() {return new Location(w, 939.5, 100, 994.5, 0, 0);} //

    public Location specSPAWN() {return new Location(w, 1000, 104, 1000, 180, 10);}
    
    
    public void shopkeeperStand() {
        for (Entity all: w.getEntities()) {
            if (all instanceof Player) continue;
            all.remove();
        }

        w.spawnEntity(blueNPC().add(0.25, 0, 0), EntityType.ARMOR_STAND);
        w.spawnEntity(blueNPC().add(-0.25, 0, 0), EntityType.ARMOR_STAND);
        w.spawnEntity(blueNPC().add(0, 0, 0.25), EntityType.ARMOR_STAND);
        w.spawnEntity(blueNPC().add(0, 0, -0.25), EntityType.ARMOR_STAND);
        w.spawnEntity(redNPC().add(0.25, 0, 0), EntityType.ARMOR_STAND);
        w.spawnEntity(redNPC().add(-0.25, 0, 0), EntityType.ARMOR_STAND);
        w.spawnEntity(redNPC().add(0, 0, 0.25), EntityType.ARMOR_STAND);
        w.spawnEntity(redNPC().add(0, 0, -0.25), EntityType.ARMOR_STAND);

        for (Entity all: w.getEntities()) {
            if (!(all instanceof ArmorStand)) continue;
            ((ArmorStand)all).setVisible(false);
            (all).setCustomName("[SHOPKEEPER]");
            ((ArmorStand)all).setBasePlate(false);
            ((ArmorStand)all).setGravity(false);
            (all).setCustomNameVisible(false);
        }
    }
}
