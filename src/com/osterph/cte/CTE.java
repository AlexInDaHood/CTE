package com.osterph.cte;

import com.osterph.lagerhalle.MySQL;
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


		String host = "45.85.219.177";
		String pw = "aFUWBuNMzUDygbhZ";
		String db = "Ostern";
		String user = "PlayHills";

		mysql = new MySQL(host, db, user, pw);

		mysql.update("CREATE TABLE IF NOT EXISTS `PLAYERPOINTS` (`UUID` text NOT NULL, `POINTS` int NOT NULL);");
	}
	
	@Override
	public void onDisable() {
		for (Player all: Bukkit.getOnlinePlayers()) all.kickPlayer("§4SERVER RELOAD");
	}
	
}
