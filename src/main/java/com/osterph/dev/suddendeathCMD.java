package com.osterph.dev;

import com.osterph.cte.CTE;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class suddendeathCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!(commandSender instanceof Player)) return false;

        Player p = (Player)commandSender;

        if(!p.isOp()) {
            p.sendMessage(CTE.prefix + "§cKeine Rechte und so :/");
            return false;
        }
        CTE.INSTANCE.getSystem().suddenDeath();

        return false;
    }
}
