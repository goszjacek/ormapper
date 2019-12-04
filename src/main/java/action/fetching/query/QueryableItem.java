package main.java.action.fetching.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.java.action.fetching.exceptions.WrongQueryException;
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
		String updatedSql = this.sql + " WHERE " + expression;
		System.out.println("Executing from querable: " + sql);
		return executeSqlAndFillObjects(updatedSql);
	}
	/**
	 * Default mode is column
	 * @param field
	 * @param expression
	 * @param value
	 * @return
	 */
	@Override
	public List<T> where(String field, String relation, String value) {
		String expression = field + " " + relation + " " + value;
		String updatedSql = this.sql + " WHERE " + expression;
		return executeSqlAndFillObjects(updatedSql);
	}
	
	@Override
	public List<T> where(String field, String relation, String value, QueryMode mode) {
		if(mode == QueryMode.COLUMN) {
			return this.where(field,relation,value);
		}else {
			String columnName = mcd.getField(field).getColumnName();
			if(columnName == null) {
				System.err.println("Probably such field doesn't exist");
				return null;
			}				
			return this.where(columnName, relation, value);
		}
	}
	
	public T id(int id) {
		String updatedSql = this.sql + " WHERE " + mcd.getId().getColumnName() + " = " + id;
		List<T> items =  executeSqlAndFillObjects(updatedSql);
		return items.get(0);
	}
	
	/**
	 * Help method. It executes the sql and returns a list of filled objects. 
	 * @param sql
	 * @return
	 */
	private List<T> executeSqlAndFillObjects(String sql){
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
            return null;
        }
		return resultList;
	}
	
	
	



}
