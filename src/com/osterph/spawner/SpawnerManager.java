package com.osterph.spawner;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SpawnerManager {
	
	public ArrayList<Spawner> spawners = new ArrayList<>();
	
	public void addSpawner() {
		spawners.add(new Spawner(new ItemStack(Material.APPLE), new Location(Bukkit.getWorld("world"), 1096.5, 100, 995.5), 20, 1, 10));
		
	}
	
	public void aktivateSpawner() {
		for(Spawner spawner : spawners) {
			spawner.onStart();
		}
	}
	
	public Spawner getSpawnerByLocation(Location loc) {
		return null;
	}
	
}
