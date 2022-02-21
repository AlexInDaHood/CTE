package com.osterph.cte;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
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
    public boolean isStarted = false;
    public boolean isRunning = false;
    public boolean isEnding = false;


    public void countdown() {
        isStarted = true;
    }

    public void forceStart() {
        isStarted = false;
        isRunning = true;
    }

    public void endGame() {
        isRunning = false;
        isStarted = false;
        isEnding = true;
    }

    public void moveHub() {
        //TODO PLAYHILLS API - LOBBY-1
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
