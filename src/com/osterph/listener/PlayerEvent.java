package com.osterph.listener;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.inventory.Shop;
import com.osterph.lagerhalle.LocationLIST;
import com.osterph.lagerhalle.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvent implements Listener {

    private CTESystem sys = CTE.INSTANCE.system;

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

        sys.currentPlayers++;
        if (sys.currentPlayers < sys.minPlayers) {
            if (sys.currentPlayers == sys.maxPlayers && sys.c > 11) sys.c = 10;
            return;
        }
        if (sys.isStarting) return;

        sys.countdown();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        sys.teams.put(p, CTESystem.TEAM.DEFAULT);
        //TODO REMOVE
        if(p.getName().contains("FlauschigesAlex")) {
            sys.teams.put(p, CTESystem.TEAM.BLUE);
        }
        if(p.getName().contains("KartoffelPanzxr")) {
            sys.teams.put(p, CTESystem.TEAM.RED);
        }
        if(p.getName().contains("i4m2g00d4u")) {
            sys.teams.put(p, CTESystem.TEAM.RED);
        }

        if (sys.isStarting || sys.isEnding) {
            p.teleport(new LocationLIST().lobbySPAWN());
        } else if (sys.isRunning) {
            sys.teams.put(p, CTESystem.TEAM.SPEC);
            p.teleport(new LocationLIST().specSPAWN());
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
                for (Player all: Bukkit.getOnlinePlayers()) ScoreboardManager.refreshBoard(all);
            }
        },5);
        if (sys.currentPlayers >= sys.minPlayers) return;

        sys.cancel_countdown();
    }

    @EventHandler
    void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Shop.openShop(p, Shop.SHOPTYPE.CHOOSE);
        String msg = e.getMessage().replace("%","%%");
        CTESystem.TEAM team = sys.teams.get(p);
        if(sys.isRunning) {
            if(msg.toLowerCase().startsWith("@a")) {
                switch(team) {
                    case RED:
                        e.setFormat("§8[§cROT§8] §c" + p.getName() + " §8» §c" + msg);
                        break;
                    case BLUE:
                        e.setFormat("§8[§9BLAU§8] §9" + p.getName() + " §8» §9" + msg);
                        break;
                    case SPEC:
                        e.setCancelled(true);
                        for(Player all : Bukkit.getOnlinePlayers()) {
                            if(sys.teams.get(all) == CTESystem.TEAM.SPEC) {
                                all.sendMessage("§8[§7SPEC§8] §7" + p.getName() + " §8» §7" + msg);
                            }
                        }
                        break;
                }
            } else {
                switch(team) {
                    case RED:
                        e.setCancelled(true);
                        for(Player all : Bukkit.getOnlinePlayers()) {
                            if(sys.teams.get(all) == CTESystem.TEAM.RED) {
                                all.sendMessage("§c" + p.getName() + " §8» §c" + msg);
                            }
                        }
                        break;
                    case BLUE:
                        e.setCancelled(true);
                        for(Player all : Bukkit.getOnlinePlayers()) {
                            if(sys.teams.get(all) == CTESystem.TEAM.BLUE) {
                                all.sendMessage("§9" + p.getName() + " §8» §9" + msg);
                            }
                        }
                        break;
                    case SPEC:
                        e.setCancelled(true);
                        for(Player all : Bukkit.getOnlinePlayers()) {
                            if(sys.teams.get(all) == CTESystem.TEAM.SPEC) {
                                all.sendMessage("§8[§7SPEC§8] §7" + p.getName() + " §8» §7" + msg);
                            }
                        }
                        break;
                }
            }
        } else {
            switch(team) {
                case RED:
                    e.setFormat("§8[§cROT§8] §c" + p.getName() + " §8» §c" + msg);
                    break;
                case BLUE:
                    e.setFormat("§8[§9BLAU§8] §9" + p.getName() + " §8» §9" + msg);
                    break;
                case SPEC:
                    e.setFormat("§8[§7SPEC§8] §7" + p.getName() + " §8» §7" + msg);
                    break;
                case DEFAULT:
                    e.setFormat("§8[§7WAITING§8] §7" + p.getName() + " §8» §7" + msg);
                    break;
            }

        }
    }
}
