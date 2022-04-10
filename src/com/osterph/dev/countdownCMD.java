package com.osterph.dev;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
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
        
        if (!p.isOp() && !new StaffManager(p).isDev()) {
            p.sendMessage(CTE.prefix + "§cUnzureichende Berechtigungen.");
            return false;
        }
        
        if(Bukkit.getScheduler().isCurrentlyRunning(CTE.INSTANCE.getSystem().scheduler)) {p.sendMessage(CTE.prefix + "Countdown already started!");
            return false;}
        BaseComponent b = new TextComponent("");
        TextComponent txt = new TextComponent();
        txt.setText("§ehat den Countdown gestartet.");

        StaffManager staff = new StaffManager(p);
        b.addExtra(staff.activeTag());
        b.addExtra(staff.activeString().replace("§l","").replace("✫","")+p.getName()+"§e ");
        b.addExtra(txt);
        CTE.INSTANCE.getSystem().sendAllMessage(b);
        CTE.INSTANCE.getSystem().startTimer();

        return false;
    }
}
