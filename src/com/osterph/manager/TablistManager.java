package com.osterph.manager;

import com.mojang.authlib.GameProfile;
import com.osterph.lagerhalle.NMSHelper;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

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

    public static void addFakePlayer(Player onlinePlayer) {

        GameProfile fakeProfile = new GameProfile(onlinePlayer.getUniqueId(), onlinePlayer.getName());

        GameProfile actualProfile = null;

        try{
            actualProfile = NMSHelper.getInstance().getGameProfile(onlinePlayer);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        fakeProfile.getProperties().clear();
        fakeProfile.getProperties().put("textures", NMSHelper.getInstance().getTexturesProperty(actualProfile));

        MinecraftServer server = MinecraftServer.getServer();
        WorldServer world = server.getWorldServer(0);
        PlayerInteractManager manager = new PlayerInteractManager(world);
        EntityPlayer player = new EntityPlayer(server, world, fakeProfile, manager);

        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, player);

        for(Player aOnline : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) aOnline).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @SuppressWarnings("deprecation")
	public static void removeFakePlayer(String Name) {
        GameProfile profile;
        if (Bukkit.getPlayer(Name) != null) {
            profile = new GameProfile(Bukkit.getPlayer(Name).getUniqueId(), Name);
        } else {
            profile = new GameProfile(Bukkit.getOfflinePlayer(Name).getUniqueId(), Name);
        }
        MinecraftServer server = MinecraftServer.getServer();
        WorldServer world = server.getWorldServer(0);
        PlayerInteractManager manager = new PlayerInteractManager(world);
        EntityPlayer player = new EntityPlayer(server, world, profile, manager);

        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, player);

        for(Player aOnline : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) aOnline).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
