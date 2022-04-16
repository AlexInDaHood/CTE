package com.osterph.spawner;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.osterph.manager.DropManager;
import com.osterph.shop.Shop.Ressourcen;

public class SpawnerManager {
	
	public ArrayList<Spawner> spawners = new ArrayList<>();
	
	public void addSpawner() {
		DropManager apple = new DropManager(Ressourcen.Apfel);
		DropManager carrot = new DropManager(Ressourcen.Karotte);
		DropManager melon = new DropManager(Ressourcen.Melone);
		
		
		//BLUE
		spawners.add(new Spawner("BLUE-APPLE", apple.getItem(1), new Location(Bukkit.getWorld("world"), 1060.5, 100, 993.5), 20, 1, 30, true));
		spawners.add(new Spawner("BLUE-MELON-1", melon.getItem(1), new Location(Bukkit.getWorld("world"), 1037.5, 104, 1018.5), 20*6, 1, 10, false));
		spawners.add(new Spawner("BLUE-MELON-2", melon.getItem(1), new Location(Bukkit.getWorld("world"), 1037.5, 104, 982.5), 20*6, 1, 10, false));
		
		//MIDDLE
		spawners.add(new Spawner("MIDDLE-CARROT-1", carrot.getItem(1), new Location(Bukkit.getWorld("world"), 1001.5, 100, 1011.5), 20*50, 1, 2, false));
		spawners.add(new Spawner("MIDDLE-CARROT-2", carrot.getItem(1), new Location(Bukkit.getWorld("world"), 988.5, 100, 999.5), 20*50, 1, 2, false));
		spawners.add(new Spawner("MIDDLE-CARROT-3", carrot.getItem(1), new Location(Bukkit.getWorld("world"), 1005.5, 100, 997.5), 20*50, 1, 2, false));
		
		//RED																											
		spawners.add(new Spawner("RED-APPLE", apple.getItem(1), new Location(Bukkit.getWorld("world"), 940.5, 100, 1007.5), 20, 1, 30, true));
		spawners.add(new Spawner("RED-MELON-1", melon.getItem(1), new Location(Bukkit.getWorld("world"), 963.5, 104, 982.5), 20*6, 1, 10, false));
		spawners.add(new Spawner("RED-MELON-2", melon.getItem(1), new Location(Bukkit.getWorld("world"), 963.5, 104, 1018.5), 20*6, 1, 10, false));
		
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
