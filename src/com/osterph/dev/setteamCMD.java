package com.osterph.dev;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.manager.ScoreboardManager;

public class setteamCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player p = (Player) sender;

        if (!p.isOp()) {
            p.sendMessage(CTE.prefix + "§cUnzureichende Berechtigungen.");
            return false;
        }

        if (args.length != 1) {
            p.sendMessage(CTE.prefix + "§cTeam benötigt.");
            return false;
        }
        CTESystem sys = CTE.INSTANCE.getSystem();

        if (args[0].equalsIgnoreCase("default")) {
            sys.teams.put(p, CTESystem.TEAM.DEFAULT);
            p.sendMessage(CTE.prefix + "Team auf §7DEFAULT§e gesetzt.");
        } else if (args[0].equalsIgnoreCase("spec")) {
            sys.teams.put(p, CTESystem.TEAM.SPEC);
            p.sendMessage(CTE.prefix + "Team auf §7§oSPEC§e gesetzt.");
        } else if (args[0].equalsIgnoreCase("red")) {
            sys.teams.put(p, CTESystem.TEAM.RED);
            p.sendMessage(CTE.prefix + "Team auf §cROT§e gesetzt.");
        } else if (args[0].equalsIgnoreCase("blue")) {
            sys.teams.put(p, CTESystem.TEAM.BLUE);
            p.sendMessage(CTE.prefix + "Team auf §9BLAU§e gesetzt.");
        } else {
            p.sendMessage(CTE.prefix + "§cUngültiges Team.");
        }

        for (Player all: Bukkit.getOnlinePlayers()) {
            ScoreboardManager.refreshBoard(all);
        }

        return false;
    }
}
