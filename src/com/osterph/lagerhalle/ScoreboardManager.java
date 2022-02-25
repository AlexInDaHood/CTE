package com.osterph.lagerhalle;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

    private static Scoreboard board;

    private static Team user;
    private static Team spectator;
    private static Team red;
    private static Team blue;

    public static void refreshBoard(Player p) {
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        defineTeams();


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
