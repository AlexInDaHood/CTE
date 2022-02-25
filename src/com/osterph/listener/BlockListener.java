package com.osterph.listener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class BlockListener implements Listener {


    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if (!isBreakable(e.getBlock())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(EntitySpawnEvent e) {
        if (!e.getEntityType().equals(EntityType.DROPPED_ITEM)) return;
        Item i = (Item) e.getEntity();

        if (!isDropped(i.getItemStack().getType())) i.remove();
    }

    public boolean isBreakable(Block b) {

        if (b.getType().equals(Material.WOOL)) {
            return b.getData() == 11 || b.getData() == 14;
        }
        if (b.getType().equals(Material.LONG_GRASS)) {
            return b.getData() == 1 || b.getData() == 0;
        }
        if (b.getType().equals(Material.ENDER_STONE)||
            b.getType().equals(Material.WOOD)||
            b.getType().equals(Material.SANDSTONE)||
            b.getType().equals(Material.OBSIDIAN)||
            b.getType().equals(Material.WEB)||
            b.getType().equals(Material.LADDER)) {

            return true;
        }
        return false;
    }

    private boolean isDropped(Material m) {

        if (m.equals(Material.ENDER_STONE)||
            m.equals(Material.WOOD)||
            m.equals(Material.SANDSTONE)||
            m.equals(Material.OBSIDIAN)||
            m.equals(Material.LADDER)||
            m.equals(Material.STONE_SWORD)||
            m.equals(Material.IRON_SWORD)||
            m.equals(Material.DIAMOND_SWORD)||
            m.equals(Material.BOW)||
            m.equals(Material.ARROW)||
            m.equals(Material.WEB)||
            m.equals(Material.WATER_BUCKET)||
            m.equals(Material.GOLDEN_APPLE)||
            m.equals(Material.TNT)) {

            return true;
        }

        return false;
    }
}
