package com.osterph.listener;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.cte.CTESystem.GAMESTATE;
import com.osterph.cte.CTESystem.TEAM;
import com.osterph.lagerhalle.LocationLIST;
import com.osterph.manager.ScoreboardManager;
import org.bukkit.inventory.ItemStack;

public class DamageListener implements Listener {

    private final CTESystem sys = CTE.INSTANCE.getSystem();

    public HashMap<Player, String[]> combat = new HashMap<>(); //TARGET | DAMAGER

	private int Scheduler;

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
    	if(!(e.getPlayer().getLocation().getY() < 30)) return;
    	if(!sys.gamestate.equals(GAMESTATE.RUNNING)) return;
    	if(!e.getPlayer().getGameMode().equals(GameMode.SURVIVAL) && !e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) return;
    	Player p = e.getPlayer();
		onDeath(p);
    }
    
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!sys.gamestate.equals(GAMESTATE.RUNNING)) {
            //e.setCancelled(true); //TODO
            return;
        }
        
        Enum<EntityDamageEvent.DamageCause> cause = e.getCause();

        if (cause.equals(EntityDamageEvent.DamageCause.CONTACT)) {
            e.setCancelled(true);
        } else if (cause.equals(EntityDamageEvent.DamageCause.DROWNING)) {
			e.setCancelled(true);
		}

		if(!(e.getEntity() instanceof Player)) return;
		if(cause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || cause.equals(EntityDamageEvent.DamageCause.PROJECTILE) || cause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) return;
		if(e.getDamage() >= ((Player) e.getEntity()).getHealth()) {
			e.setCancelled(true);
			onDeath((Player)e.getEntity());
		}
    }
    
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
    	if(!sys.gamestate.equals(GAMESTATE.RUNNING)) {
    		//e.setCancelled(true);
    		return;
    	}
    	if(e.getEntity() instanceof ArmorStand) {
    		//e.setCancelled(true); //TODO
    		return;
    	}    	
    	if(e.getEntity() instanceof Player && !sys.teams.get((Player)e.getEntity()).equals(TEAM.DEFAULT) && !sys.teams.get((Player)e.getEntity()).equals(TEAM.SPEC)) {
    		Player t = (Player) e.getEntity();
    		Player damager = null;
    		if(e.getDamager() instanceof Player) {
    			damager = (Player)e.getDamager();

				if(((Player) e.getDamager()).getItemInHand() != null) {
					Material hand = ((Player) e.getDamager()).getItemInHand().getType();
					if(hand.equals(Material.STONE_AXE) || hand.equals(Material.IRON_AXE) || hand.equals(Material.DIAMOND_AXE) || hand.equals(Material.STONE_PICKAXE) || hand.equals(Material.IRON_PICKAXE) || hand.equals(Material.DIAMOND_PICKAXE)) {
						e.setDamage(2);
					}
				}
    		} else if(e.getDamager() instanceof Projectile) {
    			Projectile pro = (Projectile) e.getDamager();
    			if(pro.getShooter() instanceof Player)
    				damager = (Player)pro.getShooter();
    		}
    		
    		if(damager != null && (sys.teams.get(damager).equals(TEAM.DEFAULT) || sys.teams.get(damager).equals(TEAM.SPEC))) {
    			e.setCancelled(true);
    			return;
    		}
    		
    		if(sys.teams.get(t).equals(sys.teams.get(damager))) {
    			e.setCancelled(true);
    			return;
    		}

			if(combat.containsKey(t)) {
				Bukkit.getScheduler().cancelTask(Integer.parseInt(combat.get(t)[1]));
			}
			Scheduler = Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, new Runnable() {
				@Override
				public void run() {
					combat.remove(t);
				}
			},20*15);

    		combat.put(t, new String[] {damager.getName(),"" + Scheduler});

    		if(e.getDamage() >= t.getHealth()) {
				t.setHealth(20);
				e.setCancelled(true);
				onDeath(t);
			}
    	} else {
    		e.setCancelled(true);
    		return;
    	}
    }

    private HashMap<Player, Integer> deathScheduler = new HashMap<>();
    private HashMap<Player, Integer> deathTimer = new HashMap<>();
    
    int scheduler;
    
    public void onDeath(Player p) {
		p.closeInventory();
		LocationLIST locs = CTE.INSTANCE.getLocations();
		if (sys.teams.get(p).equals(TEAM.SPEC) || sys.teams.get(p).equals(TEAM.DEFAULT)) {
			p.teleport(locs.specSPAWN());
			return;
		}
    	onEgg(p);
		if(combat.containsKey(p)) {
			Player target = Bukkit.getPlayer(combat.get(p)[0]);
			String pl = sys.teams.get(p).equals(TEAM.RED) ? "§c" : sys.teams.get(p).equals(TEAM.BLUE) ? "§9" : "§7";
			combat.remove(p);
			if(target != null) {
				sys.kills.put(target, sys.kills.get(target)+1);
				ScoreboardManager.refreshBoard(target);
				String t = sys.teams.get(target).equals(TEAM.RED) ? "§c" : sys.teams.get(target).equals(TEAM.BLUE) ? "§9" : "§7";
				target.playSound(target.getLocation(), Sound.BAT_DEATH, 1f, 1.6f);
				sys.sendAllMessage(CTE.prefix + pl + p.getName() + "§e wurde von §c" + t + target.getName() + "§e getötet!");
			} else {
				sys.sendAllMessage(CTE.prefix + pl + p.getName() + "§e ist gestorben!");
			}
		} else {
			String pl = sys.teams.get(p).equals(TEAM.RED) ? "§c" : sys.teams.get(p).equals(TEAM.BLUE) ? "§9" : "§7";
			sys.sendAllMessage(CTE.prefix + pl + p.getName() + "§e ist gestorben!");
		}
		TEAM taa = sys.teams.get(p);
		sys.teams.put(p, TEAM.SPEC);
		for(Player all : Bukkit.getOnlinePlayers()) {
			ScoreboardManager.refreshBoard(all);
		}
		p.setFlying(true);
		p.setHealth(20);
    	p.teleport(locs.specSPAWN());
		p.playSound(p.getLocation(), Sound.CAT_PURREOW, 1, 0.5f);
		sys.clear(p);
		if((taa == TEAM.BLUE && sys.BLUE_EGG != sys.BLUE_EGG.GONE) || (taa == TEAM.RED && sys.RED_EGG != sys.RED_EGG.GONE)) {
			p.sendMessage(CTE.prefix + "Du wirst in §c5 Sekunden §ewiederbelebt.");
	    	/*Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, new Runnable() {
				@Override
				public void run() {
					sys.teams.put(p, taa);
					if(sys.teams.get(p).equals(TEAM.BLUE)) {
						p.teleport(locs.blueSPAWN());
					} else if(sys.teams.get(p).equals(TEAM.RED)){
						p.teleport(locs.redSPAWN());
					}
					p.setGameMode(GameMode.SURVIVAL);
					p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
					sys.startEquip(p);
					p.setFlying(false);
					p.setHealth(20);
					p.sendMessage(CTE.prefix + "Du wurdest wiederbelebt.");
					for(Player all : Bukkit.getOnlinePlayers()) {
						ScoreboardManager.refreshBoard(all);
					}
				}
			},20*5L);*/
			deathTimer.put(p, 0);
			scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, new Runnable() {
				@Override
				public void run() {
					if(deathTimer.get(p) == 5) {
						sys.teams.put(p, taa);
						if(sys.teams.get(p).equals(TEAM.BLUE)) {
							p.teleport(locs.blueSPAWN());
						} else if(sys.teams.get(p).equals(TEAM.RED)){
							p.teleport(locs.redSPAWN());
						}
						p.setGameMode(GameMode.SURVIVAL);
						p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
						sys.startEquip(p);
						p.setFlying(false);
						p.setHealth(20);
						p.sendMessage(CTE.prefix + "Du wurdest wiederbelebt.");
						for(Player all : Bukkit.getOnlinePlayers()) {
							ScoreboardManager.refreshBoard(all);
						}
						Bukkit.getScheduler().cancelTask(deathScheduler.get(p));
						deathScheduler.remove(p);
						deathTimer.remove(p);
						return;
					}
					sys.sendActionBar(p, "§7Respawn in §e" + (5-deathTimer.get(p)));
					
					deathTimer.put(p, deathTimer.get(p)+1);
				}
			}, 0, 20);
			deathScheduler.put(p, scheduler);
		} else {
			p.sendMessage(CTE.prefix + "Du bist nun eliminiert!");
			sys.checkTeamSizes();
		}
    }
    
    public void onEgg(Player p) {
    	TEAM t = sys.teams.get(p);
    	if(t == TEAM.BLUE) {
    		if(p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getItemMeta().getDisplayName().equals("§cRotes-Ei")) {
    			sys.RED_EGG = CTESystem.EGG_STATE.OKAY;
    			CTE.INSTANCE.getLocations().redEGG().getBlock().setType(Material.DRAGON_EGG);
    			sys.sendAllMessage(CTE.prefix + "Das §cRote-Ei §eist nun wieder sicher!");
    			for(Player all : Bukkit.getOnlinePlayers()) {
    				ScoreboardManager.refreshBoard(all);
    			}
    		}
    	} else if(t == TEAM.RED) {
    		if(p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getItemMeta().getDisplayName().equals("§9Blaues-Ei")) {
    			sys.BLUE_EGG = CTESystem.EGG_STATE.OKAY;
    			CTE.INSTANCE.getLocations().blueEGG().getBlock().setType(Material.DRAGON_EGG);
    			sys.sendAllMessage(CTE.prefix + "Das §9Blaue-Ei §eist nun wieder sicher!");
    			for(Player all : Bukkit.getOnlinePlayers()) {
    				ScoreboardManager.refreshBoard(all);
    			}
    		}
    	}
    }
}
