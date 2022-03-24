package com.osterph.dev;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem.GAMESTATE;

public class startCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player p = (Player) sender;

        if (!p.isOp()) {
            p.sendMessage(CTE.prefix + "§cUnzureichende Berechtigungen.");
            return false;
        }

        if (!CTE.INSTANCE.getSystem().gamestate.equals(GAMESTATE.STARTING)) {
            p.sendMessage(CTE.prefix + "Game already started or ended");
            return false;
        }
        p.sendMessage(CTE.prefix + "Game started.");
        CTE.INSTANCE.getSystem().forceStart();

        return false;
    }
    
}