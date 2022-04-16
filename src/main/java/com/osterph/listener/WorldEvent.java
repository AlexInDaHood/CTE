package com.osterph.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldEvent implements Listener{

	@EventHandler
	public void onWeather(WeatherChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlocks(BlockPhysicsEvent e) {
		if(!e.getChangedType().equals(Material.WATER) && !e.getChangedType().equals(Material.STATIONARY_WATER)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemDespawn(ItemDespawnEvent e) {
		e.setCancelled(true);
	}
	
}
