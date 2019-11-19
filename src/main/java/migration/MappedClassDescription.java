package main.java.migration;

import java.util.Map;

import main.java.migration.field.FieldDescription;

public class MappedClassDescription {
	private Class classType;
	private String className;
	private Map<String,FieldDescription> fields;
	private String path, desc;
	public Class getClassType() {
		return classType;
	}
	public void setClassType(Class classType) {
		this.classType = classType;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
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
	
}
