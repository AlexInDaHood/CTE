package com.osterph.spawner;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerManager {
	
	public ArrayList<Spawner> spawners = new ArrayList<>();
	
	public void addSpawner() {
		ItemStack item = new ItemStack(Material.APPLE);
		ItemMeta name = item.getItemMeta();
		name.setDisplayName("§6Apfel");
		item.setItemMeta(name);
		spawners.add(new Spawner("BLUE-APPLE", item, new Location(Bukkit.getWorld("world"), 1069.5, 100, 995.5), 20, 1, 10));
		
	}
	
	public void aktivateSpawner() {
		for(Spawner spawner : spawners) {
			spawner.onStart();
		}
	}
	
	public Spawner getSpawnerByName(String name) {
		for(Spawner sp : spawners) {
			if(sp.getName().equals(name)) {
				return sp;
			}
		}
		return null;
	}
	
}
