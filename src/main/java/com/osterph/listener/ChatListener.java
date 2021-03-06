package com.osterph.listener;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.cte.CTESystem.GAMESTATE;
import com.osterph.dev.StaffManager;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ChatListener implements Listener {

    private final CTESystem sys = CTE.INSTANCE.getSystem();
    private final ArrayList<String> cooldown = new ArrayList<>();

    private final ArrayList<Player> antiLSpam = new ArrayList<>();
    
    
    @SuppressWarnings("incomplete-switch")
	@EventHandler
    void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage();
        String[] bmsg = msg.split(" ");
        msg = "";
        for (String s : bmsg) {
        	if(s.equalsIgnoreCase("l") || s.equalsIgnoreCase("ez") || s.equalsIgnoreCase("easy")) {
        		if(!antiLSpam.contains(p)) {
        		 msg += UwU(p, s) + " ";
        		 antiLSpam.add(p);
        		}
        	} else {
        		 msg += UwU(p, s) + " ";
        	}
        }
        antiLSpam.remove(p);
        CTESystem.TEAM team = sys.teams.get(p);
        StaffManager staff = new StaffManager(p);
        if (sys.gamestate.equals(GAMESTATE.RUNNING) || sys.gamestate.equals(GAMESTATE.SUDDEN_DEATH)) {
            if (msg.toLowerCase().startsWith("@a§f ") || msg.toLowerCase().startsWith("@all§f ") ) {
                String[] gmsg = msg.split(" ");
                msg = "";
                for (int i = 1; i < gmsg.length; i++) {
                    msg += gmsg[i] + " ";
                }
                if (msg.equals("")) return;
                e.setCancelled(true);
                BaseComponent b = new TextComponent("");
                TextComponent prefix = new TextComponent();
                TextComponent txt = new TextComponent();
                switch (team) {
                    case RED:
                        prefix.setText("§8[§5@all§8] ");
                        txt.setText("§8[§cROT§8] §c" + p.getName() + " §8» §f" + msg);
                        if (staff.hasRoles()) break;
                        cooldown.add(p.getName());
                        break;
                    case BLUE:
                        prefix.setText("§8[§5@all§8] ");
                        txt.setText("§8[§9BLAU§8] §9" + p.getName() + " §8» §f" + msg);
                        if (staff.hasRoles()) break;
                        cooldown.add(p.getName());
                        break;
                    case SPEC:
                    case DEFAULT:
                        e.setCancelled(true);
                        txt.setText("§8[§7SPEC§8] §7" + p.getName() + " §8» §f" + msg);
                        if (staff.hasRoles()) b.addExtra(staff.activeTag());
                        b.addExtra(txt);
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (sys.teams.get(all) == CTESystem.TEAM.SPEC) {
                                all.spigot().sendMessage(b);
                            }
                        }
                        return;
                }
                b.addExtra(prefix);
                if (staff.hasRoles()) b.addExtra(staff.activeTag());
                b.addExtra(txt);
                for (Player all: Bukkit.getOnlinePlayers()) {
                    all.spigot().sendMessage(b);
                }
                if (staff.hasRoles()) return;
                Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, () -> cooldown.remove(p.getName()), 20 * 30L);
            } else {
                e.setCancelled(true);
                BaseComponent b = new TextComponent("");
                TextComponent prefix = new TextComponent();
                TextComponent txt = new TextComponent();
                switch (team) {
                    case RED:
                        e.setCancelled(true);
                        prefix.setText("§8[§eTEAM§8] ");
                        txt.setText("§8[§cROT§8] §c" + p.getName() + " §8» §f" + msg);
                        b.addExtra(prefix);
                        if (staff.hasRoles()) b.addExtra(staff.activeTag());
                        b.addExtra(txt);

                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (!sys.teams.get(all).equals(CTESystem.TEAM.RED)) continue;
                            all.spigot().sendMessage(b);
                        }
                        return;
                    case BLUE:
                        e.setCancelled(true);
                        prefix.setText("§8[§eTEAM§8] ");
                        txt.setText("§8[§9BLAU§8] §9" + p.getName() + " §8» §f" + msg);
                        b.addExtra(prefix);

                        if (staff.hasRoles()) b.addExtra(staff.activeTag());
                        b.addExtra(txt);
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (!sys.teams.get(all).equals(CTESystem.TEAM.BLUE)) continue;
                            all.spigot().sendMessage(b);
                        }
                        return;
                    case SPEC:
                        e.setCancelled(true);
                        txt.setText("§8[§7SPEC§8] §7" + p.getName() + " §8» §f" + msg);

                        if (staff.hasRoles()) b.addExtra(staff.activeTag());
                        b.addExtra(txt);
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (!sys.teams.get(all).equals(CTESystem.TEAM.SPEC)) continue;
                            all.spigot().sendMessage(b);
                        }
                        return;
                }

            }
        } else {
            e.setCancelled(true);
            BaseComponent b = new TextComponent("");
            TextComponent txt = new TextComponent();
            switch (team) {
                case RED:
                    txt.setText("§8[§cROT§8] §c" + p.getName() + " §8» §f" + msg);
                    break;
                case BLUE:
                    txt.setText("§8[§9BLAU§8] §9" + p.getName() + " §8» §f" + msg);
                    break;
                case SPEC:
                    txt.setText("§8[§7SPEC§8] §7" + p.getName() +  " §8» §f" + msg);
                    break;
                case DEFAULT:
                    txt.setText("§8[§7WAITING§8] §7" + p.getName() +" §8» §f" + msg);
                    break;
            }
            if (staff.hasRoles()) {
                b.addExtra(staff.activeTag());
            }

            b.addExtra(txt);

            for (Player all: Bukkit.getOnlinePlayers()) {
                all.spigot().sendMessage(b);
            }
        }
        System.out.println(p.getName() + " > " + msg);
    }

    private String UwU(Player p, String msg) {

        HashMap<String, String> emotes = new HashMap<>();
        emotes.put("ez", "Frohe Ostern!");
        emotes.put("easy", "Frohe Ostern!");
        int r = new Random().nextInt(6)+1;
        switch (r) {
            case 1:
                emotes.put("l", "#ostern");
                break;
            case 2:
                emotes.put("l", "Alles Gute, nur das Beste, gerade jetzt zum Osterfeste!");
                break;
            case 3:
                emotes.put("l", "Jedes Jahr zur Osterfeier klaut der Has dem Huhn die Eier, woraufhin er sie versteckt. So kommts, dass wir in jedem Jahr die Eier suchen ist doch klar!");
                break;
            case 4:
                emotes.put("l", "Endlich ist es soweit, willkommen in der Osterzeit!");
                break;
            case 5:
                emotes.put("l", "Legt der Has Eier ins Nest, dann feiern wir das Osterfest!");
                break;
            case 6:
                emotes.put("l", "Was hoppelt da im grünen Gras, mein Kind es ist der Osterhas!");
                break;
        }

        emotes.put("uwu", "§dUwU");
        emotes.put("owo", "§4OwO");
        emotes.put(":)", "§6:)");
        emotes.put(";)", "§6;)");
        emotes.put(":(", "§9:(");
        emotes.put(";(", "§9;(");
        if (new StaffManager(p).isHelper()) {
            emotes.put("gg", "§6§kO§r §e§lGG§r §6§kO");
            emotes.put("xd", "§3xD");
            emotes.put("<3", "§c<3");
            emotes.put("pog", "§2§kO§r §aPOG§r §2§kO");
            emotes.put("pogchamp", "§2§kO§r §aPOGCHAMP§r §2§kO");
        }

        if (emotes.containsKey(msg.toLowerCase())) msg = emotes.get(msg.toLowerCase());
        
        return msg+"§f";
    }
}
