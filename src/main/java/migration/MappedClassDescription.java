package main.java.migration;

import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import main.java.migration.field.FieldDescription;

public class MappedClassDescription {
	private Class classType;
	private String className, tableName;
	private SortedMap<String,FieldDescription> fields;
	private String path, desc;
	FieldDescription id;
	public FieldDescription getId() {
		return id;
	}
	public void setId(FieldDescription id) {
		this.id = id;
	}
	public Class getClassType() {
		return classType;
	}
	public void setClassType(Class<?> classType) {
		this.classType = classType;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String src) {
		this.className = src;
	}
	public Map<String, FieldDescription> getFields() {
		return fields;
	}
	public void setFields(SortedMap<String, FieldDescription> fields) {
		this.fields = fields;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public FieldDescription getField(String fieldName) {
		return fields.get(fieldName);
	}
	
	@Override
	public String toString(){
		String s = className + "\n";
		for (Entry<String, FieldDescription> entry : fields.entrySet())  
		{
			s += entry.getValue().getColumnName() + "\n" + entry.getValue().getFieldName() + "\n";
		}
		return s;
			
		
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	
}
