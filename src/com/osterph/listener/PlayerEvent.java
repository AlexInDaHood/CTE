package com.osterph.listener;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.cte.CTESystem.TEAM;
import com.osterph.lagerhalle.LocationLIST;
import com.osterph.lagerhalle.TeamSelector;
import com.osterph.manager.ScoreboardManager;
import com.osterph.spawner.Spawner;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerEvent implements Listener {

    private CTESystem sys = CTE.INSTANCE.getSystem();
    
    @EventHandler
    public void onPickupItems(PlayerPickupItemEvent e) {
        if (e.getItem().getItemStack().getItemMeta() == null || e.getItem().getItemStack().getItemMeta().getDisplayName() == null) return;
    	if (e.getItem().getItemStack().getItemMeta().getDisplayName().contains("[") && e.getItem().getItemStack().getItemMeta().getDisplayName().contains("]")) {
    		String name = e.getItem().getItemStack().getItemMeta().getDisplayName();
    		ItemMeta m = e.getItem().getItemStack().getItemMeta();
    		m.setDisplayName(m.getDisplayName().substring(0, m.getDisplayName().indexOf("[")));
    		e.getItem().getItemStack().setItemMeta(m);
    		Spawner sp = CTE.INSTANCE.getSpawnermanager().getSpawnerByName(name.substring(name.indexOf("[")+1, name.indexOf("]")));
    		sp.setCurrentitems(0);
    	}
    }
    
    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if (sys.currentPlayers == sys.maxPlayers && sys.isStarting) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, CTE.prefix + "Die Runde ist voll!");
            return;
        }
        if (sys.isEnding) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, CTE.prefix + "Die Runde ist vorbei!");
            return;
        }

        if (!e.getResult().equals(PlayerLoginEvent.Result.ALLOWED)) return;

        sys.currentPlayers++;
        if (sys.currentPlayers < sys.minPlayers) {
            if (sys.currentPlayers == sys.maxPlayers && sys.c > 11) sys.c = 10;
            return;
        }

        sys.startTimer();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.setHealth(20);
        p.getActivePotionEffects().clear();
        sys.clear(p);
        sys.teams.put(p, CTESystem.TEAM.DEFAULT);

        if (sys.isRunning) {
            sys.teams.put(p, CTESystem.TEAM.SPEC);
            p.teleport(new LocationLIST().specSPAWN());
        } else {
            p.teleport(new LocationLIST().lobbySPAWN());
            p.getInventory().setItem(0, new TeamSelector().team);
        }

        if(sys.isStarting) {
            e.setJoinMessage("§8[§a+§8] §7" + p.getName());
            
            if(sys.currentPlayers >= sys.minPlayers) {
            	sys.startTimer();
            }
            
        } else {
            e.setJoinMessage(null);
        }

        sys.punkte.put(p, 0);
        sys.kills.put(p, 0);

        for (Player all: Bukkit.getOnlinePlayers()) {
            ScoreboardManager.refreshBoard(all);
            all.setLevel(sys.c);
            if (sys.isRunning) all.hidePlayer(p);
        }
    }

    @EventHandler
    void onQuit(PlayerQuitEvent e) {
        sys.currentPlayers--;
        Player p = e.getPlayer();
        sys.teams.remove(p);
        sys.blue.remove(p);
        sys.red.remove(p);
        Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, new Runnable() {
            @Override
            public void run() {
                for (Player all: Bukkit.getOnlinePlayers()) {
                    ScoreboardManager.refreshBoard(all);
                }
            }
        },5);

        if(sys.isStarting) {
            e.setQuitMessage("§8[§c-§8] §7" + p.getName());
            System.out.println(sys.currentPlayers + " : " + sys.minPlayers);
            if(sys.currentPlayers < sys.minPlayers) {
            	sys.stopStartTimer();
            	sys.sendAllMessage(CTE.prefix + "Der Start wurde abgebrochen!");
            }
        } else if(sys.isRunning) {
            e.setQuitMessage("§8[§c-§8] §7" + p.getName());
            sys.teams.put(e.getPlayer(), TEAM.SPEC);
            sys.checkTeamSizes();
        } else {
            e.setQuitMessage(null);
        }
    }
}
