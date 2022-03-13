package com.osterph.dev;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem.GAMESTATE;
import com.osterph.manager.ItemManager;
import com.osterph.shop.Shop;

public class devCMD implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        if (!p.isOp() &&!new StaffManager(p).isDev()) {
            p.sendMessage(CTE.prefix + "§cUnzureichende Berechtigungen.");
            return false;
        }
		
        openInventory(p);
        
		return false;
	}
	
	public static void openInventory(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*5, "§8» §bDev");
		inv.setContents(Shop.getStandardGUI45(false));
		inv.setItem(20, ItemManager.newItem(Material.DRAGON_EGG, "§3§lSpiel Einstellungen", "", 0));
		inv.setItem(22, ItemManager.newItem(Material.MELON_BLOCK, "§3§lSpawner Einstellungen", "", 0));
		inv.setItem(24, ItemManager.newItem(Material.SKULL_ITEM, "§3§lSpieler Einstellungen", "", 0));
		
		p.openInventory(inv);
	}

}
