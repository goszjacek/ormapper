package main.java.migration.field;

public class FieldDescription {
	String fieldName, columnName;
	FieldType fieldType;
	Class<?> classType;
	public FieldDescription(String fieldName, String columnName, FieldType fieldType) {
		super();
		this.fieldName = fieldName;
		this.columnName = columnName;
		this.fieldType = fieldType;
		
		if (fieldType==FieldType.INT)
			this.classType = Integer.class;
		if (fieldType==FieldType.STRING)
			this.classType = String.class;
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
	
	
	
}
