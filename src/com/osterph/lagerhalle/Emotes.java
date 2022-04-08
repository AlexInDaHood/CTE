package com.osterph.lagerhalle;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.dev.StaffManager;
import com.osterph.manager.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Emotes implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;


        new MessageManager("§eEmote-Liste:").complete(p);
        listEmote(p, "UwU", "§dUwU");
        listEmote(p, "OwO", "§4OwO");
        listEmote(p, ":)", "§6:)");
        listEmote(p, ":(", "§9:(");
        if (!new StaffManager(p).isHelper()) return false;
        new MessageManager("§8["+new StaffManager(p).activeString()+"§8] §eEmote-Liste:").hidePrefix().complete(p);

        listEmote(p, "GG", "§6§kO§r §e§lGG§r §6§kO");
        listEmote(p, "xD", "§3xD");
        listEmote(p, "<3", "§c<3");
        listEmote(p, "POG", "§2§kO§r §aPOG§r §2§kO");
        listEmote(p, "pogchamp", "§2§kO§r §aPOGCHAMP§r §2§kO");

        return false;
    }

    private void listEmote(Player p, String from, String to) {
        CTESystem sys = CTE.INSTANCE.getSystem();
        String role = new StaffManager(p).hasRoles() ? "§8["+new StaffManager(p).activeString()+"§8] " : "";
        String team = "§8[§7SPEC§8] ";
        String color = "§7";
        switch (sys.teams.get(p)) {
            case RED:
                team = "§8[§cROT§8] ";
                color = "§c";
                break;
            case BLUE:
                team = "§8[§9BLAU§8] ";
                color = "§9";
                break;
            case DEFAULT:
                team = "§8[§7WAITING§8] ";
                color = "§7";
                break;
        }

        String pre = role + team + color + p.getName() + "§8» ";

        new MessageManager("§8- §f"+from+" §8» "+to).hidePrefix().setHoverTitle("§7Vorschau: §f"+from).setHover(pre+to).suggestCommand(from).complete(p);
    }
}
