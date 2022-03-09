package com.osterph.listener;

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
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onItemDespawn(ItemDespawnEvent e) {
		e.setCancelled(true);
	}
	
}
