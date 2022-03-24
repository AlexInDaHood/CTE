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
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
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
import com.osterph.shop.Shop.Ressourcen;
import com.osterph.shop.Shop.SHOPTYPE;
import com.osterph.shop.ShopItem;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.TileEntitySkull;


public class LootEggManager implements Listener {
	
	private ArrayList<Location> locs = new ArrayList<>();
	private ArrayList<ItemStack> itemList = new ArrayList<>();
	private CTESystem sys = CTE.INSTANCE.getSystem();
	private String skullTextures = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjE1NGJhNDJkMjgyMmY1NjUzNGU3NTU4Mjc4OTJlNDcxOWRlZWYzMjhhYmI1OTU4NGJlNjk2N2YyNWY0OGNiNCJ9fX0=";
	
	public LootEggManager() {
		addLocations();
		addItems();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreak(BlockBreakEvent e) {
		if(!sys.gamestate.equals(GAMESTATE.RUNNING)) return;
		Block b = e.getBlock();
		if ((((CraftWorld)b.getWorld()).getHandle().getTileEntity(new BlockPosition(b.getX(), b.getY(), b.getZ())) == null) || !(((CraftWorld)b.getWorld()).getHandle().getTileEntity(new BlockPosition(b.getX(), b.getY(), b.getZ())) instanceof TileEntitySkull)) return;
		TileEntitySkull skull = (TileEntitySkull)((CraftWorld)b.getWorld()).getHandle().getTileEntity(new BlockPosition(b.getX(), b.getY(), b.getZ()));
		if(!skull.getGameProfile().getProperties().get("textures").iterator().next().getValue().equals(skullTextures)) return;
		Bukkit.getScheduler().cancelTask(scheduler);
		e.setCancelled(true);
		e.getBlock().setType(Material.AIR);
		CTE.INSTANCE.getLootEgg().openEgg(e.getBlock().getLocation());
	}
	
	
	int spawnEgg;
	int timer;
	int scheduler;
	
	public void startQueue() {
		timer = 0;
		spawnEgg = (new Random().nextInt(2)+5)*60; //2-5 Minuten
		scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, new Runnable() {			
			@Override
			public void run() {
				timer++;
				if(timer == spawnEgg) {
					Bukkit.getScheduler().cancelTask(scheduler);
					spawnEgg();
				}
			}
		}, 0, 20);
	}
	
	ArmorStand stand;
	
	public void openEgg(Location loc) {
		timer = 0;
		Bukkit.getScheduler().cancelTask(scheduler);
		sys.sendAllMessage(CTE.prefix + "§eDas §6LootEi §ewurde geöffnet!");
		stand = (ArmorStand)loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
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
					System.out.println("[LOOTEGG] [openEGG] STOPPED1: " + scheduler);
					Bukkit.getScheduler().cancelTask(scheduler);
					Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, new Runnable() {
						@Override
						public void run() {
							itemSpawnAnimation(loc, stand.getLocation().add(0, 1.5, 0));
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
				int x = new Random().nextInt(6)-3;
				int z = new Random().nextInt(6)-3;
				Location item = new Location(loc1.getWorld(), loc1.getX()+x, loc1.getY(), loc1.getZ()+z);
				drawLine(item, loc2, 0.5);
				sys.sendParticle(item.add(0, 1, 0), EnumParticle.REDSTONE, (float)0.1, 0, (float)0.1, (float)2, 50, 5);
				item.getWorld().dropItem(item, itemList.get(new Random().nextInt(itemList.size())));
				if(timer == items) {
					Bukkit.getScheduler().cancelTask(scheduler);
					despawnAnimation(loc2);
				}
			}
		}, 0, 20);
	}
	
	private void despawnAnimation(Location loc) {
		sys.sendParticle(loc.add(0, 1.3, 0), EnumParticle.ENCHANTMENT_TABLE, (float) 0.1, (float)0.1, (float) 0.1, 3, 1000, 5);
		scheduler = Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, new Runnable() {
			@Override
			public void run() {
				stand.remove();
				sys.sendParticle(loc, EnumParticle.EXPLOSION_NORMAL, (float)0.1, (float)0.1, (float)0.1, 0, 50, 0);
				startQueue();
			}
		}, 38);
		
	}
	
	
	public void drawLine(Location point1, Location point2, double space) {
		Location Loc1 = new Location(Bukkit.getWorld("world"), point1.getX(), point1.getY(), point1.getZ());
        Location Loc2 = new Location(Bukkit.getWorld("world"), point2.getX(), point2.getY(), point2.getZ());
        Vector vector = getDirectionBetweenLocations(Loc1, Loc2);
        for (double i = 1; i <= Loc1.distance(Loc2); i += 0.5) {
            vector.multiply(i);
            Loc1.add(vector);
            sys.sendParticle(Loc1.add(0, 1, 0), EnumParticle.FLAME, 0, 0, 0, 0, 1, 0);
            Loc1.add(0, -1, 0);
            Loc1.subtract(vector);
            vector.normalize();
        }
	}
	
	public void spawnEgg() {
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
				loc.getBlock().setType(Material.AIR);
				startQueue();
			}
		}, 20*60); //TODO 20*60*5
	}
	
	
	private Vector getDirectionBetweenLocations(Location Start, Location End) {
        Vector from = Start.toVector();
        Vector to = End.toVector();
        return to.subtract(from);
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
	
	private void addItems() {
		for(ShopItem item : CTE.INSTANCE.getShop().items) {
			if(item.getType().equals(SHOPTYPE.BLOCKS) || item.getType().equals(SHOPTYPE. ARMOR)) continue;
			itemList.add(item.getInventoryItem(0));
		}
		DropManager apple = new DropManager(Ressourcen.Apfel);
		DropManager melon = new DropManager(Ressourcen.Melone);
		DropManager carrot = new DropManager(Ressourcen.Karotte);
		
		itemList.add(apple.getItem(10));
		itemList.add(apple.getItem(16));
		itemList.add(apple.getItem(20));
		itemList.add(apple.getItem(32));
		

		itemList.add(melon.getItem(4));
		itemList.add(melon.getItem(7));
		itemList.add(melon.getItem(8));
		
		
		itemList.add(carrot.getItem(1));
		itemList.add(carrot.getItem(2));
		itemList.add(carrot.getItem(3));
	}
	
}
