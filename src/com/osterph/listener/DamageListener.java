package com.osterph.listener;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private CTESystem sys = CTE.INSTANCE.system;

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!sys.isRunning) {
            e.setCancelled(true);
            return;
        }

        Enum<EntityDamageEvent.DamageCause> cause = e.getCause();

        if (cause.equals(EntityDamageEvent.DamageCause.CONTACT)) {
            e.setCancelled(true);
        } else if (cause.equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!sys.isRunning) {
            e.setCancelled(true);
            return;
        }
        if (e.getEntity() instanceof ArmorStand) {
            e.setCancelled(true);
            return;
        }

        if (e.getEntity() instanceof Player && ((Player) e.getEntity()).getOpenInventory() != null) ((Player) e.getEntity()).closeInventory();

        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (sys.teams.get(p).equals(CTESystem.TEAM.SPEC) || sys.teams.get(p).equals(CTESystem.TEAM.DEFAULT)) {
            e.setCancelled(true);
            return;
        }

        if (e.getDamager() instanceof Projectile) {
            Projectile pro = (Projectile) e.getDamager();
            if (pro instanceof Fireball) return;
            Player Shooter = (Player) pro.getShooter();
            if (sys.teams.get(Shooter).equals(CTESystem.TEAM.SPEC) || sys.teams.get(Shooter).equals(CTESystem.TEAM.DEFAULT)) {
                e.setCancelled(true);
                return;
            }
            if (sys.teams.get(p).equals(sys.teams.get(Shooter))) {
                e.setCancelled(true);
            }
        } else if (e.getDamager() instanceof Player) {
            Player Damager = (Player) e.getDamager();
            if (sys.teams.get(Damager).equals(CTESystem.TEAM.SPEC) || sys.teams.get(Damager).equals(CTESystem.TEAM.DEFAULT)) {
                e.setCancelled(true);
                return;
            }
            if (sys.teams.get(p).equals(sys.teams.get(Damager))) {
                e.setCancelled(true);
            }
        }
    }
}
