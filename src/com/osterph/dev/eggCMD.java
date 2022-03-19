package com.osterph.dev;


import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.osterph.cte.CTE;
import com.osterph.manager.ItemManager;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.TileEntitySkull;


public class eggCMD implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		Player p = (Player)sender;
		
		//spawnEgg(p, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjE1NGJhNDJkMjgyMmY1NjUzNGU3NTU4Mjc4OTJlNDcxOWRlZWYzMjhhYmI1OTU4NGJlNjk2N2YyNWY0OGNiNCJ9fX0=");
		spawnAnimation(p.getLocation());
		return false;
	}

	private void spawnEgg(Player p, String textures) {
		Block b = p.getLocation().getBlock();
		b.setType(Material.SKULL);
		
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		profile.getProperties().put("textures", new Property("textures", textures));
		
		TileEntitySkull skull = (TileEntitySkull)((CraftWorld)b.getWorld()).getHandle().getTileEntity(new BlockPosition(b.getX(), b.getY(), b.getZ()));
		skull.setGameProfile(profile);
		b.getState().update();
	}
	
	int scheduler;
	int timer = 0;
	private void spawnAnimation(Location p) {
		ArmorStand as = (ArmorStand)p.getWorld().spawnEntity(p, EntityType.ARMOR_STAND);
		as.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999, 1, true));
		as.setGravity(false);
		as.getEquipment().setHelmet(ItemManager.customHead("LOL", ":D", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjE1NGJhNDJkMjgyMmY1NjUzNGU3NTU4Mjc4OTJlNDcxOWRlZWYzMjhhYmI1OTU4NGJlNjk2N2YyNWY0OGNiNCJ9fX0="));
		as.setVisible(false);
				
		scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, new Runnable() {			
			@Override
			public void run() {
				timer++;
				if(timer%2 == 0) {
					System.out.println("JZ " + timer);
					as.teleport(as.getLocation().add(0, 0.1, 0));
				}
				as.setHeadPose(new EulerAngle(0, as.getHeadPose().getY() + 0.1, 0));
				if(timer == 40) {
					Bukkit.getScheduler().cancelTask(scheduler);
					timer = 0;
					Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, new Runnable() {
						@Override
						public void run() {
							itemSpawnAnimation(p);
						}
					},20);
				}
			}
		}, 0, 1);
	}
	
	int spawnScheduler;
	int spawnTimer;
	
	private void itemSpawnAnimation(Location p) {
		int items = new Random().nextInt(5)+5;
		Player t = Bukkit.getPlayer("i4m2g00d4u");
		spawnScheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, new Runnable() {
			@Override
			public void run() {
				spawnTimer++;
				int x = new Random().nextInt(10)-5;
				int z = new Random().nextInt(10)-5;
				Location item = new Location(p.getWorld(), p.getX()+x, p.getY(), p.getZ()+z);
				drawLine(p, item, 0.5);
				item.getWorld().dropItem(item, new ItemStack(Material.CARROT));
				particle1(t, item);
				if(spawnTimer == items) {
					Bukkit.getScheduler().cancelTask(spawnScheduler);
					spawnTimer = 0;
				}
			}
		}, 0, 20);
		
	}
	
	public void drawLine(Location point1, Location point2, double space) {
		Location Loc1 = new Location(Bukkit.getWorld("world"), point1.getX(), point1.getY()+4.5, point1.getZ());
        Location Loc2 = new Location(Bukkit.getWorld("world"), point2.getX(), point2.getY(), point2.getZ());
        Vector vector = getDirectionBetweenLocations(Loc1, Loc2);
        Player t = Bukkit.getPlayer("i4m2g00d4u");
        for (double i = 1; i <= Loc1.distance(Loc2); i += 0.5) {
            vector.multiply(i);
            Loc1.add(vector);
            particle(t, Loc1);
            Loc1.subtract(vector);
            vector.normalize();
        }
	}
	
    private Vector getDirectionBetweenLocations(Location Start, Location End) {
        Vector from = Start.toVector();
        Vector to = End.toVector();
        return to.subtract(from);
    }
	
	
	private void particle(Player p, Location loc) {
		PacketPlayOutWorldParticles pa = new PacketPlayOutWorldParticles(EnumParticle.FLAME, false,(float) loc.getX(),(float) loc.getY(),(float) loc.getZ(),(float) 0,(float) 0,(float) 0,(float) 0, 1, 0);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(pa);
	}
	
	private void particle1(Player p, Location loc) {
		PacketPlayOutWorldParticles pa = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, false,(float) loc.getX(),(float) loc.getY(),(float) loc.getZ(),(float) 0.1,(float) 0,(float) 0.1,(float) 2, 50, 5);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(pa);
	}
	
}
