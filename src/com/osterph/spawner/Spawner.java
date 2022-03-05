package com.osterph.spawner;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.osterph.cte.CTE;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class Spawner {
	
	ItemStack item;
	Location loc;
	int delay;
	int amount;
	int max;
	
	int currentitems = 0;
	
	private int scheduler;
	
	public Spawner(ItemStack item, Location loc, int delay, int amount, int max) {
		this.item = item;
		this.loc = loc;
		this.delay = delay;
		this.max = max;
		this.amount = amount;
	}
	
	public void onStart() {
		System.out.println("STARTED");
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
	
}
