package com.osterph.lagerhalle;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

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

    private static void defineTeams() {
        user = board.registerNewTeam("00User");
        user.setPrefix("§7");

        blue = board.registerNewTeam("01Blue");
        blue.setPrefix("§9");

        red = board.registerNewTeam("02Red");
        red.setPrefix("§c");

        spectator = board.registerNewTeam("03Spectator");
        spectator.setPrefix("§7§o");
        spectator.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
        spectator.setCanSeeFriendlyInvisibles(true);

    }

}
