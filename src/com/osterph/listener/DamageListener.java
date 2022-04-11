package com.osterph.listener;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.*;
import org.bukkit.entity.*;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import static org.bukkit.Material.AIR;
import static org.bukkit.Material.SPRUCE_FENCE;

public class DamageListener implements Listener {

    private final CTESystem sys = CTE.INSTANCE.getSystem();

    public HashMap<Player, String[]> combat = new HashMap<>(); //TARGET | DAMAGER


	@EventHandler
    public void onMove(PlayerMoveEvent e) {
    	if(!(e.getPlayer().getLocation().getY() < 30)) return;
    	if(!sys.gamestate.equals(GAMESTATE.RUNNING) && !sys.gamestate.equals(GAMESTATE.SUDDEN_DEATH)) return;
    	if(!e.getPlayer().getGameMode().equals(GameMode.SURVIVAL) && !e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) return;
    	Player p = e.getPlayer();
		onDeath(p);
    }
    
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!sys.gamestate.equals(GAMESTATE.RUNNING) && !sys.gamestate.equals(GAMESTATE.SUDDEN_DEATH)) {
            e.setCancelled(true);
            return;
        }

		if (e.getEntityType().equals(EntityType.RABBIT)) {
			Location loc = e.getEntity().getLocation();
			loc.add(0,0.05,0).getBlock().setType(AIR);
			loc.subtract(0,0.3,0).getBlock().setType(AIR);
			if (loc.subtract(0,0.6,0).getBlock().getType().equals(SPRUCE_FENCE)) {
				loc.subtract(0,0.6,0).getBlock().setType(AIR);
			}
		}

        Enum<EntityDamageEvent.DamageCause> cause = e.getCause();

        if (cause.equals(EntityDamageEvent.DamageCause.CONTACT)) {
            e.setCancelled(true);
        } else if (cause.equals(EntityDamageEvent.DamageCause.DROWNING)) {
			e.setCancelled(true);
		} else if (cause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
			int r = new Random().nextInt(4)+1;
			e.setDamage(r);
		}

		if(!(e.getEntity() instanceof Player)) return;
		if(cause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || cause.equals(EntityDamageEvent.DamageCause.PROJECTILE)) {return;}
		if(e.getDamage() >= ((Player) e.getEntity()).getHealth()) {
			e.setCancelled(true);
			onDeath((Player)e.getEntity());
		}
    }
    
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getEntityType().equals(EntityType.RABBIT)) e.setCancelled(true);
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
    		} else if(e.getDamager() instanceof TNTPrimed) {
				TNTPrimed pro = (TNTPrimed) e.getDamager();
				if(pro.getSource() instanceof Player)
					damager = (Player)pro.getSource();
			}

			if (e.getDamager().getType().equals(EntityType.PRIMED_TNT)||e.getDamager().getType().equals(EntityType.FIREBALL)) {
				int r = new Random().nextInt(4)+1;
				e.setDamage(r);
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
			int scheduler1 = Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, () -> combat.remove(t), 20 * 15);

    		if (damager!=null)combat.put(t, new String[] {damager.getName(),"" + scheduler1});

    		if(e.getDamage() >= t.getHealth()) {
				t.setHealth(20);
				e.setCancelled(true);
				onDeath(t);
			}
    	} else {
    		e.setCancelled(true);
    	}
    }

    private final HashMap<Player, Integer> deathScheduler = new HashMap<>();
    private final HashMap<Player, Integer> deathTimer = new HashMap<>();
    
    int scheduler;

	private void dropRessource(Player p) {
		for(int i=0;i<p.getInventory().getSize();i++) {
			if (p.getInventory().getItem(i) == null||p.getInventory().getItem(i).getType() == null) continue;
			ItemStack item = p.getInventory().getItem(i);
			if(item.getType().equals(Material.CARROT) || item.getType().equals(Material.MELON) || item.getType().equals(Material.APPLE)) {
				p.getLocation().getWorld().dropItem(p.getLocation(), p.getInventory().getItem(i));
			}
		}
	}

    public void onDeath(Player p) {
		p.closeInventory();
		dropRessource(p);
		CTE.INSTANCE.getStatsManager().addDeath(p);
		LocationLIST locs = CTE.INSTANCE.getLocations();
		if (sys.teams.get(p).equals(TEAM.SPEC) || sys.teams.get(p).equals(TEAM.DEFAULT)) {
			p.teleport(locs.specSPAWN());
			return;
		}
    	onEgg(p);
		TEAM taa = sys.teams.get(p);
		for(Player all : Bukkit.getOnlinePlayers()) {
			ScoreboardManager.refreshBoard(all);
		}
		p.setHealth(20);
    	p.teleport(locs.specSPAWN());
		p.playSound(p.getLocation(), Sound.CAT_PURREOW, 1, 0.5f);
		sys.clear(p);
		if((taa == TEAM.BLUE && sys.BLUE_EGG != CTESystem.EGG_STATE.GONE) || (taa == TEAM.RED && sys.RED_EGG != CTESystem.EGG_STATE.GONE)) {
			p.sendMessage(CTE.prefix + "Du wirst in §c5 Sekunden §ewiederbelebt.");
			deathTimer.put(p, 0);
			Chicken ch = (Chicken) p.getWorld().spawnEntity(p.getLocation(), EntityType.CHICKEN);
			Vector vec;
			if(taa.equals(TEAM.BLUE)) {
				vec = new Vector(0.7, -0.8, 0); 
			} else {
				vec = new Vector(-0.7, -0.8, 0);
			}
			ch.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999, 999, true));
			scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, () -> {
				//CHICKEN
				if(deathTimer.get(p)%2 == 0) {
					ch.setVelocity(vec);
				}
				ch.setPassenger(p);
				//RESPAWN
				if(deathTimer.get(p) == 5*10) {
					ch.remove();
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
					for(PotionEffect effect : p.getActivePotionEffects()) {
						p.removePotionEffect(effect.getType());
					}
					Bukkit.getScheduler().cancelTask(deathScheduler.get(p));
					deathScheduler.remove(p);
					deathTimer.remove(p);
					return;
				}
				sys.sendActionBar(p, "§7Respawn in §e" + (5-(deathTimer.get(p)/10)));

				deathTimer.put(p, deathTimer.get(p)+1);
			}, 0, 2);
			
			deathScheduler.put(p, scheduler);

			if(combat.containsKey(p)) {
				Player target = Bukkit.getPlayer(combat.get(p)[0]);
				String pl = sys.teams.get(p).equals(TEAM.RED) ? "§c" : sys.teams.get(p).equals(TEAM.BLUE) ? "§9" : "§7";
				combat.remove(p);
				if(target != null) {
					CTE.INSTANCE.getStatsManager().addKill(target);
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
		} else {

			if(combat.containsKey(p)) {
				Player target = Bukkit.getPlayer(combat.get(p)[0]);
				String pl = sys.teams.get(p).equals(TEAM.RED) ? "§c" : sys.teams.get(p).equals(TEAM.BLUE) ? "§9" : "§7";
				combat.remove(p);
				if(target != null) {
					CTE.INSTANCE.getStatsManager().addKill(target);
					String t = sys.teams.get(target).equals(TEAM.RED) ? "§c" : sys.teams.get(target).equals(TEAM.BLUE) ? "§9" : "§7";
					target.playSound(target.getLocation(), Sound.BAT_DEATH, 1f, 1.6f);
					sys.sendAllMessage(CTE.prefix + pl + p.getName() + "§e wurde von §c" + t + target.getName() + "§4 eliminiert§e!");
					sys.addPoints(target, 5, "ELIMINIERUNG");
				} else {
					sys.sendAllMessage(CTE.prefix + pl + p.getName() + "§e wurde §4eliminiert§e!");
				}
			} else {
				String pl = sys.teams.get(p).equals(TEAM.RED) ? "§c" : sys.teams.get(p).equals(TEAM.BLUE) ? "§9" : "§7";
				sys.sendAllMessage(CTE.prefix + pl + p.getName() + "§e ist §4eliminiert§e!");
			}
			p.sendMessage(CTE.prefix + "§cDu wurdest eliminiert!");

			sys.checkTeamSizes();
		}
		sys.teams.put(p, TEAM.SPEC);
		ScoreboardManager.refreshBoard();
		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999, 999, true));
    }
    
    public void onEgg(Player p) {
    	
    	TEAM t = sys.teams.get(p);
    	if(t == TEAM.BLUE) {
    		if(p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getItemMeta().getDisplayName() != null && p.getInventory().getHelmet().getItemMeta().getDisplayName().equals("§cRotes-Ei")) {
    			sys.RED_EGG = CTESystem.EGG_STATE.OKAY;
    			CTE.INSTANCE.getLocations().redEGG().getBlock().setType(Material.DRAGON_EGG);
    			sys.sendAllMessage(CTE.prefix + "Das §cRote-Ei §eist nun wieder sicher!");
    			Bukkit.getScheduler().cancelTask(EggListener.eggScheduler.get(p));
    			EggListener.eggScheduler.remove(p);
    			ScoreboardManager.refreshBoard();
    		}
    	} else if(t == TEAM.RED) {
    		if(p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getItemMeta().getDisplayName() != null && p.getInventory().getHelmet().getItemMeta().getDisplayName().equals("§9Blaues-Ei")) {
    			sys.BLUE_EGG = CTESystem.EGG_STATE.OKAY;
    			CTE.INSTANCE.getLocations().blueEGG().getBlock().setType(Material.DRAGON_EGG);
    			sys.sendAllMessage(CTE.prefix + "Das §9Blaue-Ei §eist nun wieder sicher!");
    			Bukkit.getScheduler().cancelTask(EggListener.eggScheduler.get(p));
    			EggListener.eggScheduler.remove(p);
    			ScoreboardManager.refreshBoard(); 
    		}
    	}
    }
}
