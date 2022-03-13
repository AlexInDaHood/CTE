package com.osterph.dev;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem.GAMESTATE;
import com.osterph.manager.ItemManager;
import com.osterph.shop.Shop;
import com.osterph.spawner.Spawner;


public class DevListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getAction() == InventoryAction.NOTHING) return;
		if(e.getInventory().getTitle().contains("§8» §bDev")) {
			e.setCancelled(true);
			if(e.getCurrentItem().getType().equals(Material.AIR)) return;
			if(e.getCurrentItem().getItemMeta().getDisplayName() == null) return;
			
			Player p = (Player)e.getWhoClicked();
			ItemStack item = e.getCurrentItem();
			
			if(e.getInventory().getTitle().equals("§8» §bDev")) {
				if(item.getItemMeta().getDisplayName().contains("Spawner Einstellungen")) {
					openInventory(p, settingstype.SPAWNER);
				} else if(item.getItemMeta().getDisplayName().contains("Spieler Einstellungen")) {
					openInventory(p, settingstype.PLAYER);
				} else if(item.getItemMeta().getDisplayName().contains("Spiel Einstellungen")) {
					openInventory(p, settingstype.GAME);
				}
				if(item.getItemMeta().getDisplayName().contains("Schließen")) {
					p.closeInventory();
				}
			} else if(e.getInventory().getTitle().contains("Spawner Einstellungen")) {
				if(item.getItemMeta().getDisplayName().contains("Zurück")) {
					devCMD.openInventory(p);
				}
				
				if(item.getItemMeta().getDisplayName().contains("Apfel")) {
					openInventory(p, settingstype.APPLE);
				} else if(item.getItemMeta().getDisplayName().contains("Melone")) {
					openInventory(p, settingstype.MELON);
				} else if(item.getItemMeta().getDisplayName().contains("Karrote")) {
					openInventory(p, settingstype.CARROT);
				}
				
			} else if(e.getInventory().getTitle().contains("Spieler Einstellungen")) {
				if(item.getItemMeta().getDisplayName().contains("Zurück")) {
					devCMD.openInventory(p);
				}
				
			} else if(e.getInventory().getTitle().contains("Spiel Einstellungen")) {
				if(item.getItemMeta().getDisplayName().contains("Zurück")) {
					devCMD.openInventory(p);
				} else if(item.getItemMeta().getDisplayName().contains("Shutdown")) {
					CTE.INSTANCE.getSystem().sendAllMessage(CTE.prefix + "§cDas Spiel wurde druch einen Administrator beendet!");
					CTE.INSTANCE.getSystem().moveHub();
					Bukkit.shutdown();
				} else if(item.getItemMeta().getDisplayName().contains("Stop Game")) {
					if(!CTE.INSTANCE.getSystem().gamestate.equals(GAMESTATE.ENDING)) {
						CTE.INSTANCE.getSystem().sendAllMessage(CTE.prefix + "§cDas Spiel wurde druch einen Administrator beendet!");
						CTE.INSTANCE.getSystem().endGame();
					} else {
						p.sendMessage(CTE.prefix + "Das Spiel ist bereits dabei zu beenden!");
					}
				} else if(item.getItemMeta().getDisplayName().contains("Maximale Spieler")) {
					int current = CTE.INSTANCE.getSystem().maxPlayers;
					if(e.getAction().equals(InventoryAction.PICKUP_ALL)) {
						if(current > 2 && CTE.INSTANCE.getSystem().minPlayers < current) {
							current--;
						} else {
							p.sendMessage(CTE.prefix + "Die Zahl §c" + current + " §eist invalid für die Maximale-Spieler Anzahl!");
							return;
						}
					} else if(e.getAction().equals(InventoryAction.PICKUP_HALF)) {
						current++;
					}
					CTE.INSTANCE.getSystem().maxPlayers = current;
					e.getInventory().setItem(20, ItemManager.newItem(Material.DIAMOND, "§cMaximale Spieler §8[§a" +CTE.INSTANCE.getSystem().maxPlayers + "§8]" , "-/-§7Ändert die Anzahl der maximalen Anzahl der Spieler", 0));
					p.sendMessage(CTE.prefix + "Du hast die maximale Anzahl an Spielern auf §a" + current + " §egesetzt!");
				} else if(item.getItemMeta().getDisplayName().contains("Minimale Spieler")) {
					int current = CTE.INSTANCE.getSystem().minPlayers;
					if(e.getAction().equals(InventoryAction.PICKUP_ALL)) {
						if(current > 2) {
							current--;
						} else {
							p.sendMessage(CTE.prefix + "Die Zahl §c" + current + " §eist invalid für die Minimale-Spieler Anzahl!");
							return;
						}
					} else if(e.getAction().equals(InventoryAction.PICKUP_HALF)) {
						if(CTE.INSTANCE.getSystem().maxPlayers > current) {
							current++;
						} else {
							p.sendMessage(CTE.prefix + "Die Zahl §c" + current + " §eist invalid für die Minimale-Spieler Anzahl!");
							return;
						}
					}
					CTE.INSTANCE.getSystem().minPlayers = current;
					e.getInventory().setItem(22, ItemManager.newItem(Material.EMERALD, "§cMinimale Spieler §8[§a" +CTE.INSTANCE.getSystem().minPlayers + "§8]", "-/-§7Ändert die Anzahl der minmalen Anzahl der Spieler", 0));
					p.sendMessage(CTE.prefix + "Du hast die minmale Anzahl an Spielern auf §a" + current + " §egesetzt!");
				}
				
				
			} else if(e.getInventory().getTitle().contains("Apfel Spawner") ||e.getInventory().getTitle().contains("Melonen Spawner") || e.getInventory().getTitle().contains("Karroten Spawner")) {
				//
				//SPAWNER SETTINGS
				//
				if(item.getItemMeta().getDisplayName().contains("Zurück")) {
					openInventory(p, settingstype.SPAWNER);
				}
				if(item.getType().equals(Material.STAINED_GLASS)) return;
				String spawner = e.getInventory().getTitle().replace("§8» §bDev §8§l> ","").split(" ")[0];
				Material mat = e.getInventory().getItem(4).getType();
				if(item.getItemMeta().getDisplayName().contains("Aktivieren") || item.getItemMeta().getDisplayName().contains("Deaktivieren")) {
					boolean set = item.getItemMeta().getDisplayName().contains("Aktivieren") ? true : false;
					for(Spawner sp : CTE.INSTANCE.getSpawnermanager().spawners) {
						if(sp.getItem().getType().equals(mat)) {
							sp.setActivated(set);
						}
					}
					if(set) {
						e.getInventory().setItem(19, ItemManager.newItem(Material.INK_SACK, "§cDeaktivieren", "-/-§8§oDeaktiviert alle Äpfel Spawner", 1));
						p.sendMessage(CTE.prefix + "Du hast alle " + spawner + "§e-Spawner §aAktiviert!");
					} else {
						e.getInventory().setItem(19, ItemManager.newItem(Material.INK_SACK, "§aAktivieren", "-/-§8§oAktiviert alle Äpfel Spawner", 10));
						p.sendMessage(CTE.prefix + "Du hast alle " + spawner + "§e-Spawner §cDeaktiviert!");
					}
					return;
				} else if(item.getItemMeta().getDisplayName().contains("Anzahl")) {
					int current = Integer.parseInt(item.getItemMeta().getDisplayName().replace("§7Anzahl §8[§a", "").replace("§8]", ""));
					if(e.getAction().equals(InventoryAction.PICKUP_ALL)) {
						if(current > 1) {
							current--;
						} else {
							p.sendMessage(CTE.prefix + "Du kannst die Anzahl der spawnenden Items nicht auf 0 setzen!");
							return;
						}
					} else if(e.getAction().equals(InventoryAction.PICKUP_HALF)) {
						current++;
					}
					for(Spawner sp : CTE.INSTANCE.getSpawnermanager().spawners) {
						if(sp.getItem().getType().equals(mat)) {
							sp.setAmount(current);
						}
					}
					e.getInventory().setItem(21, ItemManager.newItem(Material.DIAMOND, "§7Anzahl §8[§a" + current + "§8]", "-/-§8§oÄndert die Anzahl an Items für alle "+spawner+"§8-Spawner-/--/-§8§oRechtsklick +1-/-§8§oLinksklick -1", 0));
					p.sendMessage(CTE.prefix + "Du hast die Anzahl der Spawnenden Items aller " + spawner + "§e-Spawner auf §a" + current +" §egesetzt!");
				} else if(item.getItemMeta().getDisplayName().contains("Delay")) {
					int current = Integer.parseInt(item.getItemMeta().getDisplayName().replace("§7Delay §8[§a", "").replace("§8]", ""));
					if(e.getAction().equals(InventoryAction.PICKUP_ALL)) {
						if(current > 10) {
							current -= 10;
						} else {
							p.sendMessage(CTE.prefix + "Du kannst den Delay nicht auf 0 setzen!");
							return;
						}
					} else if(e.getAction().equals(InventoryAction.PICKUP_HALF)) {
						current +=10;
					}
					for(Spawner sp : CTE.INSTANCE.getSpawnermanager().spawners) {
						if(sp.getItem().getType().equals(mat)) {
							sp.setDelay(current);
						}
					}
					e.getInventory().setItem(23, ItemManager.newItem(Material.WATCH, "§7Delay §8[§a" + current + "§8]", "-/-§8§oÄndert den delay der Items für alle "+spawner+"§8-Spawner-/--/-§8§oRechtsklick +1-/-§8§oLinksklick -1", 0));
					p.sendMessage(CTE.prefix + "Du hast den Delay aller " + spawner + "§e-Spawner auf §a" + current +" §egesetzt!");
				} else if(item.getItemMeta().getDisplayName().contains("Maximal")) {
					int current = Integer.parseInt(item.getItemMeta().getDisplayName().replace("§7Maximal §8[§a", "").replace("§8]", ""));
					if(e.getAction().equals(InventoryAction.PICKUP_ALL)) {
						if(current > 1) {
							current--;
						} else {
							p.sendMessage(CTE.prefix + "Du kannst die Maximale-Anzahl an Items nicht auf 0 setzen!");
							return;
						}
					} else if(e.getAction().equals(InventoryAction.PICKUP_HALF)) {
						current++;
					}
					for(Spawner sp : CTE.INSTANCE.getSpawnermanager().spawners) {
						if(sp.getItem().getType().equals(mat)) {
							sp.setMax(current);
						}
					}
					e.getInventory().setItem(25, ItemManager.newItem(Material.BARRIER, "§7Maximal §8[§a" + current + "§8]", "-/-§8§oÄndert die Maximale Anzahl an Items für alle " + spawner + "§8-Spawner-/--/-§8§oRechtsklick +1-/-§8§oLinksklick -1", 0));
					p.sendMessage(CTE.prefix + "Du hast die Maximale Anzahl der Items aller " + spawner + "§e-Spawner auf §a" + current +" §egesetzt!");
				}
					
			}
		}
	}
	
	private void openInventory(Player p, settingstype type) {
		Inventory inv = null;
		switch (type) {
		case GAME:
			inv = Bukkit.createInventory(null, 9*5, "§8» §bDev §8§l> §cSpiel Einstellungen");
			inv.setContents(Shop.getStandardGUI45(true));
			inv.setItem(4, ItemManager.newItem(Material.DRAGON_EGG, "§3§lSpiel Einstellungen", "", 0));
			if(CTE.INSTANCE.getSystem().gamestate.equals(GAMESTATE.RUNNING)) {
				inv.setItem(20, ItemManager.newItem(Material.INK_SACK, "§cStop Game", "-/-§cLässt das Team §7Default §cgewinnen!", 14));
				inv.setItem(24, ItemManager.newItem(Material.INK_SACK, "§cShutdown", "-/-§cStoppt die Runde!", 1));
			} else {
				inv.setItem(20, ItemManager.newItem(Material.DIAMOND, "§cMaximale Spieler §8[§a" +CTE.INSTANCE.getSystem().maxPlayers + "§8]" , "-/-§7Ändert die Anzahl der maximalen Anzahl der Spieler", 0));
				inv.setItem(22, ItemManager.newItem(Material.EMERALD, "§cMinimale Spieler §8[§a" +CTE.INSTANCE.getSystem().minPlayers + "§8]", "-/-§7Ändert die Anzahl der minmalen Anzahl der Spieler", 0));
				inv.setItem(24, ItemManager.newItem(Material.BARRIER, "§cPrivate-Round", "-/-§7Ändert die Runde zu einem Privat-Game", 0));
			}
			
			break;
		case PLAYER:
			inv = Bukkit.createInventory(null, 9*5, "§8» §bDev §8§l> §cSpieler Einstellungen");
			inv.setContents(Shop.getStandardGUI45(true));
			inv.setItem(4, ItemManager.newItem(Material.SKULL_ITEM, "§3§lSpawner Einstellungen", "", 0));
			break;
		case SPAWNER:
			inv = Bukkit.createInventory(null, 9*5, "§8» §bDev §8§l> §cSpawner Einstellungen");
			inv.setContents(Shop.getStandardGUI45(true));
			inv.setItem(4, ItemManager.newItem(Material.MELON_BLOCK, "§3§lSpawner Einstellungen", "", 0));
			inv.setItem(20, ItemManager.newItem(Material.APPLE, "§cApfel-Spawner", "", 0));
			inv.setItem(22, ItemManager.newItem(Material.MELON, "§aMelonen-Spawner", "", 0));
			inv.setItem(24, ItemManager.newItem(Material.CARROT_ITEM, "§6Karroten-Spawner", "", 0));
			break;
		case APPLE:
			inv = Bukkit.createInventory(null, 9*5, "§8» §bDev §8§l> §cApfel Spawner");
			inv.setContents(Shop.getStandardGUI45(true));
			inv.setItem(4, ItemManager.newItem(Material.APPLE, "§cApfel Spawner Einstellungen", "", 0));
			Spawner apple = CTE.INSTANCE.getSpawnermanager().getSpawnerByName("BLUE-APPLE");
			if(apple.isActivated()) {
				inv.setItem(19, ItemManager.newItem(Material.INK_SACK, "§cDeaktivieren", "-/-§8§oDeaktiviert alle Äpfel Spawner", 1));
			} else {
				inv.setItem(19, ItemManager.newItem(Material.INK_SACK, "§aAktivieren", "-/-§8§oAktiviert alle Äpfel Spawner", 10));
			}
			inv.setItem(21, ItemManager.newItem(Material.DIAMOND, "§7Anzahl §8[§a" + apple.getAmount() + "§8]", "-/-§8§oÄndert die Anzahl an Äpfel für alle Spawner-/--/-§8§oRechtsklick +1-/-§8§oLinksklick -1", 0));
			inv.setItem(23, ItemManager.newItem(Material.WATCH, "§7Delay §8[§a" + apple.getDelay() + "§8]", "-/-§8§oÄndert den delay an Äpfel für alle Spawner-/--/-§8§oRechtsklick +10-/-§8§oLinksklick -10", 0));
			inv.setItem(25, ItemManager.newItem(Material.BARRIER, "§7Maximal §8[§a" + apple.getMaximal() + "§8]", "-/-§8§oÄndert die Maximale Anzahl an Äpfel für alle Spawner-/--/-§8§oRechtsklick +1-/-§8§oLinksklick -1", 0));
			break;
		case MELON:
			inv = Bukkit.createInventory(null, 9*5, "§8» §bDev §8§l> §aMelonen Spawner");
			inv.setContents(Shop.getStandardGUI45(true));
			inv.setItem(4, ItemManager.newItem(Material.MELON, "§aMelonen Spawner Einstellungen", "", 0));
			Spawner melon = CTE.INSTANCE.getSpawnermanager().getSpawnerByName("BLUE-MELON-1");
			if(melon.isActivated()) {
				inv.setItem(19, ItemManager.newItem(Material.INK_SACK, "§cDeaktivieren", "-/-§8§oDeaktiviert alle Melonen Spawner", 1));
			} else {
				inv.setItem(19, ItemManager.newItem(Material.INK_SACK, "§aAktivieren", "-/-§8§oAktiviert alle Melonen Spawner", 10));
			}
			inv.setItem(21, ItemManager.newItem(Material.DIAMOND, "§7Anzahl §8[§a" + melon.getAmount() + "§8]", "-/-§8§oÄndert die Anzahl an Melonen für alle Spawner-/--/-§8§oRechtsklick +1-/-§8§oLinksklick -1", 0));
			inv.setItem(23, ItemManager.newItem(Material.WATCH, "§7Delay §8[§a" + melon.getDelay() + "§8]", "-/-§8§oÄndert den delay an Melonen für alle Spawner-/--/-§8§oRechtsklick +10-/-§8§oLinksklick -10", 0));
			inv.setItem(25, ItemManager.newItem(Material.BARRIER, "§7Maximal §8[§a" + melon.getMaximal() + "§8]", "-/-§8§oÄndert die Maximale Anzahl an Melonen für alle Spawner-/--/-§8§oRechtsklick +1-/-§8§oLinksklick -1", 0));
			break;
		case CARROT:
			inv = Bukkit.createInventory(null, 9*5, "§8» §bDev §8§l> §6Karroten Spawner");
			inv.setContents(Shop.getStandardGUI45(true));
			inv.setItem(4, ItemManager.newItem(Material.CARROT_ITEM, "§cKarroten Spawner Einstellungen", "", 0));
			Spawner carrot = CTE.INSTANCE.getSpawnermanager().getSpawnerByName("MIDDLE-CARROT-1");
			if(carrot.isActivated()) {
				inv.setItem(19, ItemManager.newItem(Material.INK_SACK, "§cDeaktivieren", "-/-§8§oDeaktiviert alle Karroten Spawner", 1));
			} else {
				inv.setItem(19, ItemManager.newItem(Material.INK_SACK, "§aAktivieren", "-/-§8§oAktiviert alle Karroten Spawner", 10));
			}
			inv.setItem(21, ItemManager.newItem(Material.DIAMOND, "§7Anzahl §8[§a" + carrot.getAmount() + "§8]", "-/-§8§oÄndert die Anzahl an Karroten für alle Spawner-/--/-§8§oRechtsklick +1-/-§8§oLinksklick -1", 0));
			inv.setItem(23, ItemManager.newItem(Material.WATCH, "§7Delay §8[§a" + carrot.getDelay() + "§8]", "-/-§8§oÄndert den delay an Karroten für alle Spawner-/--/-§8§oRechtsklick +10-/-§8§oLinksklick -10", 0));
			inv.setItem(25, ItemManager.newItem(Material.BARRIER, "§7Maximal §8[§a" + carrot.getMaximal() + "§8]", "-/-§8§oÄndert die Maximale Anzahl an Karroten für alle Spawner-/--/-§8§oRechtsklick +1-/-§8§oLinksklick -1", 0));
			break;
		}
		
		p.openInventory(inv);
	}
	
	private enum settingstype {
		SPAWNER, PLAYER, GAME, APPLE, MELON, CARROT;
	}
	
}
