package com.osterph.lagerhalle;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.UUID;

import static com.mojang.authlib.minecraft.MinecraftProfileTexture.Type.SKIN;

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

    private void lol(Player p) {

        GameProfile g = new GameProfile(p.getUniqueId(), "");

        GameProfile f = new GameProfile(UUID.randomUUID(), "Kelp");
        f.getProperties().put("textures", new Property("textures", g.getProperties().get("textures")));

        WorldServer w = MinecraftServer.getServer().getWorldServer(0);
        PlayerInteractManager m = new PlayerInteractManager(w);
        EntityPlayer d = new EntityPlayer(MinecraftServer.getServer(), w, f, m);

    }
}
