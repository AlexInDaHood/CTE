package com.osterph.cte;

import com.osterph.lagerhalle.LocationLIST;
import com.osterph.lagerhalle.ScoreboardManager;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class CTESystem {

    public HashMap<Player, TEAM> teams = new HashMap<>();
    public ArrayList<Player> winner = new ArrayList<>();
    public TEAM winnerTeam = null;
    private enum TEAM {
        BLUE, RED, SPEC, DEFAULT
    }

    public int maxPlayers = 12;
    public int minPlayers = 3;
    public int currentPlayers = 0;
    private int task;
    public int c;
    public boolean isStarting = false;
    public boolean isRunning = false;
    public boolean isEnding = false;


    public void countdown() {
        isStarting = true;
        c = 30;
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(CTE.INSTANCE, new Runnable() {
            @Override
            public void run() {
                for (Player all: Bukkit.getOnlinePlayers()) {
                    all.setLevel(c);
                }
                c--;
                if (c > 0) return;

                forceStart();
                for (Player all: Bukkit.getOnlinePlayers()) {
                    all.setLevel(0);
                }
            }
        }, 0, 20);
    }

    public void cancel_countdown() {
        isStarting = false;
        Bukkit.getScheduler().cancelTask(task);
        c = 0;
        for (Player all: Bukkit.getOnlinePlayers()) {
            all.setLevel(c);
            ScoreboardManager.refreshBoard(all);
        }
    }

    public void forceStart() {
        isStarting = false;
        isRunning = true;
        RED_EGG = EGG_STATE.OKAY;
        BLUE_EGG = EGG_STATE.OKAY;
        startRED = red.size();
        startBLUE = blue.size();

        new LocationLIST().blueEGG().getBlock().setType(Material.DRAGON_EGG);
        new LocationLIST().redEGG().getBlock().setType(Material.DRAGON_EGG);
    }

    public void endGame() {
        isRunning = false;
        isStarting = false;
        isEnding = true;

        switch (winnerTeam) {
            case DEFAULT: {
                sendAllMessage(CTE.prefix + "Die Runde §e§lCAPTURE THE EGG §eendete mit einem §7§lUnentschieden§e!");
                sendAllTitle("§7UNENTSCHIEDEN", "§eDas Spiel endete mit einem Unentschieden!");
                break;
            }
            case RED: {
                sendAllMessage(CTE.prefix + "Das §c§lRote-Team §ehat die Runde §e§lCAPTURE THE EGG §egewonnen!");
                sendAllTitle("§4§k0 §cROT §4§k0","§ehat das Spiel gewonnen!");
                break;
            }
            case BLUE: {
                sendAllMessage(CTE.prefix + "Das §c§lBlaue-Team §ehat die Runde §e§lCAPTURE THE EGG §egewonnen!");
                sendAllTitle("§1§k0 §9BLAU §1§k0","§ehat das Spiel gewonnen!");
                break;
            }
        }


    }

    public void moveHub() {
        //TODO PLAYHILLS API - LOBBY-1
    }

    public enum EGG_STATE {
        OKAY, STOLEN, GONE
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

}