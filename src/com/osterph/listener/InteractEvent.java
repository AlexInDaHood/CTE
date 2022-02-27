package com.osterph.listener;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftRabbit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
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
            }
        }
    }

    @EventHandler
    public void onLand(ProjectileHitEvent e) {
        if(e.getEntity() instanceof Fireball) {
            e.getEntity().getLocation().getWorld().createExplosion(e.getEntity().getLocation(), 1.35F);
        }
        e.getEntity().remove();
    }
}
