package main.java.action.fetching;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import main.java.database.Connector;
import main.java.migration.MappedClassDescription;

public class FetchingController {
	public static void selectAll(){
        String sql = "SELECT student_id, first_name, last_name, birth_date FROM Student";
        
        try (Connection conn = Connector.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("student_id") +  "\t" + 
                                   rs.getString("first_name") + "\t" +
                                   rs.getString("last_name") + "\t" +
                                   rs.getString("birth_date"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	/**
	 * select all elements, stored in a database for a class. 
	 * select the elements in a ordered way, and get the elements in the same order from every raw
	 * @param <T>
	 * @param cls
	 */
	public static <T> List<T> selectAll(Class<?> cls) {
		
		String sql = "SELECT ";
		MappedClassDescription mcd;
//		for(String columnName : src)
		
		return null;
	}
}
