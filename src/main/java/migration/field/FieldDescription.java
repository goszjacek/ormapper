package main.java.migration.field;

import java.lang.reflect.Method;

import main.java.action.storing.exceptions.ReflectionException;
import main.java.migration.MappedClassDescription;
import main.java.utils.MethodNameConverter;

public class FieldDescription {
	String fieldName, columnName;
	FieldType fieldType;
	Class<?> classType;
	Class<?> owner;
	public FieldDescription(String fieldName, String columnName, FieldType fieldType, Class cls, Class owner) {
		super();
		this.fieldName = fieldName;
		this.columnName = columnName;
		this.fieldType = fieldType;
		this.classType = cls;
		this.owner = owner;
	}
	
	public static FieldType getFieldType(String s) {
		if(s.equals("text")) {
			return FieldType.STRING;
		}
		if(s.equals("int")) {
			return FieldType.INT;
		}
		return null;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getColumnName() {
		return columnName;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public Class<?> getClassType() {
		return classType;
	}
	

	public Class<?> getOwner() {
		return owner;
	}

	@Override
	public String toString() {
		return "FieldDescription [fieldName=" + fieldName + ", columnName=" + columnName + ", fieldType=" + fieldType
				+ ", classType=" + classType + "]";
	}

	public Method getSetter(MappedClassDescription mcd) throws ReflectionException {
		// TODO Auto-generated method stub
		try {
			return mcd.getClassType().getMethod(MethodNameConverter.getSetter(this.fieldName), this.classType);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new ReflectionException(this);
		}
	}
	
	
	
}
