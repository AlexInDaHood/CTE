package com.osterph.lagerhalle;

import com.osterph.cte.CTE;

import java.sql.*;

public class MySQL {

    private String host;
    private String database;
    private final int port = 3306;
    private String username;
    private String password;
    private Connection con;

    public MySQL(String host, String database, String username, String password) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    private void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database +"?user="+username+"&password="+password + "&characterEncoding=latin1");
            java.lang.System.out.println("[MySQL] Connected");
        } catch (SQLException e) {
            e.printStackTrace();
            java.lang.System.out.println("[MySQL] disconnected");
        }
    }

    private void disconnect() {
        try {
            if (this.con != null) {
                this.con.close();
                java.lang.System.out.println("[MySQL] disconnected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String query) {
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

    public void setDatabase(String Tabelle, String SpalteKategorie, String SpalteInhalt, String WertKategorie, Object WertInhalt) {
        try {
            this.update("INSERT INTO " + Tabelle + " (" + SpalteKategorie + "," + WertKategorie + ") VALUES ('" + SpalteInhalt + "','" + WertInhalt + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getDatabase(String Tabelle, String SpalteKategorie, String SpalteInhalt, String WertKategorie) {
        ResultSet rs = CTE.mysql.query("SELECT " + WertKategorie + " FROM " + Tabelle + " WHERE " + SpalteKategorie + "='" + SpalteInhalt + "'");
        Object out = null;

        try{
            while (rs.next()){
                out = rs.getString(1);
            }
        } catch (SQLException exception){
            exception.printStackTrace();
        }

        return (out != null) ? out : 0;
    }
}
