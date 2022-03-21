package com.osterph.manager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.osterph.cte.CTE;
import com.osterph.cte.CTESystem;
import com.osterph.cte.CTESystem.GAMESTATE;
import com.osterph.dev.StaffManager;

public class ScoreboardManager {

    private static Scoreboard board;

    private static Team user;
    private static Team spectator;
    private static Team red;
    private static Team blue;

    private static CTESystem sys = CTE.INSTANCE.getSystem();

    public static void refreshBoard() {
        for (Player all: Bukkit.getOnlinePlayers()) {
            refreshBoard(all);
        }
    }

    public static void refreshBoard(Player p) {
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        defineTeams();
        sys.red.clear();
        sys.blue.clear();

        for(Player all : Bukkit.getOnlinePlayers()) {
            sys.teams.putIfAbsent(all, CTESystem.TEAM.DEFAULT);
            CTESystem.TEAM team = sys.teams.get(all);
            switch (team) {
                case RED:
                    all.setPlayerListName("§8[§cR§8] §c" + all.getName());
                    if (!all.getGameMode().equals(GameMode.CREATIVE)) all.setGameMode(GameMode.SURVIVAL);
                    red.addEntry(all.getName());
                    sys.red.add(all);
                    all.removePotionEffect(PotionEffectType.INVISIBILITY);
                    for (Player o : Bukkit.getOnlinePlayers()) {
                        o.showPlayer(all);
                    }
                    break;
                case BLUE:
                    all.setPlayerListName("§8[§9B§8] §9" + all.getName());
                    if (!all.getGameMode().equals(GameMode.CREATIVE)) all.setGameMode(GameMode.SURVIVAL);
                    blue.addEntry(all.getName());
                    sys.blue.add(all);
                    all.removePotionEffect(PotionEffectType.INVISIBILITY);
                    for (Player o : Bukkit.getOnlinePlayers()) {
                        o.showPlayer(all);
                    }
                    break;
                case DEFAULT:
                    all.setPlayerListName("§8[§7W§8] §7" + all.getName());
                    if (!all.getGameMode().equals(GameMode.CREATIVE)) all.setGameMode(GameMode.ADVENTURE);
                    user.addEntry(all.getName());
                    all.removePotionEffect(PotionEffectType.INVISIBILITY);
                    for (Player o : Bukkit.getOnlinePlayers()) {
                        o.showPlayer(all);
                    }
                    break;
                case SPEC:
                    all.setPlayerListName("§8[§7§oS§8] §7§o" + all.getName());
                    spectator.addEntry(all.getName());
                    if (!all.getGameMode().equals(GameMode.CREATIVE)) all.setGameMode(GameMode.ADVENTURE);
                    all.removePotionEffect(PotionEffectType.INVISIBILITY);
                    all.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0, true));
                    all.setAllowFlight(true);
                    TablistManager.addFakePlayer(all);
                    for (Player o : Bukkit.getOnlinePlayers()) {
                        if (sys.teams.get(o).equals(CTESystem.TEAM.SPEC)) continue;
                        o.hidePlayer(all);
                    }
                    break;
            }
            if (new StaffManager(all).hasRoles()) all.setPlayerListName(all.getPlayerListName()+" §8["+new StaffManager(all).activeString()+"§8]");
        }

        if (sys.gamestate.equals(GAMESTATE.RUNNING) || sys.gamestate.equals(GAMESTATE.ENDING)) {
            Objective o = board.getObjective(p.getName());
            try {
                if (o == null) o = board.registerNewObjective(p.getName(), "");
            } catch (Exception ignored) {}

            o.setDisplayName("§8» §6§lCTE §8«");
            o.setDisplaySlot(DisplaySlot.SIDEBAR);

            o.getScore("§1").setScore(6);
            o.getScore("§9BLAUES TEAM§8: " + EggStatus(TEAM.BLUE)).setScore(5);
            o.getScore("§cROTES TEAM§8: " + EggStatus(TEAM.RED)).setScore(4);
            o.getScore("§2").setScore(3);
            if (sys.teams.get(p).equals(CTESystem.TEAM.BLUE)||sys.teams.get(p).equals(CTESystem.TEAM.RED)) {
                o.getScore("§3PUNKTE§8: §7" + CTE.INSTANCE.getStatsManager().getPoints(p)).setScore(2);
                o.getScore("§bKILLS§8: §7" + CTE.INSTANCE.getStatsManager().getKills(p)).setScore(1);
            }
        } else {
            Objective o = board.getObjective(p.getName());
            try {
                if (o == null) o = board.registerNewObjective(p.getName(), "");
            } catch (Exception ignored) {}

            o.setDisplayName("§8» §6§lCTE §8«");
            o.setDisplaySlot(DisplaySlot.SIDEBAR);

            o.getScore("§1").setScore(8);
            if (Bukkit.getOnlinePlayers().size() >= 4) {
                o.getScore("§eWarte auf "+(4-Bukkit.getOnlinePlayers().size())+" Spieler...").setScore(7);
            } else {
                o.getScore("§e"+Bukkit.getOnlinePlayers().size()+"§7/§e"+sys.maxPlayers+" Spieler").setScore(7);
            }
            o.getScore("§2").setScore(6);
            o.getScore("§9BLAUES TEAM§8: §e" + sys.blue.size()+"§7/§e"+sys.maxPlayers/2).setScore(5);
            o.getScore("§cROTES TEAM§8: §e" + sys.red.size()+"§7/§e"+sys.maxPlayers/2).setScore(4);
            o.getScore("§3").setScore(3);
            o.getScore("§3GES. PUNKTE").setScore(2);
            //o.getScore("§8➥ §7"+CTE.mysql.getDatabase("PLAYERPOINTS", "UUID", p.getUniqueId().toString(), "POINTS")).setScore(1);
        }

        p.setScoreboard(board);

    }

    private static String EggStatus(TEAM t) {
        switch (t) {
            case RED: {
                switch (sys.RED_EGG) {
                    case OKAY: return "§a✔";
                    case STOLEN: return "§6⚠";
                    case GONE: return "§4"+sys.red.size()+"§7/§4"+sys.startRED;
                }
            }
            case BLUE: {
                switch (sys.BLUE_EGG) {
                    case OKAY: return "§a✔";
                    case STOLEN: return "§6⚠";
                    case GONE: return "§4"+sys.blue.size()+"§7/§4"+sys.startBLUE;
                }
            }
        }
        return "§4ERROR";
    }

    private enum TEAM{
        RED, BLUE
    }

    private static void defineTeams() {

        try {
            user = board.registerNewTeam("00User");
        } catch (Exception kelp) {
            user = board.getTeam("00User");
        }
        user.setPrefix("§8[§7W§8] §7");

        try {
            blue = board.registerNewTeam("01Blue");
        } catch (Exception kelp) {
            blue = board.getTeam("01Blue");
        }
        blue.setPrefix("§8[§9B§8] §9");

        try {
            red = board.registerNewTeam("02Red");
        } catch (Exception kelp) {
            red = board.getTeam("02Red");
        }
        red.setPrefix("§8[§cR§8] §c");

        try {
            spectator = board.registerNewTeam("03Spectator");
        } catch (Exception kelp) {
            spectator = board.getTeam("03Spectator");
        }
        spectator.setPrefix("§7§o");
        spectator.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
        spectator.setCanSeeFriendlyInvisibles(true);
    }

}
