package com.osterph.lagerhalle;

import static com.osterph.lagerhalle.NPC.spawnNPC;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.cte.CTESystem.TEAM;
import com.osterph.listener.EggListener;
import com.osterph.manager.ScoreboardManager;
import com.osterph.shop.Shop;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.WorldSettings;

public class NPCListener implements Listener {

    private final CTESystem sys = CTE.INSTANCE.getSystem();

    @EventHandler
    public void onWorld(PlayerChangedWorldEvent e) {NPC.show(e.getPlayer());}

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {NPC.show(e.getPlayer());}

    
    @EventHandler (priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractAtEntityEvent e) {

        if (e.getPlayer().getWorld().getName().equals("world")) e.setCancelled(true);

        if (e.getRightClicked().getName() == null|| !e.getRightClicked().getName().equals("[SHOPKEEPER]")) return;
        if (sys.teams.get(e.getPlayer()).equals(TEAM.SPEC) || sys.teams.get(e.getPlayer()).equals(TEAM.DEFAULT)) return;
        if(sys.gamestate.equals(CTESystem.GAMESTATE.SUDDEN_DEATH)) {e.getPlayer().sendMessage(CTE.prefix + "Der Shopkeeper traded nicht während des Sudden-Deaths!"); return;}
        if(e.getPlayer().getInventory().getHelmet() == null || e.getPlayer().getInventory().getHelmet().getType().equals(Material.LEATHER_HELMET)) {
        	CTE.INSTANCE.getShop().openShop(e.getPlayer(), Shop.SHOPTYPE.CHOOSE);
        } else if (e.getPlayer().getInventory().getHelmet().getType().equals(Material.SKULL_ITEM)){
        	Player p = e.getPlayer();
        	CTESystem system = CTE.INSTANCE.getSystem();
        	TEAM t = system.teams.get(p);
        	
        	if(t.equals(TEAM.RED) && p.getLocation().distance(CTE.INSTANCE.getLocations().redNPC()) <= 5) {
        		Bukkit.getScheduler().cancelTask(EggListener.eggScheduler.get(p));
    			EggListener.eggScheduler.remove(p);
        		system.BLUE_EGG = CTESystem.EGG_STATE.GONE;
        		system.sendAllMessage(CTE.prefix + "Das §9Blaue-Ei §ewurde erobert! Das §9Blaue-Team §ekann nicht länger respawnen!");
        		system.sendAllSound(Sound.ENDERDRAGON_GROWL, 1, 1);
        		system.setHelmet(p);
                sys.addPoints(p, 20, "EI EROBERUNG");
        		for(Player all : Bukkit.getOnlinePlayers()) {
        			ScoreboardManager.refreshBoard(all);
                    if(system.teams.get(all).equals(TEAM.RED) && !all.equals(p)) {
                        sys.addPoints(all, 5, "EI EROBERUNG");
                    }
        		}
        	} else if(t.equals(TEAM.BLUE) && p.getLocation().distance(CTE.INSTANCE.getLocations().blueNPC()) <= 5) {
        		Bukkit.getScheduler().cancelTask(EggListener.eggScheduler.get(p));
    			EggListener.eggScheduler.remove(p);
        		system.RED_EGG = CTESystem.EGG_STATE.GONE;
        		system.sendAllMessage(CTE.prefix + "Das §cRote-Ei §ewurde erobert! Das §cRote-Team §ekann nicht länger respawnen!");
        		system.sendAllSound(Sound.ENDERDRAGON_GROWL, 1, 1);
        		system.setHelmet(p);
                sys.addPoints(p, 20, "EI EROBERUNG");
        		for(Player all : Bukkit.getOnlinePlayers()) {
        			ScoreboardManager.refreshBoard(all);
                    if(system.teams.get(all).equals(TEAM.BLUE) && !all.equals(p)) {
                        sys.addPoints(all, 5, "EI EROBERUNG");
                    }
        		}
        	}
        	
        }

    }

    public static void spawnNPCs() {
        spawnNPC("§9Shopkeeper", "ewogICJ0aW1lc3RhbXAiIDogMTY0OTYyNDk5Mjg1MywKICAicHJvZmlsZUlkIiA6ICJjOWNjZmZhMGY4YWU0MzZmOWU5OWM3NWQ1ZmRiNDgxMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJPZmZpY2lhbGZrIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzY5NmExZGM2OTU1ZDMyMzhjYzU2YjcwYjY0OGIwMDFjNWE0YzljODliNWM0NzFhYWNiMDljYTFkYTRmNTZjNWMiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==", "gdzxhrJuHXwwyFQJgoaZpeYnayu6lW6JS9WwsylF3SunyHZMoqZV+UdECFnoL5alr0DcX9bI7z1Vyw2NTZvA8d6MwHM1ex4D2cnqMRTKhjWE3wTm3H2Oaf4SGTruKHO/Vku5V74qfJeZ3/JOtuBN8GOW0Xdr6kKpyM3PQaUHWIxrYM6a4Bqi72Fx2pHx6YjZBDkqYQUe7cuJ1re1xsoKLs4eA9SBlO7zmOBENnD9tosNGWq0SZsvxubErL1Bn4zWL7vgGnMdaCh7Tfg0b3I1HwVzo666eP6nW+S8tFE76pYuhMtINy0n7+75BZsMrD9proeN2/POWMT0uvIpG39BDlgh2Tc6/1Zd2XDUz6SVD9Zla74NIwgBMEPCfYRMYdcJe2s9QuMTPpaGxmhpsDIAECrbBui+xJVW9GzvkpMF64w9SzCvqMT9dCsl7Z8G/ffoHM5KrVIOCR1ujfrCepYrKmov8pDdD+RsopoN7A6CibY1/wXm3XXzkqiK91HaqBCVRXaQZWn6oCg+OYakfwgTCYnMMxQoHpBMr7iRZmdtxea/LFfL0yZ8PUhdWARNwvstr9jI92dr96B3f3ykkNXwn2mU8d6h/mhJE0s9irtR455id7MQZEZ/zKQUp1vg7XMu8cwe0ZQrivLgnORZMOHfEuHjc5XibCeSMVojSdYLqsE=", new LocationLIST().blueNPC());
        spawnNPC("§cShopkeeper", "ewogICJ0aW1lc3RhbXAiIDogMTY0OTYyNTAzNTU0NCwKICAicHJvZmlsZUlkIiA6ICI5NmMzNjVmMzU1OTk0M2M2YjVjY2EzOWNiZjU3ODE4NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJub3R6aUhEIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzlkNGM2NmI3ZWY4NWIwMTQ3NDM4MzJjNThmNTJkOTNlMTYyNmZjMzNiNjJhMjIxZDgzYmE3ODM2ZTQ3NmU3MjIiCiAgICB9CiAgfQp9" ,"v6WbP42BQ0VTirqPYHF+N17W/0R4TN2nbLdr1tKQ8+a1hgspajEt9YdhSNPplXak9lqlsKoOKPmUxzAToZ7FRYSuH8G4snEoPzal38vTQxCBMdEXdCUncbj0jQKyqh2ggiFy00r7KnQP2mvY7lDoWRpWH2dVjc5tIMbmMhOmz3FvKewAHWHv63OwN/Sl02b/0D7lISruPBr2Uwx3EU0SjytPIsNwEw49NdbItxF/Bd56lZ/MJ5U/WZYJVOStv5FRdCURDmPF03uIEhtn1tby78LtzffkVCxq6eMH43Yy1ibAb8OGk/iqf72fP9stUiuAKfxesH5lw2p/nFUlVZ5PV0QxecH8V+YK2mDJzNCFMh15nQXiiOmCpuPtUaAwghf4gDGCNAXdF5c6rNIhxYSK7ZH+DOvZGq02IWt1ui1NTqF23aTmACl9/cpzl11e2fiyOGqoQntlEjbEX7heO4JD4Yq0LBn4edttc6MFoFd6G0j0OIypo/2Iy4W4L8lOINvOK6r/LZiFoPG4rx19rcMlBAqmBa8cojnqyJK3eICaxHKhHyvFO7EtEXpIThhwcMS/yen4/oEKMQFPtSVM4MTEieSDqHP6E2CThDkLCNj8jte5wc4NK68O4PE/tcrbB8P0lDpV15mOx71hgnl4euj3fKccls3Eu9jWAfKF667EmuQ=", new LocationLIST().redNPC());
    }

    public static void show(Player p) {
        NPC.show(p);
    }
}

class NPC implements Serializable {

    private final int entityID;
    private Location location;
    private final GameProfile gameprofile;
    private Float health = 20F;
    private static final HashMap<NPC, String> npcHashMap = new HashMap<>();

    public static void spawnNPC(String Name, String value, String signature, Location loc) {
        NPC npc = new NPC(Name, loc);
        npc.setSkin(value, signature);

        npcHashMap.put(npc, loc.getWorld().getName());
    }

    public static void show(Player p){

        if (p.getWorld().getName().equals("world")) {

            for (Map.Entry<NPC, String> entry : npcHashMap.entrySet()) {
                NPC key = entry.getKey();
                String value = entry.getValue();

                if (value.equals("world")) {
                    key.spawn(p);
                    key.removeFromTablist();
                }
            }
        }
    }

    public NPC(String name, Location location) {
        entityID = (int) Math.ceil(Math.random() * 1000) + 2000;
        gameprofile = new GameProfile(UUID.randomUUID(), name);
        this.location = location.clone();
    }

    public NPC(String name, Integer entityID, UUID uuid, Location location) {
        this.entityID = entityID;
        gameprofile = new GameProfile(uuid, name);
        this.location = location.clone();
    }

    public int getEntityID() {
        return entityID;
    }

    public String getName() {
        return gameprofile.getName();
    }

    public Float getHealth() {
        return health;
    }

    public void setHealth(Float health) {
        this.health = health;
    }

    public Location getLocation() {
        return location;
    }

    public void setSkin(String value, String signatur) {
        gameprofile.getProperties().put("textures", new Property("textures", value, signatur));
    }

    public void animation(int animation) {
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
        setValue(packet, "a", entityID);
        setValue(packet, "b", (byte) animation);
        sendPacket(packet);
    }

    public void status(int status) {
        PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus();
        setValue(packet, "a", entityID);
        setValue(packet, "b", (byte) status);
        sendPacket(packet);
    }

    public void equip(Slot slot, ItemStack itemstack) {
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
        setValue(packet, "a", entityID);
        setValue(packet, "b", slot.getSlot());
        setValue(packet, "c", itemstack);
        sendPacket(packet);
    }

    public void sleep(boolean state) {
        if (state) {
            Location bedLocation = new Location(location.getWorld(), 1, 1, 1);
            PacketPlayOutBed packet = new PacketPlayOutBed();
            setValue(packet, "a", entityID);
            setValue(packet, "b", new BlockPosition(bedLocation.getX(), bedLocation.getY(), bedLocation.getZ()));

            sendPacket(packet);
            teleport(location.clone().add(0, 0.3, 0));
        } else {
            animation(2);
            teleport(location.clone().subtract(0, 0.3, 0));
        }
    }

    public void spawn() {
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
        setValue(packet, "a", entityID);
        setValue(packet, "b", gameprofile.getId());
        setValue(packet, "c", getFixLocation(location.getX()));
        setValue(packet, "d", getFixLocation(location.getY()));
        setValue(packet, "e", getFixLocation(location.getZ()));
        setValue(packet, "f", getFixRotation(location.getYaw()));
        setValue(packet, "g", getFixRotation(location.getPitch()));
        setValue(packet, "h", 0);
        DataWatcher w = new DataWatcher(null);
        w.a(6, health);
        addToTablist();
        w.a(10, (byte) 127);
        setValue(packet, "i", w);
        sendPacket(packet);
        headRotation(location.getYaw(), location.getPitch());
    }

    public void spawn(Player player) {
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
        setValue(packet, "a", entityID);
        setValue(packet, "b", gameprofile.getId());
        setValue(packet, "c", getFixLocation(location.getX()));
        setValue(packet, "d", getFixLocation(location.getY()));
        setValue(packet, "e", getFixLocation(location.getZ()));
        setValue(packet, "f", getFixRotation(location.getYaw()));
        setValue(packet, "g", getFixRotation(location.getPitch()));
        setValue(packet, "h", 0);
        DataWatcher w = new DataWatcher(null);
        w.a(6, health);
        addToTablist();
        w.a(10, (byte) 127);
        setValue(packet, "i", w);
        sendPacket(packet, player);
        headRotation(location.getYaw(), location.getPitch());
    }

    public void teleport(Location location) {
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
        setValue(packet, "a", entityID);
        setValue(packet, "b", getFixLocation(location.getX()));
        setValue(packet, "c", getFixLocation(location.getY()));
        setValue(packet, "d", getFixLocation(location.getZ()));
        setValue(packet, "e", getFixRotation(location.getYaw()));
        setValue(packet, "f", getFixRotation(location.getPitch()));

        sendPacket(packet);
        headRotation(location.getYaw(), location.getPitch());
        this.location = location.clone();
    }

    public void headRotation(float yaw, float pitch) {
        PacketPlayOutEntity.PacketPlayOutEntityLook packet = new PacketPlayOutEntity.PacketPlayOutEntityLook(entityID, getFixRotation(yaw), getFixRotation(pitch), true);
        PacketPlayOutEntityHeadRotation packetHead = new PacketPlayOutEntityHeadRotation();
        setValue(packetHead, "a", entityID);
        setValue(packetHead, "b", getFixRotation(yaw));

        sendPacket(packet);
        sendPacket(packetHead);

        this.location.setYaw(yaw);
        this.location.setPitch(pitch);
    }

    public void headRotation(Location location) {
        headRotation(location.getYaw(), location.getPitch());
    }

    public void destroy() {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{entityID});
        removeFromTablist();
        sendPacket(packet);
    }

    public void destroy(Player player) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{entityID});
        removeFromTablist();
        sendPacket(packet, player);
    }

    public void addToTablist() {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameprofile, 1, WorldSettings.EnumGamemode.NOT_SET, CraftChatMessage.fromString(gameprofile.getName())[0]);
        @SuppressWarnings("unchecked")
        List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
        players.add(data);

        setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        setValue(packet, "b", players);
        sendPacket(packet);
    }

    public void removeFromTablist() {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameprofile, 1, WorldSettings.EnumGamemode.NOT_SET, CraftChatMessage.fromString(gameprofile.getName())[0]);
        @SuppressWarnings("unchecked")
        List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
        players.add(data);
        setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        setValue(packet, "b", players);

        sendPacket(packet);
    }

    public void removeFromTablist(Player player) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameprofile, 1, WorldSettings.EnumGamemode.NOT_SET, CraftChatMessage.fromString(gameprofile.getName())[0]);
        @SuppressWarnings("unchecked")
        List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
        players.add(data);
        setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        setValue(packet, "b", players);

        sendPacket(packet, player);
    }

    private int getFixLocation(double pos) {
        return (int) MathHelper.floor(pos * 32.0D);
    }

    private byte getFixRotation(float yawpitch) {
        return (byte) ((int) (yawpitch * 256.0F / 360.0F));
    }

    private void setValue(Object obj, String name, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
        }
    }

    private Object getValue(Object obj, String name) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
        }
        return null;
    }

    private void sendPacket(Packet<?> packet, Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    private void sendPacket(Packet<?> packet) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendPacket(packet, player);
        }
    }

    private String getStringFromURL(String url) {
        String text = "";
        try {
            Scanner scanner = new Scanner(new URL(url).openStream());
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                while (line.startsWith(" ")) {
                    line = line.substring(1);
                }
                text = text + line;
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public HashMap<String, Object> encode() {
        HashMap<String, Object> map = new HashMap<>();

        // LOC
        map.put("X", location.getX());
        map.put("Y", location.getY());
        map.put("Z", location.getZ());
        map.put("Pitch", location.getPitch());
        map.put("Yaw", location.getYaw());
        map.put("World", location.getWorld().getName());

        // DATA
        map.put("name", gameprofile.getName());
        map.put("entityID", entityID);
        map.put("UUID", gameprofile.getId());
        map.put("health", health);

        // GAMEPROFILE
        String value = "";
        String signatur = "";
        for (Property property : gameprofile.getProperties().get("textures")) {
            value = property.getValue();
            signatur = property.getSignature();
        }
        map.put("value", value);
        map.put("signatur", signatur);

        return map;
    }

    public static NPC decode(HashMap<String, Object> map) {
        String name = (String) map.get("name");
        Integer entityID = (Integer) map.get("entityID");
        UUID uuid = (UUID) map.get("UUID");

        World world = Bukkit.getWorld((String) map.get("World"));
        Double x = (Double) map.get("X");
        Double y = (Double) map.get("Y");
        Double z = (Double) map.get("Z");
        Float pitch = (Float) map.get("Pitch");
        Float yaw = (Float) map.get("Yaw");
        Location location = new Location(world, x, y, z, yaw, pitch);

        NPC npc = new NPC(name, entityID, uuid, location);
        npc.health = (Float) map.get("health");
        String value = (String) map.get("value");
        String signatur = (String) map.get("signatur");
        npc.gameprofile.getProperties().put("textures", new Property("textures", value, signatur));
        return npc;
    }

    public String toString() {
        return encode().toString();
    }

    public enum Slot {
        HAND(0), HELMET(4), CHESTPLATE(3), LEGGINGS(2), BOOTS(1);

        private Integer slot;

        private Slot(Integer slot) {
            this.slot = slot;
        }

        public Integer getSlot() {
            return slot;
        }
    }
}

