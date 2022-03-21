package com.osterph.manager;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class StatsManager {
	
	HashMap<Player, Integer> kills = new HashMap<>();
	HashMap<Player, Integer> deaths = new HashMap<>();
	HashMap<Player, Integer> points = new HashMap<>();
	
	public StatsManager() {
		
	}
	
	public void addKill(Player p) {
		if(kills.containsKey(p)) {
			kills.put(p, kills.get(p)+1);
		} else {
			kills.put(p, 1);
		}
	}
	
	public void addDeath(Player p) {
		if(deaths.containsKey(p)) {
			deaths.put(p, deaths.get(p)+1);
		} else {
			deaths.put(p, 1);
		}
	}
	
	public void addPoints(Player p, int point) {
		if(points.containsKey(p)) {
			points.put(p, points.get(p)+point);
		} else {
			points.put(p, point);
		}
	}
	
	public int getKills(Player p) {
		if(kills.containsKey(p)) {
			return kills.get(p);
		} else {
			return 0;
		}
	}
	
	public int getDeaths(Player p) {
		if(deaths.containsKey(p)) {
			return deaths.get(p);
		} else {
			return 0;
		}
	}
	
	public int getPoints(Player p) {
		if(points.containsKey(p)) {
			return points.get(p);
		} else {
			return 0;
		}
	}
	
}
