package com.osterph.lagerhalle;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class TablistManager {

    private static String header = "";
    private static String footer = "";

    public static void displayTablist(Player p) {
        sendTablist(p);
    }

    private static void sendTablist(Player p) {
        IChatBaseComponent tabHeader = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + header + "\"}");
        IChatBaseComponent tabFooter = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + footer + "\"}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(tabHeader);

        try {
            Field field = packet.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(packet, tabFooter);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        }

    }

}
