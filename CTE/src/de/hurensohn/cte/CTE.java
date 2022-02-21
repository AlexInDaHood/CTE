package de.hurensohn.cte;

import org.bukkit.plugin.java.JavaPlugin;

public class CTE extends JavaPlugin{
	
	public static CTE INSTANCE;
	public static String prefix = "§8[§dCTE§8] §d";
	
	
	@Override
	public void onEnable() {
		INSTANCE = this;
		
		
	}
	
	@Override
	public void onDisable() {
	}
	
}
