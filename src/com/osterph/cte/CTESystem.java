package com.osterph.cte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.osterph.listener.EggListener;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.osterph.lagerhalle.LocationLIST;
import com.osterph.lagerhalle.NPCListener;
import com.osterph.listener.DamageListener;
import com.osterph.manager.ItemManager;
import com.osterph.manager.ScoreboardManager;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

import static org.bukkit.Material.AIR;

public class CTESystem {
	
    public HashMap<Player, TEAM> teams = new HashMap<>();
    public EGG_STATE BLUE_EGG = EGG_STATE.OKAY;
    public EGG_STATE RED_EGG = EGG_STATE.OKAY;
    
    public ArrayList<Player> red = new ArrayList<>();
    public int startRED = 0;
    public ArrayList<Player> blue = new ArrayList<>();
    public int startBLUE = 0;
    
    public HashMap<Player, ItemStack> chestplates = new HashMap<>();
    
    public TEAM winnerTeam = TEAM.DEFAULT;
    
    public int maxPlayers = 12;
    public int minPlayers = 4;
    
    
    public int c;
    public GAMESTATE gamestate = GAMESTATE.STARTING;


    public void clear(Player p) {
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
        p.setHealth(20);
        p.getInventory().clear();
    }

    public void checkTeamSizes() {
    	int blue = 0;
    	int red = 0;
    	for(Player p : teams.keySet()) {
    		if(teams.get(p).equals(TEAM.BLUE)) {
    			blue++;
    		} else if(teams.get(p).equals(TEAM.RED)) {
    			red++;
    		}
    	}
    	if(blue == 0) {
    		winnerTeam = TEAM.RED;
    		endGame();
    	} else if(red == 0) {
    		winnerTeam = TEAM.BLUE;
    		endGame();
    	}
    }

    public int teamData(Player p) {
        if (red.contains(p)) return 14;
        if (blue.contains(p)) return 11;

        return 8;
    }
    
    public void startEquip(Player p) {

        clear(p);
        ItemStack i = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta m = (LeatherArmorMeta) i.getItemMeta();
        if (teams.get(p).equals(TEAM.SPEC) || teams.get(p).equals(TEAM.DEFAULT)) return;
        if (teams.get(p).equals(TEAM.RED)) m.setColor(Color.RED);
        if (teams.get(p).equals(TEAM.BLUE)) m.setColor(Color.BLUE);

        p.getInventory().setHelmet(new ItemManager(Material.LEATHER_HELMET).withName("§7Lederhelm").unbreakable(true).withMeta(m).complete());
        p.getInventory().setLeggings(new ItemManager(Material.LEATHER_LEGGINGS).withName("§7Lederhose").unbreakable(true).withMeta(m).complete());
        p.getInventory().setBoots(new ItemManager(Material.LEATHER_BOOTS).withName("§7Lederstiefel").unbreakable(true).withMeta(m).complete());
        p.getInventory().setItem(0, new ItemManager(Material.WOOD_SWORD).withName("§7Holzschwert").unbreakable(true).complete());
        
        if(!chestplates.containsKey(p))  {
        	p.getInventory().setChestplate(new ItemManager(Material.LEATHER_CHESTPLATE).withName("§7Lederbrustpanzer").unbreakable(true).withMeta(m).complete());
        } else {
        	p.getInventory().setChestplate(chestplates.get(p));
        }
    }

    public void setHelmet(Player p) {
    	ItemStack i = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta m = (LeatherArmorMeta) i.getItemMeta();
        if (teams.get(p).equals(TEAM.SPEC) || teams.get(p).equals(TEAM.DEFAULT)) return;
        if (teams.get(p).equals(TEAM.RED)) m.setColor(Color.RED);
        if (teams.get(p).equals(TEAM.BLUE)) m.setColor(Color.BLUE);
        p.getInventory().setHelmet(new ItemManager(Material.LEATHER_HELMET).withName("§7Lederhelm").unbreakable(true).withMeta(m).complete());
    }

    private int suddenDeath;

    private void startSuddenDeathCounter() {
        suddenDeath = Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, this::suddenDeath, 20*60*20);
    }

    private void stopSuddenDeathCounter() {
        Bukkit.getScheduler().cancelTask(suddenDeath);
    }

    private boolean checkAllTeams() {
        if (red.size()+1 < blue.size()) {
            Player p = blue.get(new Random().nextInt(blue.size()));
            teams.put(p, TEAM.RED);
            red.add(p);
            blue.remove(p);
            return false;
        } else if (blue.size()+1 < red.size()) {
            Player p = red.get(new Random().nextInt(red.size()));
            teams.put(p, TEAM.BLUE);
            blue.add(p);
            red.remove(p);
            return false;
        } else {
            return true;
        }
    }

    public void forceStart() {
        startSuddenDeathCounter();
        if (gamestate.equals(GAMESTATE.RUNNING) || gamestate.equals(GAMESTATE.SUDDEN_DEATH)) return;
        startLoop();
        for (Player all: Bukkit.getOnlinePlayers()) {
            all.closeInventory();
            if (!teams.get(all).equals(TEAM.DEFAULT)) continue;
            if (red.size() < blue.size()) {
                teams.put(all, TEAM.RED);
                red.add(all);
            } else if (red.size() > blue.size()) {
                teams.put(all, TEAM.BLUE);
                blue.add(all);
            } else {
                boolean Bred = new Random().nextBoolean();
                if (Bred) {
                    teams.put(all, TEAM.RED);
                    red.add(all);
                } else {
                    teams.put(all, TEAM.BLUE);
                    blue.add(all);
                }
            }
        }
        while (!checkAllTeams()) {
            checkAllTeams();
        }

    	gamestate = GAMESTATE.RUNNING;
        stopStartTimer();
        RED_EGG = EGG_STATE.OKAY;
        BLUE_EGG = EGG_STATE.OKAY;
        startRED = red.size();
        startBLUE = blue.size();

        new LocationLIST().blueEGG().getBlock().setType(Material.DRAGON_EGG);
        new LocationLIST().redEGG().getBlock().setType(Material.DRAGON_EGG);

        for(Player all : Bukkit.getOnlinePlayers()) {
            ScoreboardManager.refreshBoard(all);
            NPCListener.show(all);
            startEquip(all);
            if (teams.get(all).equals(TEAM.RED)) all.teleport(new LocationLIST().redSPAWN());
            if (teams.get(all).equals(TEAM.BLUE)) all.teleport(new LocationLIST().blueSPAWN());
        }
        new LocationLIST().shopkeeperStand();
        CTE.INSTANCE.getSpawnermanager().aktivateSpawner();

        countdown = 900;
        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, () -> {
            switch (countdown) {
                case 300:
                    sendAllMessage(CTE.prefix + "Das Deathmatch startet in §c5 Minuten§e.");
                    break;
                case 180:
                    sendAllMessage(CTE.prefix + "Das Deathmatch startet in §c3 Minuten§e.");
                    break;
                case 60:
                case 30:
                case 10:
                case 9:
                case 8:
                case 7:
                case 6:
                case 5:
                case 4:
                case 3:
                case 2:
                    sendAllMessage(CTE.prefix + "Das Deathmatch startet in §c"+countdown+" Sekunden§e.");
                    break;
                case 1:
                    sendAllMessage(CTE.prefix + "Das Deathmatch startet in §c"+countdown+" Sekunde§e.");
                    break;
                case 0:
                    suddenDeath();
            }
            countdown--;
        },0,20L);
    }

	public void endGame() {
        stopSuddenDeathCounter();
        stopLoop();
        for (Entity all: Bukkit.getWorld("world").getEntities()) {
            if (!all.getType().equals(EntityType.DROPPED_ITEM)) continue;
            all.remove();
        }
        gamestate = GAMESTATE.ENDING;
        switch (winnerTeam) {
        	case SPEC:
            case DEFAULT: 
                sendAllMessage(CTE.prefix + "Die Runde §6§lCAPTURE THE EGG§e endete mit einem §7§lUnentschieden§e!");
                sendAllTitle("§7UNENTSCHIEDEN", "§eDas Spiel endete mit einem Unentschieden!");
                break;
            case RED: 
                sendAllMessage(CTE.prefix + "Das §c§lROTE Team§e hat die Runde §6§lCAPTURE THE EGG §egewonnen!");
                sendAllTitle("§4§k0§r §cROT §4§k0","§ehat das Spiel gewonnen!");
                break;
            case BLUE: 
                sendAllMessage(CTE.prefix + "Das §9§lBLAUE Team §ehat die Runde §6§lCAPTURE THE EGG §egewonnen!");
                sendAllTitle("§1§k0§r §9BLAU §1§k0","§ehat das Spiel gewonnen!");
                break;
        }
        for(Player all : Bukkit.getOnlinePlayers()) {
        	clear(all);
            all.teleport(CTE.INSTANCE.getLocations().lobbySPAWN());
            all.playSound(all.getLocation(), Sound.WITHER_DEATH, 1, 1);
            all.sendMessage(CTE.prefix + "§cDer Server startet in 15 Sekunden neu.");
        }
        for(Entity t : Bukkit.getWorld("world").getEntities()) {
        	if(!(t instanceof Item)) continue;
            t.remove();
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, () -> {
            moveHub();
            Bukkit.getServer().shutdown();
        }, 20*15L);

    }

    public void moveHub() {
        //TODO PLAYHILLS API - LOBBY-1
    }
    
    
    public void addPoints(Player p, int points) {
        String UUID = p.getUniqueId().toString();
        int punkte = (int) CTE.mysql.getDatabase("PLAYERPOINTS", "UUID" , UUID, "POINTS");
        punkte += points;
        CTE.INSTANCE.getStatsManager().addPoints(p, points);
        ScoreboardManager.refreshBoard(p);
        CTE.mysql.setDatabase("PLAYERPOINTS", "UUID", UUID, "POINTS", punkte);
    }

    public void sendAllMessage(String msg) {
        for(Player all : Bukkit.getOnlinePlayers()) {
            all.sendMessage(msg);
        }
    }

	@SuppressWarnings("deprecation")
	public void sendAllTitle(String title, String subtitle) {
        for(Player all : Bukkit.getOnlinePlayers()) {
            all.sendTitle(title, subtitle);
        }
    }

    public void sendAllSound(Sound sound, int vol, int pitch) {
        for(Player all : Bukkit.getOnlinePlayers()) {
            all.playSound(all.getLocation(), sound, vol ,pitch);
        }
    }
    
    public void sendParticle(Location loc, EnumParticle particle, float xz, float yz, float zz, float speed, int amount, int data) {
		PacketPlayOutWorldParticles pa = new PacketPlayOutWorldParticles(particle, false,(float) loc.getX(),(float) loc.getY() -1,(float) loc.getZ(), xz,yz , zz, speed, amount, data);
		for(Player p : Bukkit.getOnlinePlayers()) {
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(pa);
		}
	}

    public void sendActionBar(Player p, String msg) {
        IChatBaseComponent bar = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg + "\"}");
        PacketPlayOutChat chat = new PacketPlayOutChat(bar, (byte)2);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(chat);
    }

    
    public int scheduler;
    public int countdown = 61;
    
    public void startTimer() {
    	gamestate = GAMESTATE.STARTING;
        for (Entity all: Bukkit.getWorld("world").getEntities()) {
            if (!all.getType().equals(EntityType.DROPPED_ITEM)) continue;
            all.remove();
        }
    	if(!Bukkit.getScheduler().isCurrentlyRunning(scheduler)) {
	    	scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, () -> {

if (!gamestate.equals(GAMESTATE.STARTING)) {
Bukkit.getScheduler().cancelTask(scheduler);
countdown = 0;
return;
}
                for(Player all : Bukkit.getOnlinePlayers()) {
                    all.setLevel(countdown);
                }

                switch(countdown) {
                    case 60:
                        sendAllMessage(CTE.prefix + "Das Spiel beginnt in §c60 Sekunden§e.");
                        break;
                    case 45:
                        sendAllMessage(CTE.prefix + "Das Spiel beginnt in §c45 Sekunden§e.");
                        break;
                    case 30:
                        sendAllMessage(CTE.prefix + "Das Spiel beginnt in §c30 Sekunden§e.");
                        break;
                    case 20:
                        sendAllMessage(CTE.prefix + "Das Spiel beginnt in §c20 Sekunden§e.");
                        break;
                    case 10:
                        sendAllMessage(CTE.prefix + "Das Spiel beginnt in §c10 Sekunden§e.");
                        break;
                    case 3:
                        sendAllMessage(CTE.prefix + "Das Spiel beginnt in §c3 Sekunden§e.");
                        break;
                    case 2:
                        sendAllMessage(CTE.prefix + "Das Spiel beginnt in §c2 Sekunden§e.");
                        break;
                    case 1:
                        sendAllMessage(CTE.prefix + "Das Spiel beginnt in §c1 Sekunde§e.");
                        break;
                    case 0: forceStart();
                }

                countdown--;
            }, 0, 20L);
    	}
    	
    }
    
    public void stopStartTimer() {
    	Bukkit.getScheduler().cancelTask(scheduler);
    	countdown = 61;
    	for(Player all : Bukkit.getOnlinePlayers()) {
    		all.setLevel(0);
    	}
    }
    public void suddenDeath() {
        this.gamestate = GAMESTATE.SUDDEN_DEATH;
        for(Player all : Bukkit.getOnlinePlayers()) {
            all.playSound(all.getLocation(), Sound.WITHER_DEATH, 1, 1);
            all.sendTitle("§c§lSUDDEN DEATH","");
            all.sendMessage(CTE.prefix + "§c§lSUDDEN DEATH");
            all.sendMessage("§eAlle Eier wurden erobert! Du kannst nicht länger respawnen!");
            if(all.getEquipment().getHelmet().getType() != null && all.getEquipment().getHelmet().getType().equals(Material.SKULL_ITEM)) {
                setHelmet(all);
                Bukkit.getScheduler().cancelTask(EggListener.eggScheduler.get(all));
                EggListener.eggScheduler.remove(all);
            }
        }
        CTE.INSTANCE.getLocations().redEGG().getBlock().setType(Material.AIR);
        CTE.INSTANCE.getLocations().blueEGG().getBlock().setType(Material.AIR);
        RED_EGG = EGG_STATE.GONE;
        BLUE_EGG = EGG_STATE.GONE;
        ScoreboardManager.refreshBoard();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, () -> {
            if (!gamestate.equals(GAMESTATE.SUDDEN_DEATH)) return;
            for (int i = 0; i < 50; i++) {
                Location loc = new Location(Bukkit.getWorld("world"), 800.5, 200, 800.5);
                while (!isSpawnable(loc)) {
                    int xr = new Random().nextInt(208) + 900 - 4;
                    int zr = new Random().nextInt(118) + 945 - 4;

                    loc.setX(xr+.5);
                    loc.setZ(zr+.5);
                }

                loc.setY(180);

                Rabbit r = (Rabbit) Bukkit.getWorld("world").spawnEntity(loc, EntityType.RABBIT);
                r.setRabbitType(Rabbit.Type.BLACK_AND_WHITE);
            }
        },0,20L);

        countdown = 390;
        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, () -> {
            switch (countdown) {
                case 300:
                    sendAllMessage(CTE.prefix + "Das Spiel endet in §c5 Minuten§e.");
                    break;
                case 180:
                    sendAllMessage(CTE.prefix + "Das Spiel endet in §c3 Minuten§e.");
                    break;
                case 60:
                case 30:
                case 10:
                case 9:
                case 8:
                case 7:
                case 6:
                case 5:
                case 4:
                case 3:
                case 2:
                    sendAllMessage(CTE.prefix + "Das Spiel endet in §c"+countdown+" Sekunden§e.");
                    break;
                case 1:
                    sendAllMessage(CTE.prefix + "Das Spiel endet in §c"+countdown+" Sekunde§e.");
                    break;
                case 0:
                    endGame();
            }
            countdown--;
        },0,20L);
    }

    private boolean isSpawnable(Location loc) {
        for (int i = 150; i > 30; i--) {
            loc.setY(i);
            if (!loc.getBlock().getType().equals(AIR)) return true;
        }
        return false;
    }
    
    public enum EGG_STATE {
        OKAY, STOLEN, GONE
    }

    public enum TEAM {
	    BLUE, RED, SPEC, DEFAULT
	 }

	 public enum GAMESTATE {
    	STARTING, RUNNING, SUDDEN_DEATH, ENDING
     }

    public void stopLoop() {
        Bukkit.getScheduler().cancelTask(loop);
    }

    int loop;

    private final HashMap<Player, Double> dmg = new HashMap<>();

    public void startLoop() {
        loop = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, () -> {
            if (!gamestate.equals(GAMESTATE.RUNNING) && !gamestate.equals(GAMESTATE.SUDDEN_DEATH)) return;
            for(Player all : Bukkit.getOnlinePlayers()) {
                if(all.getGameMode().equals(GameMode.CREATIVE) || all.getGameMode().equals(GameMode.SPECTATOR)) continue;
                if(teams.get(all).equals(TEAM.SPEC) || teams.get(all).equals(TEAM.DEFAULT)) continue;
                if(all.getLocation().getY() < 113 && all.getLocation().getX() < 1100 && all.getLocation().getX() > 900 && all.getLocation().getZ() > 945 && all.getLocation().getZ() < 1055) {
                    dmg.remove(all);
                    continue;
                }
                sendActionBar(all, "§cDu hast dich zu weit von der Map entfernt!");
                if (!dmg.containsKey(all)) {
                    dmg.put(all, 0.3);
                } else {
                    dmg.put(all, dmg.get(all)+0.3);
                }
                if(all.getHealth() > dmg.get(all)) {
                    all.damage(dmg.get(all));
                } else {
                    new DamageListener().onDeath(all);
                }
            }
        }, 20, 20L);
    }
}
