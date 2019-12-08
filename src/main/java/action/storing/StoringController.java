package main.java.action.storing;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Map.Entry;

import main.java.action.storing.exceptions.StoringException;
import main.java.action.storing.utils.ClassGetter;
import main.java.database.Connector;
import main.java.migration.Configuration;
import main.java.migration.MappedClassDescription;
import main.java.migration.exceptions.WrongClassDescriptionException;
import main.java.migration.field.FieldDescription;
import main.java.migration.onetoone.OneToOneDescription;

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
			throw new StoringException(e);
		}
	}
	
	private String generateInsertSQL(Object item) throws WrongClassDescriptionException {
		if(configuration.getDescription(item.getClass().getSimpleName())!=null) {
			var mcd = configuration.getDescription(item.getClass().getSimpleName());
			
            String sql = "insert into " + mcd.getTableName() + "(";
            //insert fields
			Map<String, FieldDescription> fields = mcd.getFields();
			for(Map.Entry<String, FieldDescription> entry : fields.entrySet()){
				if(entry.getValue() != mcd.getId())
					sql+=entry.getValue().getColumnName() + ", ";
			}
			sql=sql.substring(0,sql.length()-2);
			//insert oneToOnes
			sql += ") values (";
			for(Map.Entry<String, FieldDescription> entry : fields.entrySet()){
				// get value of item using information from field, convert it to formatted string and store in the database
				if(entry.getValue() != mcd.getId()) {
					sql += "'" + ClassGetter.get(item, entry.getValue()) + "', ";
				}
			}
			sql=sql.substring(0,sql.length()-2);
			sql += ")";
			return sql;
		}else {
			throw new WrongClassDescriptionException();

		}
		
	}
	
	
	
}
