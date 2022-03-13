package com.osterph.spawner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.osterph.cte.CTE;


public class Spawner {

	String name;
	ItemStack item;
	Location loc;
	int delay;
	int amount;
	int max;
	
	boolean activated;
	
	int currentitems = 0;
	
	private int scheduler;
	
	public Spawner(String name, ItemStack item, Location loc, int delay, int amount, int max) {
		this.loc = loc;
		this.delay = delay;
		this.max = max;
		this.amount = amount;
		this.name = name;
		activated = false;
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(meta.getDisplayName() + "[" + name + "]");
		item.setItemMeta(meta);
		this.item = item;
	}
	
	public void onStart() {
		activated = true;
		scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, new Runnable() {			
			@Override
			public void run() {
				if(currentitems < max) {
					for(int i=0;i<amount;i++) {
						Item it = loc.getWorld().dropItem(loc, item);
						it.setVelocity(new Vector(0, 0.001, 0));
						currentitems++;
					}
				}
			}
		}, 0, delay);
	}
	
	public void onStop() {
		activated = false;
		try {
			Bukkit.getScheduler().cancelTask(scheduler);
		} catch(Exception e) {}
		currentitems = 0;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
		if(activated) {
			onStop();
			onStart();
		}
	}
	
	public void setMax(int max) {
		this.max = max;
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
	
	public void toggle() {
		if(activated) {
			onStop();
		} else {
			onStart();
		}		
	}
	
	public void setActivated(boolean activated) {
		if(activated) {
			onStart();
		} else {
			onStop();
		}
	}
	
	public boolean isActivated() {
		return activated;
	}
	
}
