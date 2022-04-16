package com.osterph.listener;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.lagerhalle.LocationLIST;
import com.osterph.shop.Shop;
import com.osterph.shop.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;

public class InteractEvent implements Listener {
	
	ArrayList<Player> fireball = new ArrayList<>();
	
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            if (p.getItemInHand() == null) return;
            if(p.getItemInHand().getType().equals(Material.FIREBALL)) {
                e.setCancelled(true);
                if (fireball.contains(p)) return;
                if (p.getItemInHand().getAmount() == 1) p.getInventory().clear(p.getInventory().getHeldItemSlot());
                p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
                Fireball a = p.launchProjectile(Fireball.class);
                a.setVelocity(a.getVelocity().multiply(1.5));
                fireball.add(p);
                Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, () -> fireball.remove(p),20);
            } else if(p.getItemInHand().getType().equals(Material.BLAZE_ROD)) {
                if(!p.getLocation().add(0,-1,0).getBlock().getType().equals(Material.AIR)) {p.sendMessage(CTE.prefix + "§cDieses Item kannst du gerade nicht benutzen!");return;}
                if (p.getItemInHand().getAmount() == 1) p.getInventory().clear(p.getInventory().getHeldItemSlot());
                p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
                onSave(e.getPlayer());
            } else if(p.getItemInHand().getType().equals(Material.BRICK)) {
                if(!canBridge(p.getLocation(), getDirection(p))) {p.sendMessage(CTE.prefix + "§cDieses Item kannst du gerade nicht benutzen!");e.setCancelled(true);return;}
            	e.setCancelled(true);
            	onBridge(p.getLocation().add(0, -1, 0), getDirection(p), 0, p);
            	if (p.getItemInHand().getAmount() == 1) p.getInventory().clear(p.getInventory().getHeldItemSlot());
                p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
            } else if(p.getItemInHand().getType().equals(Material.ENDER_PEARL)) {
                if (e.getPlayer().getInventory().getHelmet() != null && e.getPlayer().getInventory().getHelmet().getType().equals(Material.SKULL_ITEM)) {
                    p.sendMessage(CTE.prefix + "§cDu kannst dich mit dem Ei nicht teleportieren.");
                    p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
                    return;
                }
                //if (p.getItemInHand().getAmount() == 1) p.getInventory().clear(p.getInventory().getHeldItemSlot());
                //p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
            } else if(p.getItemInHand().getType().equals(Material.ENDER_PORTAL_FRAME)) {
                e.setCancelled(true);
                CTESystem sys = CTE.INSTANCE.getSystem();

                if (e.getPlayer().getInventory().getHelmet() != null && e.getPlayer().getInventory().getHelmet().getType().equals(Material.SKULL_ITEM)) {
                    p.sendMessage(CTE.prefix + "§cDu kannst dich mit dem Ei nicht teleportieren.");
                    p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
                    return;
                }
                p.setFallDistance(0);
                if (p.getItemInHand().getAmount() == 1) p.getInventory().clear(p.getInventory().getHeldItemSlot());
                p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);

                LocationLIST l = CTE.INSTANCE.getLocations();
                Location loc = l.specSPAWN();

                switch (sys.teams.get(p)) {
                    case RED:
                        loc = l.redSPAWN();
                        break;
                    case BLUE:
                        loc = l.blueSPAWN();
                        break;
                }

                p.teleport(loc);
                p.playSound(p.getLocation(), Sound.PORTAL_TRAVEL, 1, 1);
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 2);
            }
        }
    }
    
    private boolean canBridge(Location loc, direction dir) {
        switch (dir) {
            case N:
                loc.add(0, -1, -1);
                if (!loc.getBlock().getType().equals(Material.AIR)) {
                    return false;
                }
                break;
            case E:
                loc.add(1, -1, 0);
                if (!loc.getBlock().getType().equals(Material.AIR)) {
                    return false;
                }
                break;
            case S:
                loc.add(0, -1, 1);
                if (!loc.getBlock().getType().equals(Material.AIR)) {
                    return false;
                }
                break;
            case W:
                loc.add(-1, -1, 0);
                if (!loc.getBlock().getType().equals(Material.AIR)) {
                    return false;
                }
                break;
            case NW:
                loc.add(-1, -1, -1);
                if (!loc.getBlock().getType().equals(Material.AIR)) {
                    return false;
                }
                break;
            case NE:
                loc.add(1, -1, -1);
                if (!loc.getBlock().getType().equals(Material.AIR)) {
                    return false;
                }
                break;
            case SE:
                loc.add(1, -1, 1);
                if (!loc.getBlock().getType().equals(Material.AIR)) {
                    return false;
                }
                break;
            case SW:
                loc.add(-1, -1, 1);
                if (!loc.getBlock().getType().equals(Material.AIR)) {
                    return false;
                }
                break;
        }
        return true;
    }

    private void onBridge(Location loc, direction dir, int timer, Player p) {
        if (timer >= 30) return;
        Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, () -> {
            switch (dir) {
                case N:
                    loc.add(0, 0, -1);
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        bridgeBlock(loc, p);
                        onBridge(loc, dir, timer + 1, p);
                    }
                    break;
                case E:
                    loc.add(1, 0, 0);
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        bridgeBlock(loc, p);
                        onBridge(loc, dir, timer + 1, p);
                    }
                    break;
                case S:
                    loc.add(0, 0, 1);
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        bridgeBlock(loc, p);
                        onBridge(loc, dir, timer + 1, p);
                    }
                    break;
                case W:
                    loc.add(-1, 0, 0);
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        bridgeBlock(loc, p);
                        onBridge(loc, dir, timer + 1, p);
                    }
                    break;
                case NW:
                    loc.add(-1, 0, -1);
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        bridgeBlock(loc, p);
                        onBridge(loc, dir, timer + 1, p);
                    }
                    break;
                case NE:
                    loc.add(1, 0, -1);
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        bridgeBlock(loc, p);
                        onBridge(loc, dir, timer + 1, p);
                    }
                    break;
                case SE:
                    loc.add(1, 0, 1);
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        bridgeBlock(loc, p);
                        onBridge(loc, dir, timer + 1, p);
                    }
                    break;
                case SW:
                    loc.add(-1, 0, 1);
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        bridgeBlock(loc, p);
                        onBridge(loc, dir, timer + 1, p);
                    }
                    break;
            }
        }, 5);
    }

    private void bridgeBlock(Location loc, Player p) {
        loc.getBlock().setType(Material.STAINED_GLASS);
        CTESystem sys = CTE.INSTANCE.getSystem();
        loc.getBlock().setData((byte) sys.teamData(p));
    }
    
    private enum direction {
    	N, NE, E, SE, S, SW, W, NW
    }
    
    public static direction getDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 180) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return direction.N;
        } else if (22.5 <= rotation && rotation < 67.5) {
            return direction.NE;
        } else if (67.5 <= rotation && rotation < 112.5) {
            return direction.E;
        } else if (112.5 <= rotation && rotation < 157.5) {
            return direction.SE;
        } else if (157.5 <= rotation && rotation < 202.5) {
            return direction.S;
        } else if (202.5 <= rotation && rotation < 247.5) {
            return direction.SW;
        } else if (247.5 <= rotation && rotation < 292.5) {
            return direction.W;
        } else if (292.5 <= rotation && rotation < 337.5) {
            return direction.NW;
        } else if (337.5 <= rotation && rotation < 360.0) {
            return direction.N;
        } else {
            return direction.N;
        }
    }
    
    @EventHandler
    public void onTP(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        CTESystem sys = CTE.INSTANCE.getSystem();
        if (!e.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) return;
        if (!sys.gamestate.equals(CTESystem.GAMESTATE.RUNNING) && !sys.gamestate.equals(CTESystem.GAMESTATE.SUDDEN_DEATH)) {
            e.setCancelled(true);
            return;
        }
        if (e.getPlayer().getInventory().getHelmet() != null && e.getPlayer().getInventory().getHelmet().getType().equals(Material.SKULL_ITEM)) {
            e.setCancelled(true);
            p.sendMessage(CTE.prefix + "§cDu kannst dich mit dem Ei nicht teleportieren.");
            p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
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
                loc.getBlock().setData((byte)sys.teamData(p));
            }
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(CTE.INSTANCE, () -> {
            for(Location loc : locs) {
                if(loc.getBlock().getType().equals(Material.STAINED_GLASS)) {
                    loc.getBlock().setType(Material.AIR);
                }
            }
        }, 20*10);
    }

    @EventHandler
    public void onLand(ProjectileHitEvent e) {
        if(e.getEntity() instanceof Fireball) {
            e.getEntity().getLocation().getWorld().createExplosion(e.getEntity().getLocation(), 1.2F);
        }
        e.getEntity().remove();
    }
}
