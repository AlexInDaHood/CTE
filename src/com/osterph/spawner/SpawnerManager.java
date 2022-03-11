package com.osterph.spawner;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.osterph.manager.DropManager;
import com.osterph.manager.DropManager.DROP;

public class SpawnerManager {
	
	public ArrayList<Spawner> spawners = new ArrayList<>();
	
	public void addSpawner() {
		DropManager apple = new DropManager(DROP.APPLE);
		DropManager carrot = new DropManager(DROP.CARROT);
		DropManager melon = new DropManager(DROP.MELON);
		
		
		//BLUE
		spawners.add(new Spawner("BLUE-APPLE", apple.getItem(1), new Location(Bukkit.getWorld("world"), 1069.5, 100, 995.5), 20, 1, 30));
		spawners.add(new Spawner("BLUE-MELON-1", melon.getItem(1), new Location(Bukkit.getWorld("world"), 1037.4, 104, 1028.5), 20*6, 1, 10));
		spawners.add(new Spawner("BLUE-MELON-2", melon.getItem(1), new Location(Bukkit.getWorld("world"), 1037.4, 104, 974.5), 20*6, 1, 10));
		
		//MIDDLE
		spawners.add(new Spawner("MIDDLE-CARROT-1", carrot.getItem(1), new Location(Bukkit.getWorld("world"), 1001.5, 100, 1011.5), 20*30, 1, 2));
		spawners.add(new Spawner("MIDDLE-CARROT-2", carrot.getItem(1), new Location(Bukkit.getWorld("world"), 988.5, 100, 999.5), 20*30, 1, 2));
		spawners.add(new Spawner("MIDDLE-CARROT-1", carrot.getItem(1), new Location(Bukkit.getWorld("world"), 1005.5, 100, 997.5), 20*30, 1, 2));
		
		//RED
		spawners.add(new Spawner("RED-APPLE", apple.getItem(1), new Location(Bukkit.getWorld("world"), 931.5, 100, 1005.5), 20, 1, 30));
		spawners.add(new Spawner("RED-MELON-1", melon.getItem(1), new Location(Bukkit.getWorld("world"), 964.4, 104, 975.5), 20 *6, 1, 10));
		spawners.add(new Spawner("RED-MELON-2", melon.getItem(1), new Location(Bukkit.getWorld("world"), 964.4, 104, 1026.5), 20 * 6, 1, 10));
		
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
