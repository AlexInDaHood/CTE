package com.osterph.listener;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractEvent implements Listener {

    //
    //FIREBALL
    //
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            if(p.getItemInHand().getType().equals(Material.FIREBALL)) {
                p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
                p.launchProjectile(Fireball.class);
            } else if(p.getItemInHand().getType().equals(Material.BLAZE_ROD)) {
                onSave(e.getPlayer());
            }
        }
    }

    private void onSave(Player p) {
        ArrayList<Location> locs = new ArrayList<>();
        locs.add(p.getLocation().add(0, -2, 0));
        locs.add(p.getLocation().add(1, -2, 1));
        locs.add(p.getLocation().add(1, -2, 0));
        locs.add(p.getLocation().add(0, -2, 1));
        locs.add(p.getLocation().add(-1, -2, 0));
        locs.add(p.getLocation().add(0, -2, -1));
        locs.add(p.getLocation().add(-1, -2, -1));
        locs.add(p.getLocation().add(-1, -2, 1));
        locs.add(p.getLocation().add(1, -2, -1));
        locs.add(p.getLocation().add(2, -2, 0));
        locs.add(p.getLocation().add(-2, -2, 0));
        locs.add(p.getLocation().add(0, -2, 2));
        locs.add(p.getLocation().add(0, -2, -2));

        for(Location loc : locs) {
            if(loc.getBlock().getType().equals(Material.AIR)) {
                loc.getBlock().setType(Material.DIAMOND_BLOCK);
            }
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, new Runnable() {
            @Override
            public void run() {
                for(Location loc : locs) {
                    if(loc.getBlock().getType().equals(Material.DIAMOND_BLOCK)) {
                        loc.getBlock().setType(Material.AIR);
                    }
                }
            }
        }, 20*10);
    }

    @EventHandler
    public void onLand(ProjectileHitEvent e) {
        if(e.getEntity() instanceof Fireball) {
            e.getEntity().getLocation().getWorld().createExplosion(e.getEntity().getLocation(), 1.35F);
        }
        e.getEntity().remove();
    }
}
