package main.java.action.fetching.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.java.action.fetching.FetchingController;
import main.java.action.fetching.exceptions.WrongQueryException;
import main.java.action.fetching.utils.ClassFiller;
import main.java.database.Connector;
import main.java.migration.MappedClassDescription;
import main.java.migration.field.FieldDescription;

public class QueryableItem<T> implements Queryable<T>{
	String sql; 
	MappedClassDescription mcd;
	FetchingController fc;
	
	
	public QueryableItem(String sql, MappedClassDescription mcd, FetchingController fc) {
		super();
		this.sql = sql;
		this.mcd = mcd;
		this.fc = fc;
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
	public List<T> where(String field, String relation, String value, QueryMode mode) throws WrongQueryException {
		if(mode == QueryMode.COLUMN) {
			return this.where(field,relation,value);
		}else {
			FieldDescription fieldDesc = mcd.getField(field);
			if(fieldDesc == null) {
				System.err.println("Probably such field doesn't exist");
				throw new WrongQueryException();
			}				
			return this.where(fieldDesc.getColumnName(), relation, value);
		}
	}
	
	public T id(int id) throws WrongQueryException {
		String updatedSql = this.sql + " WHERE " + mcd.getId().getColumnName() + " = " + id;
		List<T> items =  executeSqlAndFillObjects(updatedSql);
		if(items.size()==0)
			throw new WrongQueryException();
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
            	resultList.add(ClassFiller.fillObject(rs, mcd, fc));
            }
        } catch (SQLException e) {
        	System.err.println("Unable to connect to database or wrong SQL statement. ");
            System.out.println(e.getMessage());
            return null;
        }
		return resultList;
	}
	
	
	



}
