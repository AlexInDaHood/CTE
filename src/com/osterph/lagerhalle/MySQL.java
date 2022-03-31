package com.osterph.lagerhalle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.osterph.cte.CTE;

public class MySQL {

    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private Connection con;

    public MySQL(String host, String database, String username, String password) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    private void connect() {
        try {
            int port = 3306;
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database +"?user="+username+"&password="+password + "&characterEncoding=latin1");
            System.out.println("[MySQL] Connected");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[MySQL] disconnected");
        }
    }

    private void disconnect() {
        try {
            if (this.con != null) {
                this.con.close();
                System.out.println("[MySQL] disconnected");
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
            e.printStackTrace();
        }
        disconnect();
    }

    public ResultSet query(String query) {
        connect();
        try {
            PreparedStatement ps = con.prepareStatement(query);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return null;
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
