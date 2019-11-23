package main.java.action.fetching;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;


import main.java.database.Connector;
import main.java.migration.Configuration;
import main.java.migration.MappedClassDescription;
import main.java.migration.field.FieldDescription;
import main.java.migration.field.FieldType;
import main.java.utils.MethodNameConverter;

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
		String sql = "SELECT ";
		String clsName = cls.getSimpleName();
		MappedClassDescription mcd = this.configuration.getDescription(clsName);
		SortedMap<String, FieldDescription> fields = mcd.getFields();
		Set<String> fieldSet = (Set<String>) mcd.getFields().keySet();
		Iterator<String> it = fieldSet.iterator();
		while(it.hasNext()) {
			String fieldString = it.next();
			sql += fields.get(fieldString).getColumnName() + ", "; 
		}
		sql = sql.substring(0, sql.length()-2);
		sql += " FROM " + mcd.getTableName();
		System.out.println(sql);
		
		List<T> resultList = new ArrayList<T>();
		try (Connection conn = Connector.getConnection();
	             Statement stmt  = conn.createStatement();
	             ResultSet rs    = stmt.executeQuery(sql)){
	            
            // loop through the result set
            while (rs.next()) {
            	resultList.add(fillObject(rs, mcd));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		
		return resultList;
	}
	
	private Object getVar(ResultSet rs, String name, FieldType type) throws SQLException {
		if(type == FieldType.INT) {
			return rs.getInt(name);
		}
		if(type == FieldType.STRING) {
			return rs.getString(name);
		}
		return null;
	}
	
	private <T> T fillObject(ResultSet rs, MappedClassDescription mcd) {
		Class<T> mappedClass =  mcd.getClassType();
		Constructor<T> constructor;
		T item;
		
		try {
			constructor = mappedClass.getConstructor();	
		
		}catch (NoSuchMethodException | SecurityException e) {
			System.err.println("Wrong class configuration or no empty constuctor defined.");
			e.printStackTrace();
			return null;
		}
		try {
			item = constructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			System.err.println("Unable to run constructor. Does constuctor without arguments exists?");
			e.printStackTrace();
			return null;
		}
		
		SortedMap<String, FieldDescription> fields = mcd.getFields();
		Set<String> keys = fields.keySet();
		Iterator<String> it = keys.iterator();
		
		try {
			while(it.hasNext()) {
				FieldDescription field = (FieldDescription) fields.get(it.next());
				//	run setter for the field and assign the value

				Method setter = mappedClass.getMethod(MethodNameConverter.getSetter(field.getFieldName()), field.getClassType());
				
				setter.invoke(item, getVar(rs, field.getColumnName(), field.getFieldType()));
	
			}
		} catch (NoSuchMethodException | SecurityException e) {
			System.err.println("Check if all setter for the fields are defined with default convetions.");
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SQLException e) {
			System.err.println("Unable to invoke setter for the field. Check privided column name and filed type. ");
			e.printStackTrace();
			return null;
		} 
		
		return item;
	}
	
	
}
