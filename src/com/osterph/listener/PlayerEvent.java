package com.osterph.listener;

import com.google.gson.JsonObject;
import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.cte.CTESystem.GAMESTATE;
import com.osterph.cte.CTESystem.TEAM;
import com.osterph.dev.StaffManager;
import com.osterph.lagerhalle.LabyModProtocol;
import com.osterph.manager.ItemManager;
import com.osterph.manager.ScoreboardManager;
import com.osterph.manager.TablistManager;
import com.osterph.spawner.Spawner;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerEvent implements Listener {

    private final CTESystem sys = CTE.INSTANCE.getSystem();
    
    @EventHandler
    public void onPickupItems(PlayerPickupItemEvent e) {
        if(sys.teams.get(e.getPlayer()).equals(TEAM.DEFAULT) || sys.teams.get(e.getPlayer()).equals(TEAM.SPEC)) {e.setCancelled(true);return;}
        EggListener.checkHolzschwert(e.getPlayer());
        if (e.getItem().getItemStack().getItemMeta() == null || e.getItem().getItemStack().getItemMeta().getDisplayName() == null) return;
    	if (e.getItem().getItemStack().getItemMeta().getDisplayName().contains("[") && e.getItem().getItemStack().getItemMeta().getDisplayName().contains("]")) {
            String name = e.getItem().getItemStack().getItemMeta().getDisplayName();
            ItemMeta m = e.getItem().getItemStack().getItemMeta();
            m.setDisplayName(m.getDisplayName().substring(0, m.getDisplayName().indexOf("[")));
            e.getItem().getItemStack().setItemMeta(m);
            Spawner sp = CTE.INSTANCE.getSpawnermanager().getSpawnerByName(name.substring(name.indexOf("[") + 1, name.indexOf("]")));
            sp.setCurrentitems(0);
            if (!sp.isSplit()) return;
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (sys.teams.get(e.getPlayer()).equals(TEAM.DEFAULT) || sys.teams.get(e.getPlayer()).equals(TEAM.DEFAULT))
                    continue;
                if (all == e.getPlayer()) continue;
                if (all.getLocation().distance(e.getItem().getLocation()) > 1) continue;

                all.getInventory().addItem(e.getItem().getItemStack());
            }
        }
    }
    
    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if (Bukkit.getOnlinePlayers().size() == sys.maxPlayers && sys.gamestate.equals(GAMESTATE.STARTING)) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, CTE.prefix + "Die Runde ist voll!");
            return;
        }
        if (sys.gamestate.equals(GAMESTATE.ENDING)) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, CTE.prefix + "Die Runde ist vorbei!");
            return;
        }

        if (!e.getResult().equals(PlayerLoginEvent.Result.ALLOWED)) return;

        if (Bukkit.getOnlinePlayers().size() < sys.minPlayers) {
            if (Bukkit.getOnlinePlayers().size() == sys.maxPlayers && sys.c > 11) sys.c = 10;
        }

    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
    	e.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        sendPlayingGamemode();
        sendServerBanner(p);
        TablistManager.displayTablist(p);
        p.setHealth(20);
        p.getActivePotionEffects().clear();
        sys.clear(p);
        sys.teams.put(p, CTESystem.TEAM.DEFAULT);

        if (sys.gamestate.equals(GAMESTATE.RUNNING) || sys.gamestate.equals(GAMESTATE.SUDDEN_DEATH)) {
            sys.teams.put(p, CTESystem.TEAM.SPEC);
            p.teleport(CTE.INSTANCE.getLocations().specSPAWN());
        } else {
            p.teleport(CTE.INSTANCE.getLocations().lobbySPAWN());
            p.getInventory().setItem(0, CTE.INSTANCE.getSelector().team);
            
            ItemStack tutorial = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta meta = (BookMeta)tutorial.getItemMeta();
            meta.setDisplayName("§8» §3Spielanleitung §8«");
            meta.setAuthor("Osterhase");					//TODO
            meta.addPage("§8[§6§lCTE§8] \n§6CTE §7ist ein Teambasierter Spielmodus, indem es die Aufgabe deines Teams ist, das gegnerische Ei zu stehlen und zurück zu deiner Basis zu bringen. In deiner Basis angekommen übergibst du das Ei dann dem Shopkeeper.");
            meta.addPage("§7Nach der Abgabe des gegnerischen Ei`s ist es dem gegnerische Team nicht länger möglich zu respawnen. Das Team, welches als letztes am Leben ist, entscheidet die Runde damit für sich.");
            meta.addPage("§8[§6§lRessourcen§8]\n§7Während des gesamten Spiels werdet ihr zusätzlich mit Ressourcen wie §cÄpfel§7, §aMelonen§7 und §6Karotten §7ausgestattet, womit ihr euch zusätzliche Ausstattung kaufen könnt.");
            //meta.addPage("§8[§6§lLooteggs§8]\n§7Zusätzlich spawnen im Abstand von 2-5 Minuten §6Looteggs§7, die ihr dann in der Mitte aufsuchen könnt. Findet und öffnet ihr eins, versorgt euch das §6Lootegg §7mit zusätzlichen Ressourcen oder Items.");
            tutorial.setItemMeta(meta);
            p.getInventory().setItem(4, tutorial);
        }

        if(sys.gamestate.equals(GAMESTATE.STARTING)) {
            if(new StaffManager(p).isDev()) {
                p.getInventory().setItem(8, new ItemManager(Material.REDSTONE_COMPARATOR).withName("§8» §bDev-Settings §8«").complete());
            }
            e.setJoinMessage("§8[§a+§8] §7" + p.getName());

            if(Bukkit.getOnlinePlayers().size() >= sys.minPlayers && sys.countdown > 60) {
            	sys.startTimer();
            }
        } else {
            e.setJoinMessage(null);
        }

        for (Player all: Bukkit.getOnlinePlayers()) {
            ScoreboardManager.refreshBoard(all);
            all.setLevel(sys.c);
            if (sys.gamestate.equals(GAMESTATE.RUNNING) || sys.gamestate.equals(GAMESTATE.SUDDEN_DEATH)) all.hidePlayer(p);
        }
    }

    @EventHandler
    void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        TablistManager.removeFakePlayer(p.getName());
        if (e.getPlayer().getInventory().getHelmet() != null && e.getPlayer().getInventory().getHelmet().getType().equals(Material.SKULL_ITEM)) {
            new DamageListener().onEgg(p);
        }
        sys.teams.remove(p);
        sys.blue.remove(p);
        sys.red.remove(p);
        Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, () -> {
            for (Player all: Bukkit.getOnlinePlayers()) {
                ScoreboardManager.refreshBoard(all);
            }
        },5);

        if(sys.gamestate.equals(GAMESTATE.STARTING)) {
            e.setQuitMessage("§8[§c-§8] §7" + p.getName());
            Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, () -> {
                if(Bukkit.getOnlinePlayers().size() < sys.minPlayers) {
                    sys.stopStartTimer();
                    sys.sendAllMessage(CTE.prefix + "Der Start wurde abgebrochen!");
                }
            },5);
        } else if(sys.gamestate.equals(GAMESTATE.RUNNING) || sys.gamestate.equals(GAMESTATE.SUDDEN_DEATH)) {
            e.setQuitMessage("§8[§c-§8] §7" + p.getName());
            sys.teams.put(e.getPlayer(), TEAM.SPEC);
            sys.checkTeamSizes();
        } else {
            e.setQuitMessage(null);
        }
    }
    
    private void sendPlayingGamemode() {
    	JsonObject obj = new JsonObject();
    	obj.addProperty("show_gamemode", true);
    	obj.addProperty("gamemode_name", "§ePlayHills.eu §8» §6§lCapture the Egg");
    	//LabyModProtocol.sendClientMessage(p, "server_gamemode", obj); //TODO
    }
    
    private void sendServerBanner(Player player) {
        JsonObject object = new JsonObject();
        object.addProperty("url", "https://cdn.discordapp.com/attachments/846451756816138250/952402124065607710/PBanner.png"); 
        LabyModProtocol.sendClientMessage(player, "server_banner", object);
    }
}
