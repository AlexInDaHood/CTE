package com.osterph.cte;

import com.osterph.dev.*;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.osterph.lagerhalle.Emotes;
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
import com.osterph.manager.StatsManager;
import com.osterph.shop.Shop;
import com.osterph.shop.ShopListener;
import com.osterph.spawner.SpawnerManager;
import com.osterph.cte.CloudServerLogin;

public class CTE extends JavaPlugin{
	
	public static CTE INSTANCE;
	public static String prefix = "§8[§6CTE§8] §e";
	public static MySQL mysql;
	
	private CTESystem system;
	private SpawnerManager spawnermanager;
	private LocationLIST locations;
	private TeamSelector selector;
	private Shop shop;
	private StatsManager statsManager;
	
	@Override
	public void onEnable() {
		INSTANCE = this;
		system = new CTESystem();
		spawnermanager = new SpawnerManager();
		locations = new LocationLIST();
		selector = new TeamSelector();
		shop = new Shop();
		statsManager = new StatsManager();
		onSettings();
		register();

		mysql = new MySQL(getConfig().getString("host"), getConfig().getString("database"), getConfig().getString("user"), getConfig().getString("password"));
		String s = "";
		for (int i = 15; i <= 24; i++) {
			s += ", `"+i+"` int NOT NULL";
		}
		mysql.update("CREATE TABLE IF NOT EXISTS `SPIELPUNKTE` (`UUID` text NOT NULL"+s+");");

		for (Player all: Bukkit.getOnlinePlayers()) {
			ScoreboardManager.refreshBoard(all);
		}
		NPCListener.spawnNPCs();
		CloudServerLogin.login();
	}

	@Override
	public void onDisable() {
		CloudServerLogin.logout();
	}

	private void onSettings() {
		Bukkit.getWorld("world").setThundering(false);
		Bukkit.getWorld("world").setStorm(false);
		Bukkit.getWorld("world").setDifficulty(Difficulty.EASY);
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

		getCommand("suddendeath").setExecutor(new suddendeathCMD());
		getCommand("dev").setExecutor(new devCMD());
		getCommand("start").setExecutor(new startCMD());
		getCommand("countdown").setExecutor(new countdownCMD());
		getCommand("setteam").setExecutor(new setteamCMD());
		getCommand("emotes").setExecutor(new Emotes());

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
	
	public Shop getShop() {
		return shop;
	}

	public StatsManager getStatsManager() {
		return statsManager;
	}
	
}
