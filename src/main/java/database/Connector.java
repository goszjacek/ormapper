package main.java.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
	private static final String url = "jdbc:sqlite:D:\\E\\software_engineering\\workspace\\ormapper\\database.db";
	private static final String DRIVER = "org.sqlite.JDBC";
	
	
	public static void establishConnection() {
		try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Brak sterownika JDBC");
            e.printStackTrace();
        }
		Connection conn = null;
	    try {
	        // create a connection to the database
	        conn = DriverManager.getConnection(url);
	        
	        System.out.println("Connection to SQLite has been established.");
	        
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    } finally {
	        try {
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }
	    }
	}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url);
	}
}
