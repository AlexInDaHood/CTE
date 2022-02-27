package com.osterph.listener;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.lagerhalle.LocationLIST;
import com.osterph.manager.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        p.setHealth(20);
        sys.clear(p);
        sys.teams.put(p, CTESystem.TEAM.DEFAULT);

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
                for (Player all: Bukkit.getOnlinePlayers()) {
                    ScoreboardManager.refreshBoard(all);
                }
            }
        },5);
        if (sys.currentPlayers >= sys.minPlayers) return;

        sys.cancel_countdown();
    }
}
