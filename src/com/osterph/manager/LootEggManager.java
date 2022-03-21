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
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.cte.CTESystem.GAMESTATE;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.TileEntitySkull;


public class LootEggManager implements Listener {
	
	private ArrayList<Location> locs = new ArrayList<>();
	private CTESystem sys = CTE.INSTANCE.getSystem();
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
		openEgg(e.getBlock().getLocation());
		//TODO	
	}
	
	int spawnEgg;
	int timer;
	int scheduler;
	
	public void startQueue() {
		timer = 0;
		spawnEgg = (new Random().nextInt(3)+2)*60; //2-5 Minuten
		scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, new Runnable() {			
			@Override
			public void run() {
				timer++;
				if(timer == spawnEgg) {
					spawnEgg();
				}
			}
		}, 0, 20);
		
	}
	
	public void openEgg(Location loc) {
		timer = 0;
		Bukkit.getScheduler().cancelTask(scheduler);
		sys.sendAllMessage(CTE.prefix + "§7Das §eLootEi §7wurde geöffnet!");
		ArmorStand stand = (ArmorStand)loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		stand.setGravity(false);
		stand.setVisible(false);
		stand.getEquipment().setHelmet(ItemManager.customHead("LOL", ":D", skullTextures));
		
		scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, new Runnable() {
			@Override
			public void run() {
				timer++;
				if(timer%2 == 0) {
					stand.teleport(stand.getLocation().add(0, 0.1, 0));
				}
				stand.setHeadPose(new EulerAngle(0, stand.getHeadPose().getY()+0.1, 0));
				if(timer == 40) {
					Bukkit.getScheduler().cancelTask(scheduler);
					Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, new Runnable() {
						@Override
						public void run() {
							itemSpawnAnimation(loc, stand.getLocation());
						}
					}, 20);
				}
			}
		}, 0, 1);
	}
	
	private void itemSpawnAnimation(Location loc1, Location loc2) {
		int items = new Random().nextInt(5)+5;
		timer = 0;
		scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, new Runnable() {
			@Override
			public void run() {
				timer++;
				int x = new Random().nextInt(10)-5;
				int z = new Random().nextInt(10)-5;
				Location item = new Location(loc1.getWorld(), loc1.getX()+x, loc1.getY(), loc1.getZ()+z);
				drawLine(item, loc2, 0.5);
				item.getWorld().dropItem(item, new ItemStack(Material.CARROT));
				particle1(item);
				if(timer == items) {
					Bukkit.getScheduler().cancelTask(scheduler);
					startQueue();
				}
			}
		}, 0, 20);
	}
	
	
	public void drawLine(Location point1, Location point2, double space) {
		Location Loc1 = new Location(Bukkit.getWorld("world"), point1.getX(), point1.getY()+4.5, point1.getZ());
        Location Loc2 = new Location(Bukkit.getWorld("world"), point2.getX(), point2.getY(), point2.getZ());
        Vector vector = getDirectionBetweenLocations(Loc1, Loc2);
        for (double i = 1; i <= Loc1.distance(Loc2); i += 0.5) {
            vector.multiply(i);
            Loc1.add(vector);
			particle(Loc1, EnumParticle.FLAME);
            Loc1.subtract(vector);
            vector.normalize();
        }
	}
	
	public void spawnEgg() {
		Bukkit.getScheduler().cancelTask(scheduler);
		Location loc = locs.get(new Random().nextInt(locs.size()));
		Block b = loc.getBlock();
		b.setType(Material.SKULL);
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		profile.getProperties().put("textures", new Property("textures", skullTextures));
		((TileEntitySkull)((CraftWorld)b.getWorld()).getHandle().getTileEntity(new BlockPosition(b.getX(), b.getY(), b.getZ()))).setGameProfile(profile);;
		b.getState().update();
		sys.sendAllMessage(CTE.prefix + "Ein §6Lootegg §eist gespawnt! Finde es um tolle Belohnungen zu erhalten!");
		
		scheduler = Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, new Runnable() {
			@Override
			public void run() {
				sys.sendAllMessage(CTE.prefix + "Das §6Lootegg §eist verschwunden!");
				startQueue();
				loc.getBlock().setType(Material.AIR);
			}
		}, 20*60); //TODO 20*60*5
		
	}
	
	
	private Vector getDirectionBetweenLocations(Location Start, Location End) {
        Vector from = Start.toVector();
        Vector to = End.toVector();
        return to.subtract(from);
    }
	
	private void particle(Location loc, EnumParticle particle) {
		PacketPlayOutWorldParticles pa = new PacketPlayOutWorldParticles(particle, false,(float) loc.getX(),(float) loc.getY(),(float) loc.getZ(),(float) 0,(float) 0,(float) 0,(float) 0, 1, 0);
		for(Player p : Bukkit.getOnlinePlayers()) {
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(pa);
		}
	}
	
	private void particle1(Location loc) {
		PacketPlayOutWorldParticles pa = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, false,(float) loc.getX(),(float) loc.getY(),(float) loc.getZ(),(float) 0.1,(float) 0,(float) 0.1,(float) 2, 50, 5);
		for(Player p : Bukkit.getOnlinePlayers()) {
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(pa);
		}
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
