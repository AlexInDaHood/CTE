package com.osterph.spawner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.osterph.cte.CTE;


public class Spawner {

	String name;
	ItemStack item;
	Location loc;
	int delay;
	int amount;
	int max;
	
	int currentitems = 0;
	
	private int scheduler;
	
	public Spawner(String name, ItemStack item, Location loc, int delay, int amount, int max) {
		this.loc = loc;
		this.delay = delay;
		this.max = max;
		this.amount = amount;
		this.name = name;
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(meta.getDisplayName() + "[" + name + "]");
		item.setItemMeta(meta);
		this.item = item;
	}
	
	public void onStart() {
		scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, new Runnable() {			
			@Override
			public void run() {
				if(currentitems < max) {
					for(int i=0;i<amount;i++) {
						loc.getWorld().dropItem(loc, item);
						currentitems++;
					}
				}
			}
		}, 0, delay);
	}
	
	public void onStop() {
		Bukkit.getScheduler().cancelTask(scheduler);
		currentitems = 0;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
		onStop();
		onStart();
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	
	public Location getLocation() {
		return loc;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public int getMaximal() {
		return max;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public int getCurrentitems() {
		return currentitems;
	}
	
	public void setCurrentitems(int currentitems) {
		this.currentitems = currentitems;
	}

	public String getName() {
		return name;
	}
}
