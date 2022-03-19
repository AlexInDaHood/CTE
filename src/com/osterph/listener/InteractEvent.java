package com.osterph.listener;

import java.util.ArrayList;

import com.osterph.cte.CTESystem;
import com.osterph.shop.Shop;
import com.osterph.shop.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.osterph.cte.CTE;
import org.bukkit.event.player.PlayerTeleportEvent;

public class InteractEvent implements Listener {

    //
    //FIREBALL + RETTUNGSPLATFORM
    //
	
	ArrayList<Player> fireball = new ArrayList<>();
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            if (p.getItemInHand() == null) return;
            if(p.getItemInHand().getType().equals(Material.FIREBALL) && !fireball.contains(p)) {
                if (p.getItemInHand().getAmount() == 1) p.getInventory().clear(p.getInventory().getHeldItemSlot());
                p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
                Fireball a = p.launchProjectile(Fireball.class);
                a.setVelocity(a.getVelocity().multiply(1.5));
                e.setCancelled(true);
                fireball.add(p);
                Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, new Runnable() {
				@Override
				public void run() {
					fireball.remove(p);
					}
				},20);
                return;
            } else if(p.getItemInHand().getType().equals(Material.BLAZE_ROD)) {
                if (p.getItemInHand().getAmount() == 1) p.getInventory().clear(p.getInventory().getHeldItemSlot());
                p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
                onSave(e.getPlayer());
                return;
            }
        }
    }

    @EventHandler
    public void onTP(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        CTESystem sys = CTE.INSTANCE.getSystem();
        if (!e.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) return;
        if (!sys.gamestate.equals(CTESystem.GAMESTATE.RUNNING)) {
            e.setCancelled(true);
            return;
        }
        if (e.getPlayer().getInventory().getHelmet() != null && e.getPlayer().getInventory().getHelmet().getType().equals(Material.SKULL_ITEM)) {
            e.setCancelled(true);
            p.sendMessage(CTE.prefix + "§cDu kannst dich mit dem Ei nicht teleportieren.");
            p.getInventory().addItem(new ShopItem(Material.ENDER_PEARL, "§fEnderperle", "UwU", 1, 4, Shop.Ressourcen.Karotte, null, 30, Shop.SHOPTYPE.SONSTIGES).getInventoryItem(0));
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
        p.setFallDistance(0);

        CTESystem sys = CTE.INSTANCE.getSystem();
        for(Location loc : locs) {
            if(loc.getBlock().getType().equals(Material.AIR)) {
                loc.getBlock().setType(Material.STAINED_GLASS);
                if (sys.teams.get(p).equals(CTESystem.TEAM.RED)) loc.getBlock().setData((byte) 14);
                if (sys.teams.get(p).equals(CTESystem.TEAM.BLUE)) loc.getBlock().setData((byte) 11);
            }
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, new Runnable() {
            @Override
            public void run() {
                for(Location loc : locs) {
                    if(loc.getBlock().getType().equals(Material.STAINED_GLASS)) {
                        loc.getBlock().setType(Material.AIR);
                    }
                }
            }
        }, 20*10);
    }

    @EventHandler
    public void onLand(ProjectileHitEvent e) {
        if(e.getEntity() instanceof Fireball) {
            e.getEntity().getLocation().getWorld().createExplosion(e.getEntity().getLocation(), 1.45F);
        }
        e.getEntity().remove();
    }
}
