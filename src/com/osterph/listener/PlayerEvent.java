package com.osterph.listener;

import com.osterph.lagerhalle.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        for (Player all: Bukkit.getOnlinePlayers()) ScoreboardManager.refreshBoard(all);
    }
}
