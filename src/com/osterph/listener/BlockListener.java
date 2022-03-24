package com.osterph.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.osterph.shop.Shop;
import com.osterph.shop.ShopItem;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.cte.CTESystem.TEAM;
import com.osterph.spawner.Spawner;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener {
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
        if(CTE.INSTANCE.getSystem().gamestate.equals(CTESystem.GAMESTATE.SUDDEN_DEATH)) {e.getPlayer().sendMessage(CTE.prefix + "Du kannst keine Blöcke während des Suddendeaths platzieren!");e.setCancelled(true);return;}
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

    //
    //NO EXPLOSION SAVE
    //

    @EventHandler
    public void onTNT(BlockExplodeEvent e) {
        if(!CTE.INSTANCE.getSystem().gamestate.equals(CTESystem.GAMESTATE.SUDDEN_DEATH)) {
            List<Block> d = new ArrayList<>();
            for (Block b : e.blockList()) {
                if (!isBreakable(b)) continue;
                d.add(b);
            }
            e.blockList().clear();
            e.blockList().addAll(d);
        }
    }

    @EventHandler
    public void onTNT(EntityExplodeEvent e) {
        if(!CTE.INSTANCE.getSystem().gamestate.equals(CTESystem.GAMESTATE.SUDDEN_DEATH)) {
            List<Block> d = new ArrayList<>();
            for (Block b : e.blockList()) {
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
                b.getType().equals(Material.LADDER) ||
                b.getType().equals(Material.SKULL);
    }
    
    
    
    //
    //CHEST-System
    //
    private HashMap<Player, Inventory> inventorys = new HashMap<>();
	
	@EventHandler
	public void openInv(InventoryOpenEvent e) {		
		if(e.getInventory().getType() != InventoryType.CHEST && e.getInventory().getType() != InventoryType.ENDER_CHEST) return;
			Player p = (Player)e.getPlayer();
			CTESystem sys = CTE.INSTANCE.getSystem();
		if(e.getInventory().getType().equals(InventoryType.CHEST)) {
			if(e.getInventory().getTitle().contains("Blue Chest")) {
				if(!sys.teams.get(p).equals(TEAM.BLUE) && !p.getGameMode().equals(GameMode.CREATIVE)) {
					p.sendMessage(CTE.prefix+ "Du kannst die §9Blaue-Kiste §enicht öffnen!");
					e.setCancelled(true);
				}
			} else if(e.getInventory().getTitle().contains("Red Chest")) {
				if(!sys.teams.get(p).equals(TEAM.RED) && !p.getGameMode().equals(GameMode.CREATIVE)) {
					p.sendMessage(CTE.prefix + "Du kannst die §cRote-Kiste §enicht öffnen!");
					e.setCancelled(true);
				}
			}
		} else if(e.getInventory().getType().equals(InventoryType.ENDER_CHEST)) {
			e.setCancelled(true);
			if(inventorys.containsKey(p)) {
				p.openInventory(inventorys.get(p));
			} else {
				Inventory inv = Bukkit.createInventory(null, 9*3, "Enderchest");
				p.openInventory(inv);
				inventorys.put(p, inv);
			}
		}
	}

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getView().getTopInventory().getType().equals(InventoryType.CHEST) || e.getView().getTopInventory().getType().equals(InventoryType.ENDER_CHEST)) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == null) return;
            EggListener.checkHolzschwert((Player) e.getWhoClicked());
            if(EggListener.blockedItems().contains(e.getCurrentItem().getType())) {
                e.setCancelled(true);
            }
        }
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
                m.equals(Material.WOOL) ||
                m.equals(Material.BRICK);
    }
}
