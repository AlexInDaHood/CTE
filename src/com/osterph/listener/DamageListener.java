package com.osterph.listener;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.cte.CTESystem.TEAM;
import com.osterph.lagerhalle.LocationLIST;
import com.osterph.manager.ScoreboardManager;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Team;

public class DamageListener implements Listener {

    private final CTESystem sys = CTE.INSTANCE.getSystem();

    public HashMap<Player, String[]> combat = new HashMap<>(); //TARGET | DAMAGER


	private int Scheduler;

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
    	if(!(e.getPlayer().getLocation().getY() < 30)) return;
    	if(!sys.isRunning) return;
    	if(!e.getPlayer().getGameMode().equals(GameMode.SURVIVAL) && !e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) return;
    	Player p = e.getPlayer();
		onDeath(p);
    }
    
    
    
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!sys.isRunning) {
            e.setCancelled(true);
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
    	if(!sys.isRunning) {
    		e.setCancelled(true);
    		return;
    	}
    	if(e.getEntity() instanceof ArmorStand) {
    		e.setCancelled(true);
    		return;
    	}    	
    	if(e.getEntity() instanceof Player && !sys.teams.get((Player)e.getEntity()).equals(TEAM.DEFAULT) && !sys.teams.get((Player)e.getEntity()).equals(TEAM.SPEC)) {
    		Player t = (Player) e.getEntity();
    		Player damager = null;
    		if(e.getDamager() instanceof Player) {
    			damager = (Player)e.getDamager();
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
				onDeath(t);
			}
    		
    	} else {
    		e.setCancelled(true);
    		return;
    	}
    	
    }

    public void onDeath(Player p) {
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
	    	Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, new Runnable() {
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
			},20*5L);
		} else {
			p.sendMessage(CTE.prefix + "Du bist nun eliminiert!");
			sys.checkTeamSizes();
		}
    }
    
    private void onEgg(Player p) {
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
    
    /**
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!sys.isRunning) {
            e.setCancelled(true);
            return;
        }
        if (e.getEntity() instanceof ArmorStand) {
            e.setCancelled(true);
            return;
        }

        if (e.getEntity() instanceof Player && ((Player) e.getEntity()).getOpenInventory() != null) ((Player) e.getEntity()).closeInventory();

        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        
        if (sys.teams.get(p).equals(CTESystem.TEAM.SPEC) || sys.teams.get(p).equals(CTESystem.TEAM.DEFAULT)) {
            e.setCancelled(true);
            return;
        }

        if (e.getDamager() instanceof Projectile) {
            Projectile pro = (Projectile) e.getDamager();
            if (pro instanceof Fireball) return;
            Player Shooter = (Player) pro.getShooter();
            if (sys.teams.get(Shooter).equals(CTESystem.TEAM.SPEC) || sys.teams.get(Shooter).equals(CTESystem.TEAM.DEFAULT)) {
                e.setCancelled(true);
                return;
            }
            if (sys.teams.get(p).equals(sys.teams.get(Shooter))) {
                e.setCancelled(true);
            }
        } else if (e.getDamager() instanceof Player) {
            Player Damager = (Player) e.getDamager();
            if (sys.teams.get(Damager).equals(CTESystem.TEAM.SPEC) || sys.teams.get(Damager).equals(CTESystem.TEAM.DEFAULT)) {
                e.setCancelled(true);
                return;
            }
            if (sys.teams.get(p).equals(sys.teams.get(Damager))) {
                e.setCancelled(true);
            }
        }
    }
    **/
}
