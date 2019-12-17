package main.java.action.storing;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Map.Entry;

import main.java.action.exception.DatabaseException;
import main.java.action.storing.exceptions.ReflectionException;
import main.java.action.storing.exceptions.StoringException;
import main.java.action.storing.utils.ClassGetter;
import main.java.database.Connector;
import main.java.migration.Configuration;
import main.java.migration.MappedClassDescription;
import main.java.migration.exceptions.NoConfigurationException;
import main.java.migration.exceptions.SQLGenerationException;
import main.java.migration.exceptions.WrongClassDescriptionException;
import main.java.migration.field.FieldDescription;
import main.java.migration.field.FieldType;
import main.java.migration.onetoone.OneToOneDescription;
import main.java.utils.MethodNameConverter;

public class StoringController {
	Configuration configuration;
	
	public StoringController(Configuration configuration) {
		super();
		this.configuration = configuration;
	}
	/**
	 * Every object should have at least one field to store
	 * @param item
	 * @throws StoringException
	 */
	public void store(Object item) throws StoringException {
		
		try {
			String sql = generateInsertSQL(item);
			try (Connection conn = Connector.getConnection();
		             Statement stmt  = conn.createStatement();){
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				System.err.println("Wrong statement: " + sql);
				throw new StoringException(e);
			}
			
			
		} catch (WrongClassDescriptionException e) {
			System.err.println(e.getMessage());
			throw new StoringException(e);
		}
		
		// retrieve id of stored element and assign in item
		
		try {
			var mcd = configuration.getSafeDescription(item.getClass().getSimpleName());
			Integer idOfInserted = getInsertedId(item, mcd);
			System.out.println(String.format("for %s set %s", mcd.getClassType().getName(), idOfInserted));
			Method toSetId = mcd.getId().getSetter(mcd);
			toSetId.invoke(item, idOfInserted);
		} catch (DatabaseException e) {

			e.printStackTrace();
		} catch (NoConfigurationException e) {
			e.printStackTrace();
			throw new StoringException(e);
			
		} catch (ReflectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	catch (Exception e) {
			System.err.println("Unable to set it. Object stored without id");
			
		}
		
		
		
	}
	
	private Integer getInsertedId(Object item, MappedClassDescription mcd) throws DatabaseException {
		try {
			String sql = generateGetInsertedId(item, mcd);
			
			try (Connection conn = Connector.getConnection();
		             Statement stmt  = conn.createStatement();
					ResultSet rs    = stmt.executeQuery(sql);){
				return Integer.parseInt(rs.getString("last"));
			} catch (SQLException e) {
				System.err.println("Wrong statement: " + sql);
				throw new SQLGenerationException(sql);
			}
		} catch (SQLGenerationException e) {
			throw new DatabaseException(e);
		} 
		
	}
	
	private String generateGetInsertedId(Object item, MappedClassDescription mcd) throws SQLGenerationException {
		return "SELECT MAX(" + mcd.getId().getColumnName() + ") AS LAST FROM " + mcd.getTableName(); 
	}
	
	
	private String generateInsertSQL(Object item) throws WrongClassDescriptionException {
		if(configuration.getDescription(item.getClass().getSimpleName())!=null) {
			var mcd = configuration.getDescription(item.getClass().getSimpleName());
			
            String sql = "insert into " + mcd.getTableName() + "(";
            //insert fields
			Map<String, FieldDescription> fields = mcd.getFields();
			boolean any = false;
			for(Map.Entry<String, FieldDescription> entry : fields.entrySet()){
				if(entry.getValue() != mcd.getId()) {
					sql+=entry.getValue().getColumnName() + ", ";
					any = true;
				}
					
			}
			if(any) sql=sql.substring(0,sql.length()-2);
			//insert oneToOnes
			sql += ") values (";
			any=false;
			for(Map.Entry<String, FieldDescription> entry : fields.entrySet()){
				// get value of item using information from field, convert it to formatted string and store in the database
				FieldDescription field = entry.getValue();
				if(field != mcd.getId()) {
					try {
						if(field.getFieldType().equals(FieldType.ONETOONE)) {
							Integer id = ClassGetter.getId(item, entry.getValue(), configuration);
							sql += "'" + id + "', ";
							any = true;
						}else {
							sql += "'" + ClassGetter.get(item, entry.getValue()) + "', ";
							any = true;
						}
					} catch (ReflectionException e) {
						e.printStackTrace();
						throw new WrongClassDescriptionException(item.getClass(), e);
					}
					
				}
			}
			if(any) 
				sql=sql.substring(0,sql.length()-2);
			sql += ")";
			return sql;
		}else {
			throw new WrongClassDescriptionException(item.getClass());
		}
		
	}
	
	
	
}
