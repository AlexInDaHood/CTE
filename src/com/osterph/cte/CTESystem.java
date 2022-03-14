package com.osterph.cte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.osterph.lagerhalle.LocationLIST;
import com.osterph.lagerhalle.NPCListener;
import com.osterph.manager.ItemManager;
import com.osterph.manager.ScoreboardManager;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class CTESystem {
	
    public HashMap<Player, TEAM> teams = new HashMap<>();
    public HashMap<Player, Integer> punkte = new HashMap<>();
    public HashMap<Player, Integer> kills = new HashMap<>();
    public EGG_STATE BLUE_EGG = EGG_STATE.OKAY;
    public EGG_STATE RED_EGG = EGG_STATE.OKAY;
    
    public ArrayList<Player> red = new ArrayList<>();
    public int startRED = 0;
    public ArrayList<Player> blue = new ArrayList<>();
    public int startBLUE = 0;
    
    
    public TEAM winnerTeam = TEAM.DEFAULT;
    
    public int maxPlayers = 12;
    public int minPlayers = 2;
    
    
    public int c;
    public GAMESTATE gamestate = GAMESTATE.STARTING;


    public void clear(Player p) {
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
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
    
    public void startEquip(Player p) {

        clear(p);
        ItemStack i = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta m = (LeatherArmorMeta) i.getItemMeta();
        if (teams.get(p).equals(TEAM.SPEC) || teams.get(p).equals(TEAM.DEFAULT)) return;
        if (teams.get(p).equals(TEAM.RED)) m.setColor(Color.RED);
        if (teams.get(p).equals(TEAM.BLUE)) m.setColor(Color.BLUE);

        p.getInventory().setHelmet(new ItemManager(Material.LEATHER_HELMET).withName("§7Lederhelm").unbreakable(true).withMeta(m).complete());
        p.getInventory().setChestplate(new ItemManager(Material.LEATHER_CHESTPLATE).withName("§7Lederbrustpanzer").unbreakable(true).withMeta(m).complete());
        p.getInventory().setLeggings(new ItemManager(Material.LEATHER_LEGGINGS).withName("§7Lederhose").unbreakable(true).withMeta(m).complete());
        p.getInventory().setBoots(new ItemManager(Material.LEATHER_BOOTS).withName("§7Lederstiefel").unbreakable(true).withMeta(m).complete());
        p.getInventory().setItem(0, new ItemManager(Material.WOOD_SWORD).withName("§7Holzschwert").unbreakable(true).complete());
    }

    public void setHelmet(Player p) {
    	ItemStack i = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta m = (LeatherArmorMeta) i.getItemMeta();
        if (teams.get(p).equals(TEAM.SPEC) || teams.get(p).equals(TEAM.DEFAULT)) return;
        if (teams.get(p).equals(TEAM.RED)) m.setColor(Color.RED);
        if (teams.get(p).equals(TEAM.BLUE)) m.setColor(Color.BLUE);
        p.getInventory().setHelmet(new ItemManager(Material.LEATHER_HELMET).withName("§7Lederhelm").unbreakable(true).withMeta(m).complete());
    }
    
    public void forceStart() {
        Bukkit.getWorld("world").getWorldBorder().setCenter(1000.5, 1000.5);
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

    	gamestate =GAMESTATE.RUNNING;
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
    }



    @SuppressWarnings("incomplete-switch")
	public void endGame() {

        Bukkit.getWorld("world").getWorldBorder().setCenter(0.5, 0.5);
        gamestate = GAMESTATE.ENDING;
        switch (winnerTeam) {
            case DEFAULT: {
                sendAllMessage(CTE.prefix + "Die Runde §6§lCAPTURE THE EGG §eendete mit einem §7§lUnentschieden§e!");
                sendAllTitle("§7UNENTSCHIEDEN", "§eDas Spiel endete mit einem Unentschieden!");
                break;
            }
            case RED: {
                sendAllMessage(CTE.prefix + "Das §c§lROTE Team §ehat die Runde §6§lCAPTURE THE EGG §egewonnen!");
                sendAllTitle("§4§k0§r §cROT §4§k0","§ehat das Spiel gewonnen!");
                break;
            }
            case BLUE: {
                sendAllMessage(CTE.prefix + "Das §9§lBLAUE Team §ehat die Runde §6§lCAPTURE THE EGG §egewonnen!");
                sendAllTitle("§1§k0§r §9BLAU §1§k0","§ehat das Spiel gewonnen!");
                break;
            }
        }
        for(Player all : Bukkit.getOnlinePlayers()) {
        	clear(all);
            all.teleport(CTE.INSTANCE.getLocations().lobbySPAWN());
            all.playSound(all.getLocation(), Sound.WITHER_DEATH, 1, 1);
            all.sendMessage(CTE.prefix + "Der Server startet in 15 Sekunden neu.");
        }
        for(Entity t : Bukkit.getWorld("world").getEntities()) {
        	if(t instanceof Item) {
        		t.remove();
        	}
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, new Runnable() {
            @Override
            public void run() {
                moveHub();
            }
        }, 20*15L);

    }

    public void moveHub() {
        //TODO PLAYHILLS API - LOBBY-1
    }
    
    public void addPoints(Player p, int points) {
        String UUID = p.getUniqueId().toString();
        int punkte = (int) CTE.mysql.getDatabase("PLAYERPOINTS", "UUID" , UUID, "POINTS");
        punkte += points;
        this.punkte.put(p, this.punkte.get(p)+points);

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

    public void sendActionBar(Player p, String msg) {
        IChatBaseComponent bar = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg + "\"}");
        PacketPlayOutChat chat = new PacketPlayOutChat(bar, (byte)2);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(chat);
    }

    
    private int scheduler;
    private int countdown = 60;
    
    public void startTimer() {
    	gamestate = GAMESTATE.STARTING;
    	if(!Bukkit.getScheduler().isCurrentlyRunning(scheduler)) {
	    	scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, new Runnable() {			
				@Override
				public void run() {

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
				}
			}, 0, 20L);
    	}
    	
    }
    
    public void stopStartTimer() {
    	Bukkit.getScheduler().cancelTask(scheduler);
    	countdown = 60;
    	for(Player all : Bukkit.getOnlinePlayers()) {
    		all.setLevel(0);
    	}
    }
    
    public enum EGG_STATE {
        OKAY, STOLEN, GONE
    }

    public enum TEAM {
	    BLUE, RED, SPEC, DEFAULT
	 }

	 public enum GAMESTATE {
    	STARTING, RUNNING, ENDING;
    }
}
