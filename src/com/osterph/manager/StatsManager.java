package com.osterph.manager;

import com.osterph.cte.CTE;
import com.osterph.lagerhalle.MySQL;
import org.bukkit.entity.Player;

import java.util.HashMap;

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

	public void addAchievement(Player p, int point) {
		if(points.containsKey(p)) {
			points.put(p, points.get(p)+point);
		} else {
			points.put(p, point);
		}
	}

	public enum ACHIEVEMENT {

	}
	
	public int getKills(Player p) {
		return kills.getOrDefault(p, 0);
	}
	
	public int getDeaths(Player p) {
		return deaths.getOrDefault(p, 0);
	}

	public int getPoints(Player p) {
		return points.getOrDefault(p, 0);
	}

	public int getAllPoints(Player p) {
		MySQL sql = CTE.mysql;

		int punkte = 0;

		for (int i = 15; i <= 24; i++) {
			int add = Integer.parseInt(sql.getDatabase("SPIELPUNKTE", "UUID", p.getUniqueId().toString(), i+"").toString());
			punkte = (add == -999) ? 0 : add+punkte;
		}

		return punkte;
	}

	public int getDayPoints(Player p, String date) {
		MySQL sql = CTE.mysql;

		int punkte = 0;

			int add = Integer.parseInt(sql.getDatabase("SPIELPUNKTE", "UUID", p.getUniqueId().toString(), date+"").toString());
			punkte = (add == -999) ? 0 : add+punkte;

		return punkte;
	}
	
}
