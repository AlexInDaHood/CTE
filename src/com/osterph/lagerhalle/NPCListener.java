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

    private CTESystem sys = CTE.INSTANCE.getSystem();

    @EventHandler
    public void onWorld(PlayerChangedWorldEvent e) {NPC.show(e.getPlayer());}

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {NPC.show(e.getPlayer());}

    
    @EventHandler (priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractAtEntityEvent e) {

        if (e.getPlayer().getWorld().getName().equals("world")) e.setCancelled(true);

        if (e.getRightClicked().getName() == null|| !e.getRightClicked().getName().equals("[SHOPKEEPER]")) return;
        if (sys.teams.get(e.getPlayer()).equals(TEAM.SPEC) || sys.teams.get(e.getPlayer()).equals(TEAM.DEFAULT)) return;
        
        if(e.getPlayer().getInventory().getHelmet() == null || e.getPlayer().getInventory().getHelmet().getType().equals(Material.LEATHER_HELMET)) {
        	Shop.openShop(e.getPlayer(), Shop.SHOPTYPE.CHOOSE);
        } else if (e.getPlayer().getInventory().getHelmet().getType().equals(Material.SKULL_ITEM)){
        	Player p = e.getPlayer();
        	CTESystem system = CTE.INSTANCE.getSystem();
        	TEAM t = system.teams.get(p);
        	
        	if(t.equals(TEAM.RED)) {
        		system.BLUE_EGG = system.BLUE_EGG.GONE;
        		system.sendAllMessage(CTE.prefix + "Das §9Blaue-Ei §ewurde erobert! Das §9Blaue-Team §ekann nicht länger respawnen!");
        		system.sendAllSound(Sound.ENDERDRAGON_GROWL, 1, 1);
        		system.setHelmet(p);
        		for(Player all : Bukkit.getOnlinePlayers()) {
        			ScoreboardManager.refreshBoard(all);
        		}
        	} else if(t.equals(TEAM.BLUE)) {
        		system.RED_EGG = system.RED_EGG.GONE;
        		system.sendAllMessage(CTE.prefix + "Das §cRote-Ei §ewurde erobert! Das §cRote-Team §ekann nicht länger respawnen!");
        		system.sendAllSound(Sound.ENDERDRAGON_GROWL, 1, 1);
        		system.setHelmet(p);
        		for(Player all : Bukkit.getOnlinePlayers()) {
        			ScoreboardManager.refreshBoard(all);
        		}
        	}
        	
        }

    }

    public static void spawnNPCs() {
        spawnNPC("§9Shopkeeper", "ewogICJ0aW1lc3RhbXAiIDogMTY0NjY4MTc0OTk2MywKICAicHJvZmlsZUlkIiA6ICJhNWI3YTJiZWM1OWQ0YWQwODczOTc5Mzc1ODdjZDgwNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJSYWJiaXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTU0Yjg0ZDMwNDNkMTU2NDY3NzQ4MTQ1OWEzYzU0MWRiZjVlNWViODFlZGI5MTA1YmE1NzIxYmY2OGRmZjg2MyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9", "sNeHCB5/3X3F9sLDQ2U6pJ61ihRLLx/VyIo4wbwbnb2GatOC6Bvd7ef3hT/yN8z2xTPEb1H8bo2+gO/0JR4UxzSJvxRLIqARG0BxzoIfYAAQKFtN9zR93xDkPbxBQKt/D9yPqSkHq3uwkmJk5aCHpwAdQJdfEbCWd3vzB5y0870RLXzgaIoLDQZFtJMytdFoCTGDKRddtwm0ORKIR+ohfD8NftErdxHrf9IiYUfi96+qBiQKJBYykZ+d40CpwAnNV/R2cAmwd+SIEFYR7EX4OOwoG0DvaQbVksdrm5FSATo1ynhY119+6YMktN476IAiNnuWCW5w3+yaW6gnqhOblAHIUVdNpk7gYmznoTSaVc1b6TO0lgvBGoA89rSwSLhzgZyYiDSiOjrWu61kVWFaNL+o4avF9nxMS4YniU8gCzrHMfPwSuwfFeC5B/ejWeTYkUUps4rD4cXPqJ0wgNWoyeu0sTTHsLFGeglNRQOZqRCfOO4rhVhxxEpiVb9PKsTfI5oGaB88yOFZ5sxYh8B0RPJFuD4uPEt16tSxgEodyIQ9rseP8Vf9WDvn+qy92Z1FZs7CpyZzKjU1PfR8+viLF6lZKNH6tI6gCz6v+qCGw5/twvBbvZWTMwLiAg9TqDR0nPoUi6R0eb9ENv54i/VZyLwo3K5L3fg086gpZST7JwM=", new LocationLIST().blueNPC());
        spawnNPC("§cShopkeeper", "ewogICJ0aW1lc3RhbXAiIDogMTY0NjY4MjE0MTkwNCwKICAicHJvZmlsZUlkIiA6ICIxNmQxMjc5N2ZhYjE0YTM2OGMzOWZjNGI0ODI3YTkxZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJnYW5nbGwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdlN2UyZDIxOWNlNGFhN2UyMzkyZmFjZWY4YmZkY2ZjZGIxOWY3MzBkMDkzZTFjZGZhZjg3YzBjNGYwZDVlNSIKICAgIH0KICB9Cn0=" ,"YG5/rxFSHr5P2lRrml4FbbIwdjGfvKHxNMywN01zHg37fcip2wspw4f3gekPToHjyS16e3fNjAqnqAYR5Le+o7jszFPizO8qkmDb9GrAN1lz7K0OQId+snfeSP3qKSBjCpI0MM1gAqeIQSMj1gf3CaeioEY+uP2JPVdXQ7YNbxbtuH3kjS5a5xoIib6RfqyAZKRn+CYvdMphKirU2PtZ7ZV4sP0ZDRUSrPI1ag1Gh4YuEdvO0g+TiGJlS9m9axalh6s7MSg4cjCWs1IbiFnH4usVfIaFfIZ+n+PHaO2FpQR7hu59zcIlMcTkBChcSyi4unhqd2zR5wSSe+kirlYv+tdAVPYv0pu3CHBWF6V+Zrbn2Z8xse0xB4DgELKLPBnIDUQQds8hUV5soP7fdD7x+wHUYVBABa3gF+NPc+WMHBl1O5U1htV6AAz94m5aqdgQCx4i3eeUyaTosZ58C08+ms8P6xsja9JPCBFFthikNn0gXeaESdNREqy73WPlUe5xxF76wDF+Jc6mcsbj3V78sad/ifXpw5Ge0VIknmI/LZWMgzOJYDl0SYriMoOF1gLWsU59ukd2si0qiJ636g5rbiC7Qjnv5UXBrur/+lwAnQouPMqa/2PJBAjmSC3tenR0TWJ/9IkKaVZGyo827B0pujVYePnbfxgCVUjqbtAV354=", new LocationLIST().redNPC());
        //spawnNPC("§cShopkeeper", "ewogICJ0aW1lc3RhbXAiIDogMTY0NzEyNDQ0NjEyMiwKICAicHJvZmlsZUlkIiA6ICI4MTVhMzRkYzRiZDg0YmE5YjJmMGVjOWZkMzY2OWFmNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJlcmljX3lvdW5nIiwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzViZDdiMmFiYzAxOWYyYTNiNzQyYjNmOTM1NGQ5YmE2YWMyY2M4YjQ2M2I5OGIwYTI0MzY2MjViYzdlZWIyZWQiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==", "", new LocationLIST().blueNPC());
    }

    public static void show(Player p) {
        NPC.show(p);
    }
}

@SuppressWarnings("serial")
class NPC implements Serializable {

    private int entityID;
    private Location location;
    private GameProfile gameprofile;
    private Float health = 20F;
    private static HashMap<NPC, String> npcHashMap = new HashMap<>();

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

