package com.osterph.lagerhalle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationLIST {
    private World w;

    public LocationLIST() {
        w = Bukkit.getWorld("world");
    }

    public Location lobbySPAWN() {
        return new Location(w, 0, 50, 0);
    }

    public Location blueSPAWN() {
        return new Location(w, 1040, 50, 1000);
    }

    public Location blueEGG() {return new Location(w, 111, 50, 111);}
    public Location redEGG() {return new Location(w, 222, 50, 222);}



    public Location redSPAWN() {
        return new Location(w, 960, 50, 1000);
    }

    public Location specSPAWN() {
        return new Location(w, 1000, 50, 1000);
    }
}
