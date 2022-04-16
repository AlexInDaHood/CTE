package com.osterph.dev;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
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

        if (!p.isOp() && !new StaffManager(p).isHelper()) {
            p.sendMessage(CTE.prefix + "§cUnzureichende Berechtigungen.");
            return false;
        }

        if (!CTE.INSTANCE.getSystem().gamestate.equals(GAMESTATE.STARTING)) {
            p.sendMessage(CTE.prefix + "Game already started or ended");
            return false;
        }
        BaseComponent b = new TextComponent("");
        TextComponent txt = new TextComponent();
        txt.setText("§ehat die Runde gestartet.");

        StaffManager staff = new StaffManager(p);
        b.addExtra(staff.activeTag());
        b.addExtra(staff.activeString().replace("§l","").replace("✫","")+p.getName()+"§e ");
        b.addExtra(txt);
        CTE.INSTANCE.getSystem().sendAllMessage(b);
        CTE.INSTANCE.getSystem().forceStart();

        return false;
    }
    
}