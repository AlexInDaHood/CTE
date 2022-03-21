package com.osterph.dev;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.osterph.cte.CTE;

public class animationCMD implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		p.teleport(new Location(p.getWorld(), 1000, 150, 1000, -90, 60));
		
		onSpawnAnimation(p, p.getLocation(), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
		return false;
	}
	
	private int scheduler;
	private int timer;
	
	private void onSpawnAnimation(Player p, Location loc, double a, double b, double c, double multi) {
		Chicken ch = (Chicken) loc.getWorld().spawnEntity(p.getLocation(), EntityType.CHICKEN);
		Vector vec = new Vector(a, b, c);
		ch.setPassenger(p);
		
		scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, new Runnable() {			
			@Override
			public void run() {
				timer++;
				if(timer%2 == 0)
					ch.setVelocity(vec);
				
				if((ch.getLocation().add(0, -1, 0).getBlock().getType() != Material.AIR && ch.getLocation().add(0, -1, 0).getBlock().getType() != Material.GRASS && ch.getLocation().add(0, -1, 0).getBlock().getType() != Material.YELLOW_FLOWER) || ch.getPassenger() == null) {
					Bukkit.getScheduler().cancelTask(scheduler);
					ch.remove();
					timer = 0;
					System.out.println("STOPPED"); ///animation 0.7 -0.8 0 0
				}
			}
		}, 0, 2);
		
	}
	
}
