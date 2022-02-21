package com.osterph.lagerhalle;

import com.osterph.cte.CTE;
import org.bukkit.Bukkit;

import java.sql.*;

public class MySQL {

    private static String host;
    private static String database;
    private static final int port = 3306;
    private static String username;
    private static String password;
    private static Connection con;

    public MySQL(String host, String database, String username, String password) {
        MySQL.host = host;
        MySQL.database = database;
        MySQL.username = username;
        MySQL.password = password;
    }

    private static void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database +"?user="+username+"&password="+password + "&characterEncoding=latin1");
            java.lang.System.out.println("[MySQL] Connected");
        } catch (SQLException e) {
            e.printStackTrace();
            java.lang.System.out.println("[MySQL] disconnected");
        }
    }

    private static void disconnect() {
        try {
            if (con != null) {
                con.close();
                java.lang.System.out.println("[MySQL] disconnected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(String query) {
        connect();
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            java.lang.System.out.println(e);
        }
        disconnect();
    }

    public ResultSet query(String query) {
        connect();
        ResultSet rs = null;
        try {
            PreparedStatement ps = con.prepareStatement(query);
            return ps.executeQuery();
        } catch (SQLException e) {

            java.lang.System.out.println(e);
        }
        disconnect();
        return rs;
    }

    public static void setDatabase(String Tabelle, String SpalteKategorie, String SpalteInhalt, String WertKategorie, String WertInhalt) {
        try {
            update("INSERT INTO " + Tabelle + " (" + SpalteKategorie + "," + WertKategorie + ") VALUES ('" + SpalteInhalt + "','" + WertInhalt + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDatabase(String Tabelle, String SpalteKategorie, String SpalteInhalt, String WertKategorie) {
        ResultSet rs = CTE.mysql.query("SELECT " + WertKategorie + " FROM " + Tabelle + " WHERE " + SpalteKategorie + "='" + SpalteInhalt + "'");
        String out = null;

        try{
            while (rs.next()){
                out = rs.getString(1);
            }
        } catch (SQLException exception){
            exception.printStackTrace();
        }

        return (out != null) ? out : "null";
    }
}
