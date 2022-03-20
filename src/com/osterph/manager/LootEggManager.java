package com.osterph.manager;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.cte.CTESystem.GAMESTATE;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.TileEntitySkull;


public class LootEggManager implements Listener {
	
	ArrayList<Location> locs = new ArrayList<>();
	
	private String skullTextures = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjE1NGJhNDJkMjgyMmY1NjUzNGU3NTU4Mjc4OTJlNDcxOWRlZWYzMjhhYmI1OTU4NGJlNjk2N2YyNWY0OGNiNCJ9fX0=";
	
	public LootEggManager() {
		addLocations();
			
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(BlockBreakEvent e) {
		if(!sys.gamestate.equals(GAMESTATE.RUNNING)) return;
		Block b = e.getBlock();
		if (!(b instanceof TileEntitySkull)) return;
		TileEntitySkull skull = (TileEntitySkull)((CraftWorld)b.getWorld()).getHandle().getTileEntity(new BlockPosition(b.getX(), b.getY(), b.getZ()));
		if(!skull.getGameProfile().getProperties().get("textures").iterator().next().getValue().equals(skullTextures)) return;
		e.setCancelled(true);
		e.getBlock().setType(Material.AIR);
		Bukkit.getScheduler().cancelTask(scheduler);
		removeEgg(e.getBlock().getLocation());
		openEgg(e.getBlock().getLocation());
		startQueue();
	}
	
	private int newTime = -1;
	private int scheduler = -999;
	
	private int currentTime = 0;
	
	public void startQueue() {
		currentTime = 0;
		generatenewTime();
		Bukkit.getScheduler().cancelTask(scheduler);
		scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, new Runnable() {			
			@Override
			public void run() {
				currentTime++;
				if(currentTime == newTime) {
					spawnEgg();
				}
			}
		}, 0, 20);
	}
	
	private CTESystem sys = CTE.INSTANCE.getSystem();
	
	public void spawnEgg() {
		Bukkit.getScheduler().cancelTask(scheduler);
		int rndmLoc = new Random().nextInt(locs.size());
		Location loc = locs.get(rndmLoc);
		placeEgg(loc);
		sys.sendAllMessage(CTE.prefix + "§7Ein §eLootEi §7ist gespawnt! Finde es um tolle Belohnungen zu erhalten!");
		
		scheduler = Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, new Runnable() {
			@Override
			public void run() {
				sys.sendAllMessage(CTE.prefix + "§7Das §eLootEi §7ist verschwunden!");
				startQueue();
				removeEgg(loc);
			}
		}, 20*60);//TODO 20*60*5
	}
	
	private void openEgg(Location loc) {
		sys.sendAllMessage(CTE.prefix + "§7Das §eLootEi §7wurde geöffnet!");
	}
	
	private void generatenewTime() {
		newTime = new Random().nextInt((3*10))+(2*10); //Zwischen 2-5 Minuten;
	}
	
	private void removeEgg(Location loc) {
		loc.getBlock().setType(Material.AIR);
	}
	
	private void placeEgg(Location loc) {
		Block b = loc.getBlock();
		b.setType(Material.SKULL);
		
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		profile.getProperties().put("textures", new Property("textures", skullTextures));
		
		TileEntitySkull skull = (TileEntitySkull)((CraftWorld)b.getWorld()).getHandle().getTileEntity(new BlockPosition(b.getX(), b.getY(), b.getZ()));
		skull.setGameProfile(profile);
		b.getState().update();
	}
	
	private void addLocations() {
		World w = Bukkit.getWorld("world");
		locs.add(new Location(w, 992, 100, 990));
		locs.add(new Location(w, 997, 100, 987));
		locs.add(new Location(w, 999, 100, 994));
		locs.add(new Location(w, 1007, 100, 990));
		locs.add(new Location(w, 1005, 100, 996));
		locs.add(new Location(w, 1007, 100, 1000));
		locs.add(new Location(w, 1013, 100, 998));
		locs.add(new Location(w, 1012, 100, 1002));
		locs.add(new Location(w, 1008, 100, 1009));
		locs.add(new Location(w, 1002, 100, 1008));
		locs.add(new Location(w, 994, 104, 1011));
		locs.add(new Location(w, 993, 102, 1005));
		locs.add(new Location(w, 988, 100, 1006));
		locs.add(new Location(w, 987, 100, 1001));
		locs.add(new Location(w, 991, 101, 1000));
		locs.add(new Location(w, 988, 100, 996));
		locs.add(new Location(w, 994, 100, 995));
		locs.add(new Location(w, 999, 102, 999));
		locs.add(new Location(w, 1003, 106, 988));
	}
	
}
