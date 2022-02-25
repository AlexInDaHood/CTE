package com.osterph.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ShopListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getView().getTopInventory().getName().contains("§a§b§cShop")) {

        }
    }
}
