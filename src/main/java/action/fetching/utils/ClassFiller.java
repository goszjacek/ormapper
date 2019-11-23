package main.java.action.fetching.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;

import main.java.migration.MappedClassDescription;
import main.java.migration.field.FieldDescription;
import main.java.migration.field.FieldType;
import main.java.utils.MethodNameConverter;

public class ClassFiller {
	private static Object getVar(ResultSet rs, String name, FieldType type) throws SQLException {
		if(type == FieldType.INT) {
			return rs.getInt(name);
		}
		if(type == FieldType.STRING) {
			return rs.getString(name);
		}
		return null;
	}
	
	public  static <T> T fillObject(ResultSet rs, MappedClassDescription mcd) {
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
