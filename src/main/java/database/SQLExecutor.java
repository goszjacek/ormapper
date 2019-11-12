package main.java.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLExecutor {
	public static void executeSQL(String sql) {
		try (Connection conn = Connector.getConnection();
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
}
