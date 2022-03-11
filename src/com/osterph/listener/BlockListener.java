package com.osterph.listener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import com.osterph.cte.CTE;
import com.osterph.spawner.Spawner;

import java.util.ArrayList;
import java.util.List;

public class BlockListener implements Listener {
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		for(Spawner sp : CTE.INSTANCE.getSpawnermanager().spawners) {
			if(e.getBlock().getLocation().distance(sp.getLocation()) < 3) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(CTE.prefix + "§cDu kannst hier keine Blöcke platzieren! [Spawner-Schutz]");
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.VILLAGER_NO, 1, 1);
			}
		}
	}
	
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if (!isBreakable(e.getBlock())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTNT(BlockExplodeEvent e) {
        List<Block> d = new ArrayList<>();
        for (Block b: e.blockList()) {
            if (!isBreakable(b)) continue;
            d.add(b);
        }
        e.blockList().clear();
        e.blockList().addAll(d);
    }

    @EventHandler
    public void onTNT(EntityExplodeEvent e) {
        List<Block> d = new ArrayList<>();
        for (Block b: e.blockList()) {
            if (!isBreakable(b)) continue;
            d.add(b);
        }
        if (e.getEntity() instanceof TNTPrimed) {
            e.setCancelled(true);
            e.getEntity().getLocation().getWorld().createExplosion(e.getEntity().getLocation(), 3);
        }
        e.blockList().clear();
        e.blockList().addAll(d);
    }

    @EventHandler
    public void onExplode(ExplosionPrimeEvent e) {
        e.setFire(false);
        if (e.getEntity() instanceof TNTPrimed) e.setRadius(3);
        if (e.getEntity() instanceof Fireball) e.setRadius(2);
    }

    @EventHandler
    public void onDrop(EntitySpawnEvent e) {
        if (e.getEntityType().equals(EntityType.CHICKEN)) e.setCancelled(true);
        if (!e.getEntityType().equals(EntityType.DROPPED_ITEM)) return;
        Item i = (Item) e.getEntity();

        if (!isDropped(i.getItemStack().getType())) i.remove();
    }

    @SuppressWarnings("deprecation")
	public boolean isBreakable(Block b) {

        if (b.getType().equals(Material.WOOL)) {
            return b.getData() == 11 || b.getData() == 14;
        }
        if (b.getType().equals(Material.LONG_GRASS)) {
            return b.getData() == 1 || b.getData() == 2;
        }
        if (b.getType().equals(Material.ENDER_STONE)||
            b.getType().equals(Material.WOOD)||
            b.getType().equals(Material.RED_ROSE)||
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
            m.equals(Material.TNT)||
            m.equals(Material.APPLE)||
            m.equals(Material.MELON)||
            m.equals(Material.CARROT_ITEM)||
            m.equals(Material.ENDER_PEARL)||
            m.equals(Material.BLAZE_ROD)||
            m.equals(Material.EGG)) {

            return true;
        }

        return false;
    }
}
