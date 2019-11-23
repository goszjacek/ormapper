package main.java.action.fetching.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.java.action.fetching.utils.ClassFiller;
import main.java.database.Connector;
import main.java.migration.MappedClassDescription;

public class QueryableItem<T> implements Queryable<T>{
	String sql; 
	MappedClassDescription mcd;
	
	
	
	public QueryableItem(String sql, MappedClassDescription mcd) {
		super();
		this.sql = sql;
		this.mcd = mcd;
	}



	@Override
	public List<T> where(String expression) {
		this.sql += " WHERE " + expression;
		List<T> resultList = new ArrayList<T>();
		System.out.println("Executing from querable: " + sql);
		try (Connection conn = Connector.getConnection();
	             Statement stmt  = conn.createStatement();
	             ResultSet rs    = stmt.executeQuery(sql)){
	            
            // loop through the result set
            while (rs.next()) {
            	resultList.add(ClassFiller.fillObject(rs, mcd));
            }
        } catch (SQLException e) {
        	System.err.println("Unable to connect to database or wrong SQL statement. ");
            System.out.println(e.getMessage());
        }
		return resultList;
	}



}
