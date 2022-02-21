package com.osterph.cte;

import org.bukkit.plugin.java.JavaPlugin;

public class CTE extends JavaPlugin{
	
	public static CTE INSTANCE;
	public static String prefix = "§8[§dCTE§8] §5";
	public static MySQL mysql;

	public boolean isStarted = false;


	public CTESystem system;



	@Override
	public void onEnable() {
		INSTANCE = this;
		
		
	}
	
	@Override
	public void onDisable() {
	}
	
}
