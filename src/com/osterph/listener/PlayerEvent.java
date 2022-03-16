package com.osterph.listener;

import com.google.gson.JsonObject;
import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.cte.CTESystem.GAMESTATE;
import com.osterph.cte.CTESystem.TEAM;
import com.osterph.dev.StaffManager;
import com.osterph.lagerhalle.LabyModProtocol;
import com.osterph.manager.ItemManager;
import com.osterph.manager.ScoreboardManager;
import com.osterph.manager.TablistManager;
import com.osterph.spawner.Spawner;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerEvent implements Listener {

    private CTESystem sys = CTE.INSTANCE.getSystem();
    
    @EventHandler
    public void onPickupItems(PlayerPickupItemEvent e) {
        if(sys.teams.get(e.getPlayer()).equals(TEAM.DEFAULT) || sys.teams.get(e.getPlayer()).equals(TEAM.SPEC)) {e.setCancelled(true);return;}
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
        if (Bukkit.getOnlinePlayers().size() == sys.maxPlayers && sys.gamestate.equals(GAMESTATE.STARTING)) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, CTE.prefix + "Die Runde ist voll!");
            return;
        }
        if (sys.gamestate.equals(GAMESTATE.ENDING)) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, CTE.prefix + "Die Runde ist vorbei!");
            return;
        }

        if (!e.getResult().equals(PlayerLoginEvent.Result.ALLOWED)) return;

        if (Bukkit.getOnlinePlayers().size() < sys.minPlayers) {
            if (Bukkit.getOnlinePlayers().size() == sys.maxPlayers && sys.c > 11) sys.c = 10;
            return;
        }

        sys.startTimer();
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        sendPlayingGamemode(p);
        sendServerBanner(p);
        TablistManager.displayTablist(p);
        p.setHealth(20);
        p.getActivePotionEffects().clear();
        sys.clear(p);
        sys.teams.put(p, CTESystem.TEAM.DEFAULT);

        if (sys.gamestate.equals(GAMESTATE.RUNNING)) {
            sys.teams.put(p, CTESystem.TEAM.SPEC);
            p.teleport(CTE.INSTANCE.getLocations().specSPAWN());
        } else {
            p.teleport(CTE.INSTANCE.getLocations().lobbySPAWN());
            p.getInventory().setItem(0, CTE.INSTANCE.getSelector().team);
        }

        if(sys.gamestate.equals(GAMESTATE.STARTING)) {
            if(new StaffManager(p).isDev()) {
                p.getInventory().setItem(8, new ItemManager(Material.REDSTONE_COMPARATOR).withName("§8» §bDev-Settings §8«").complete());
            }
            e.setJoinMessage("§8[§a+§8] §7" + p.getName());
            
            if(Bukkit.getOnlinePlayers().size() >= sys.minPlayers) {
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
            if (sys.gamestate.equals(GAMESTATE.RUNNING)) all.hidePlayer(p);
        }
    }

    @EventHandler
    void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (e.getPlayer().getInventory().getHelmet() != null && e.getPlayer().getInventory().getHelmet().getType().equals(Material.SKULL_ITEM)) {
            new DamageListener().onEgg(p);
        }
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

        if(sys.gamestate.equals(GAMESTATE.STARTING)) {
            e.setQuitMessage("§8[§c-§8] §7" + p.getName());
            System.out.println(Bukkit.getOnlinePlayers().size() + " : " + sys.minPlayers);
            if(Bukkit.getOnlinePlayers().size() < sys.minPlayers) {
            	sys.stopStartTimer();
            	sys.sendAllMessage(CTE.prefix + "Der Start wurde abgebrochen!");
            }
        } else if(sys.gamestate.equals(GAMESTATE.RUNNING)) {
            e.setQuitMessage("§8[§c-§8] §7" + p.getName());
            sys.teams.put(e.getPlayer(), TEAM.SPEC);
            sys.checkTeamSizes();
        } else {
            e.setQuitMessage(null);
        }
    }
    
    private void sendPlayingGamemode(Player p) {
    	JsonObject obj = new JsonObject();
    	obj.addProperty("show_gamemode", true);
    	obj.addProperty("gamemode_name", "§ePlayHills.eu §8» §6§lCapture the Egg");
    	LabyModProtocol.sendClientMessage(p, "server_gamemode", obj);
    }
    
    public void sendServerBanner(Player player) {
        JsonObject object = new JsonObject();
        object.addProperty("url", "https://cdn.discordapp.com/attachments/846451756816138250/952402124065607710/PBanner.png"); 
        LabyModProtocol.sendClientMessage(player, "server_banner", object);
    }
    
}
