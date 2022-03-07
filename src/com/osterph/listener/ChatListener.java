package com.osterph.listener;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.dev.StaffManager;
import com.osterph.inventory.Shop;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

public class ChatListener implements Listener {

    private CTESystem sys = CTE.INSTANCE.system;
    private ArrayList<String> cooldown = new ArrayList<>();

    @SuppressWarnings("incomplete-switch")
	@EventHandler
    void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Shop.openShop(p, Shop.SHOPTYPE.WEAPON);
        String msg = e.getMessage();
        msg = UwU(msg);
        CTESystem.TEAM team = sys.teams.get(p);
        if (sys.isRunning) {
            if (msg.toLowerCase().startsWith("@a ") || msg.toLowerCase().startsWith("@all ")) {
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
                        if (new StaffManager(p).hasRoles()) break;
                        cooldown.add(p.getName());
                        break;
                    case BLUE:
                        prefix.setText("§8[§5@all§8] ");
                        txt.setText("§8[§9BLAU§8] §9" + p.getName() + " §8» §f" + msg);
                        if (new StaffManager(p).hasRoles()) break;
                        cooldown.add(p.getName());
                        break;
                    case SPEC:
                        e.setCancelled(true);
                        txt.setText("§8[§7SPEC§8] §7" + p.getName() + " §8» §7" + msg);
                        if (new StaffManager(p).hasRoles()) b.addExtra(new StaffManager(p).activeTag());
                        b.addExtra(txt);
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (sys.teams.get(all) == CTESystem.TEAM.SPEC) {
                                all.spigot().sendMessage(b);
                            }
                        }
                        return;
                }
                b.addExtra(prefix);
                if (new StaffManager(p).hasRoles()) b.addExtra(new StaffManager(p).activeTag());
                b.addExtra(txt);
                for (Player all: Bukkit.getOnlinePlayers()) {
                    all.spigot().sendMessage(b);
                }
                if (new StaffManager(p).hasRoles()) return;
                Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, new Runnable() {
                    @Override
                    public void run() {
                        cooldown.remove(p.getName());
                    }
                }, 20 * 30L);
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
                        if (new StaffManager(p).hasRoles()) b.addExtra(new StaffManager(p).activeTag());
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

                        if (new StaffManager(p).hasRoles()) b.addExtra(new StaffManager(p).activeTag());
                        b.addExtra(txt);
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (!sys.teams.get(all).equals(CTESystem.TEAM.BLUE)) continue;
                            all.spigot().sendMessage(b);
                        }
                        return;
                    case SPEC:
                        e.setCancelled(true);
                        txt.setText("§8[§7SPEC§8] §7" + p.getName() + " §8» §7" + msg);

                        if (new StaffManager(p).hasRoles()) b.addExtra(new StaffManager(p).activeTag());
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
                    txt.setText("§8[§7SPEC§8] §7" + p.getName() +  " §8» §7" + msg);
                    break;
                case DEFAULT:
                    txt.setText("§8[§7WAITING§8] §7" + p.getName() +" §8» §7" + msg);
                    break;
            }
            if (new StaffManager(p).hasRoles()) {
                b.addExtra(new StaffManager(p).activeTag());
            }

            b.addExtra(txt);

            for (Player all: Bukkit.getOnlinePlayers()) {
                all.spigot().sendMessage(b);
            }
        }
        System.out.println(p.getName() + " > " + msg);
    }

    private String UwU(String msg) {

        msg = msg.replace("UwU","§dUwU§f");
        msg = msg.replace("OwO","§5OwO§f");
        msg = msg.replace(":3","§c:3§f");

        return msg;
    }
}
