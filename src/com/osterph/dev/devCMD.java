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
		inv.setItem(20, new ItemManager(Material.DRAGON_EGG).withName("§3§lSpiel Einstellungen").complete());
		inv.setItem(22, new ItemManager(Material.MELON_BLOCK).withName("§3§lSpawner Einstellungen").complete());
		inv.setItem(24, new ItemManager(Material.SKULL_ITEM).withName("§3§lSpieler Einstellungen").complete());
		
		p.openInventory(inv);
	}

}
