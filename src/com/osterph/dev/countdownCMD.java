package com.osterph.dev;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.osterph.cte.CTE;

public class countdownCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player p = (Player) sender;
        
        
        if (!p.isOp()) {
            p.sendMessage(CTE.prefix + "§cUnzureichende Berechtigungen.");
            return false;
        }
        
        if(Bukkit.getScheduler().isCurrentlyRunning(CTE.INSTANCE.getSystem().scheduler)) {p.sendMessage(CTE.prefix + "Countdown already started!");;return false;};
        p.sendMessage(CTE.prefix + "Countdown started.");
        CTE.INSTANCE.getSystem().startTimer();

        return false;
    }
}
