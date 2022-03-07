package com.osterph.dev;

import com.osterph.cte.CTE;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class countdownCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player p = (Player) sender;

        if (!p.isOp()) {
            p.sendMessage(CTE.prefix + "§cUnzureichende Berechtigungen.");
            return false;
        }

        p.sendMessage(CTE.prefix + "Countdown started.");
        CTE.INSTANCE.system.countdown();

        return false;
    }
}
