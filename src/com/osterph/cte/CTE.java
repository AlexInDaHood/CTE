package com.osterph.cte;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.osterph.dev.DevListener;
import com.osterph.dev.countdownCMD;
import com.osterph.dev.devCMD;
import com.osterph.dev.setteamCMD;
import com.osterph.dev.startCMD;
import com.osterph.lagerhalle.LocationLIST;
import com.osterph.lagerhalle.MySQL;
import com.osterph.lagerhalle.NPCListener;
import com.osterph.lagerhalle.TeamSelector;
import com.osterph.listener.BlockListener;
import com.osterph.listener.ChatListener;
import com.osterph.listener.DamageListener;
import com.osterph.listener.EggListener;
import com.osterph.listener.InteractEvent;
import com.osterph.listener.PlayerEvent;
import com.osterph.listener.WorldEvent;
import com.osterph.manager.ScoreboardManager;
import com.osterph.shop.ShopListener;
import com.osterph.spawner.SpawnerManager;

public class CTE extends JavaPlugin{
	
	public static CTE INSTANCE;
	public static String prefix = "§8[§6CTE§8] §e";
	public static MySQL mysql;
	
	private CTESystem system;
	private SpawnerManager spawnermanager;
	private LocationLIST locations;
	private TeamSelector selector;
	@Override
	public void onEnable() {
		INSTANCE = this;
		system = new CTESystem();
		spawnermanager = new SpawnerManager();
		locations = new LocationLIST();
		selector = new TeamSelector();
		onSettings();
		register();
		

		String host = "45.85.219.177";
		String pw = "aFUWBuNMzUDygbhZ";
		String db = "Ostern";
		String user = "PlayHills";

		mysql = new MySQL(host, db, user, pw);

		mysql.update("CREATE TABLE IF NOT EXISTS `PLAYERPOINTS` (`UUID` text NOT NULL, `POINTS` int NOT NULL);");

		for (Player all: Bukkit.getOnlinePlayers()) {
			ScoreboardManager.refreshBoard(all);
			system.punkte.put(all, 0);
			system.kills.put(all, 0);
		}
		NPCListener.spawnNPCs();
	}

	private void onSettings() {
		Bukkit.getWorld("world").setThundering(false);
		Bukkit.getWorld("world").setStorm(false);
		Bukkit.getWorld("world").setDifficulty(Difficulty.PEACEFUL);
		Bukkit.getWorld("world").setGameRuleValue("doMobSpawning", "false");
		Bukkit.getWorld("world").setGameRuleValue("doMobLoot", "false");
		Bukkit.getWorld("world").setGameRuleValue("randomTickSpeed", "0");
		Bukkit.getWorld("world").setGameRuleValue("doFireTick", "false");
		Bukkit.getWorld("world").setGameRuleValue("doDaylightCycle", "false");
		Bukkit.getWorld("world").setGameRuleValue("mobGriefing", "false");
		Bukkit.getWorld("world").setTime(6000);
		
		spawnermanager.addSpawner();
	}

	private void register() {
		PluginManager pm= Bukkit.getPluginManager();

		pm.registerEvents(new PlayerEvent(), this);
		pm.registerEvents(new EggListener(), this);
		pm.registerEvents(new BlockListener(), this);
		pm.registerEvents(new ShopListener(), this);
		pm.registerEvents(new ChatListener(), this);
		pm.registerEvents(new InteractEvent(), this);
		pm.registerEvents(new DamageListener(), this);
		pm.registerEvents(new NPCListener(), this);
		pm.registerEvents(new TeamSelector(), this);
		pm.registerEvents(new WorldEvent(), this);
		pm.registerEvents(new DevListener(), this);
		
		
		getCommand("dev").setExecutor(new devCMD());
		getCommand("start").setExecutor(new startCMD());
		getCommand("countdown").setExecutor(new countdownCMD());
		getCommand("setteam").setExecutor(new setteamCMD());
	}
	
	public SpawnerManager getSpawnermanager() {
		return spawnermanager;
	}
	
	public LocationLIST getLocations() {
		return locations;
	}
	
	public CTESystem getSystem() {
		return system;
	}
	public TeamSelector getSelector() {
		return selector;
	}
	
}
