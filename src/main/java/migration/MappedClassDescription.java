package main.java.migration;

import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import main.java.migration.field.FieldDescription;
import main.java.migration.onetoone.OneToOneDescription;

public class MappedClassDescription {
	private Class classType;
	private String className, tableName;
	private Map<String,FieldDescription> fields;
	private Map<String,OneToOneDescription> oneToOnes;
	private String path, desc;
	FieldDescription id;
	
	public MappedClassDescription(String descriptionLocation, String path) {
		this.desc = descriptionLocation;
		this.path = path;
	}
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
	public void setFields(Map<String, FieldDescription> fields) {
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
	public Map<String, OneToOneDescription> getOneToOnes() {
		return oneToOnes;
	}
	public void setOneToOnes(Map<String, OneToOneDescription> oneToOnes) {
		this.oneToOnes = oneToOnes;
	}

	
}
