package com.osterph.listener;

import java.util.ArrayList;
import java.util.List;

import com.osterph.shop.Shop;
import com.osterph.shop.ShopItem;
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
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener {
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
        if(e.getBlock().getType().equals(Material.TNT)) {
            e.setCancelled(true);
            if(e.getPlayer().getItemInHand().getAmount() > 1) {
                e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
            } else {
                e.getPlayer().getInventory().remove(e.getPlayer().getItemInHand());
            }
            e.getBlock().getLocation().getWorld().spawn(e.getBlock().getLocation(), TNTPrimed.class);
            return;
        }
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            for (Spawner sp : CTE.INSTANCE.getSpawnermanager().spawners) {
                if (e.getBlock().getLocation().distance(sp.getLocation()) < 3) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(CTE.prefix + "§cDu kannst hier keine Blöcke platzieren! [Spawner-Schutz]");
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.VILLAGER_NO, 1, 1);
                    return;
                }
            }
            if ((e.getBlock().getLocation().distance(CTE.INSTANCE.getLocations().redSPAWN()) < 2) || (e.getBlock().getLocation().distance(CTE.INSTANCE.getLocations().blueSPAWN()) < 2)) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(CTE.prefix + "§cDu kannst hier keine Blöcke platzieren! [Spawn-Schutz]");
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.VILLAGER_NO, 1, 1);
                return;
            }

            if(e.getBlock().getLocation().getY() >= 110) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(CTE.prefix + "§cDu hast die maximale Bauhöhe erreicht!");
                return;
            }

            if(e.getBlock().getLocation().getX() > 1100 || e.getBlock().getLocation().getX() < 900 || e.getBlock().getLocation().getZ() < 945 || e.getBlock().getLocation().getZ() > 1055) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(CTE.prefix + "§cDu kannst dich nicht weiter von der Map entfernen!");
                return;
            }

        }
	}
	
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if(e.getBlock().getType().equals(Material.WEB)) return;
        if (!isBreakable(e.getBlock())) {
            e.setCancelled(true);
            return;
        }
        
        if (CTE.INSTANCE.getShop().getShopItemByMaterial(e.getBlock().getType()) != null) {
	        e.setCancelled(true);
	        ItemStack drops = CTE.INSTANCE.getShop().getShopItemByMaterial(e.getBlock().getType()).getInventoryItem(e.getBlock().getData());
	        drops.setAmount(1);
	        e.getBlock().setType(Material.AIR);
	        e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), drops);
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

  /*  @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        Material m = e.getItem().getItemStack().getType();
        ItemStack i = e.getItem().getItemStack();
        switch (m) {
            case WOOL:
                i = new ShopItem(Material.WOOL, "§7Wolle", "Zum bauen", 16, 4, Shop.Ressourcen.Apfel, null, 20, Shop.SHOPTYPE.BLOCKS).getInventoryItem(i.getDurability());
                break;
            case WOOD:
                i = new ShopItem(Material.WOOD, "§7Holz", "Zum bauen", 16, 4, Shop.Ressourcen.Melone, null, 21, Shop.SHOPTYPE.BLOCKS).getInventoryItem(0);
                break;
            case SANDSTONE:
                i = new ShopItem(Material.SANDSTONE, "§7Sandstein", "Zum bauen", 16, 12, Shop.Ressourcen.Apfel, null, 23, Shop.SHOPTYPE.BLOCKS).getInventoryItem(2);
                break;
            case ENDER_STONE:
                i = new ShopItem(Material.ENDER_STONE, "§7Endstein", "Zum bauen", 8, 24, Shop.Ressourcen.Apfel, null, 24, Shop.SHOPTYPE.BLOCKS).getInventoryItem(0);
                break;
            case OBSIDIAN:
                i = new ShopItem(Material.OBSIDIAN, "§7Obsidian", "Zum bauen", 2, 4, Shop.Ressourcen.Karotte, null, 30, Shop.SHOPTYPE.BLOCKS).getInventoryItem(0);
                break;
            case LADDER:
                i = new ShopItem(Material.LADDER, "§7Leiter", "Zum bauen", 16, 4, Shop.Ressourcen.Apfel, null, 32, Shop.SHOPTYPE.BLOCKS).getInventoryItem(0);
                break;
        }
        e.getItem().setItemStack(i);
   }*/

    @EventHandler
    public void onExplode(ExplosionPrimeEvent e) {
        e.setFire(false);
        if (e.getEntity() instanceof TNTPrimed) e.setRadius(4);
        if (e.getEntity() instanceof Fireball) e.setRadius(3);
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
        if (b.getType().equals(Material.WOOD)) return b.getData() == 0;

        return b.getType().equals(Material.ENDER_STONE) ||
                b.getType().equals(Material.RED_ROSE) ||
                b.getType().equals(Material.SANDSTONE) ||
                b.getType().equals(Material.OBSIDIAN) ||
                b.getType().equals(Material.WEB) ||
                b.getType().equals(Material.LADDER);
    }

    private boolean isDropped(Material m) {
        return m.equals(Material.ENDER_STONE) ||
                m.equals(Material.WOOD) ||
                m.equals(Material.SANDSTONE) ||
                m.equals(Material.OBSIDIAN) ||
                m.equals(Material.LADDER) ||
                m.equals(Material.STONE_SWORD) ||
                m.equals(Material.IRON_SWORD) ||
                m.equals(Material.DIAMOND_SWORD) ||
                m.equals(Material.BOW) ||
                m.equals(Material.ARROW) ||
                m.equals(Material.WEB) ||
                m.equals(Material.WATER_BUCKET) ||
                m.equals(Material.GOLDEN_APPLE) ||
                m.equals(Material.TNT) ||
                m.equals(Material.APPLE) ||
                m.equals(Material.MELON) ||
                m.equals(Material.CARROT_ITEM) ||
                m.equals(Material.ENDER_PEARL) ||
                m.equals(Material.BLAZE_ROD) ||
                m.equals(Material.FIREBALL) ||
                m.equals(Material.WOOL);
    }
}
