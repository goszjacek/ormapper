package main.java.action.fetching;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.java.action.fetching.query.QueryableItem;
import main.java.action.fetching.utils.ClassFiller;
import main.java.database.Connector;
import main.java.migration.Configuration;
import main.java.migration.MappedClassDescription;
import main.java.migration.field.FieldDescription;

public class FetchingController {
	private Configuration configuration;
	
	
	
	public FetchingController(Configuration configuration) {
		super();
		this.configuration = configuration;
	}
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
	public <T> List<T> selectAll(Class<T> cls) {		
		MappedClassDescription mcd = this.configuration.getDescription(cls.getSimpleName());
		String sql = createBasicQueryForClass(cls);
		System.out.println("Craeted sql: "+ sql);
		
		List<T> resultList = new ArrayList<T>();
		try (Connection conn = Connector.getConnection();
	             Statement stmt  = conn.createStatement();
	             ResultSet rs    = stmt.executeQuery(sql)){
	            
            // loop through the result set
            while (rs.next()) {
            	T item = ClassFiller.fillObject(rs, mcd);
            	if(item != null){
            		resultList.add(item);
            	}
            }
        } catch (SQLException e) {
        	System.err.println("Unable to connect to database");
            System.out.println(e.getMessage());
        }
		
		return resultList;
	}
	
	public <T> QueryableItem<T> select(Class <T> cls){
		String sql = createBasicQueryForClass(cls);
		MappedClassDescription mcd = this.configuration.getDescription(cls.getSimpleName());
		System.out.println("Sql passed to queryable item: "+ sql);		
		
		return new QueryableItem<T>(sql, mcd);		
	}
	
	private <T> String createBasicQueryForClass(Class <T> cls){
		MappedClassDescription mcd = this.configuration.getDescription(cls.getSimpleName());
		String sql = "SELECT ";		
		Map<String, FieldDescription> fields = mcd.getFields();
		Set<String> fieldSet = (Set<String>) mcd.getFields().keySet();
		Iterator<String> it = fieldSet.iterator();
		while(it.hasNext()) {
			String fieldString = it.next();
			sql += fields.get(fieldString).getColumnName() + ", "; 
		}
		sql = sql.substring(0, sql.length()-2);
		sql += " FROM " + mcd.getTableName();
		return sql;
	}
	
	
	
	
}
