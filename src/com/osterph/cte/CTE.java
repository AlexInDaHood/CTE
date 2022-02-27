package com.osterph.cte;

import com.osterph.lagerhalle.MySQL;
import com.osterph.lagerhalle.NPCListener;
import com.osterph.manager.ScoreboardManager;
import com.osterph.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CTE extends JavaPlugin{
	
	public static CTE INSTANCE;
	public static String prefix = "§8[§6CTE§8] §e";
	public static MySQL mysql;
	public CTESystem system;

	@Override
	public void onEnable() {
		INSTANCE = this;
		system = new CTESystem();
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
	}

	private void register() {
		PluginManager pm= Bukkit.getPluginManager();

		pm.registerEvents(new PlayerEvent(), this);
		pm.registerEvents(new EggListener(), this);
		pm.registerEvents(new BlockListener(), this);
	}
	
}
