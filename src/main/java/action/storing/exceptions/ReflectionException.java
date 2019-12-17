package main.java.action.storing.exceptions;

import main.java.migration.field.FieldDescription;

public class ReflectionException extends Exception {
	String msg;
	public ReflectionException(Exception e) {
		// TODO Auto-generated constructor stub
	}
	
	public ReflectionException(Object item, FieldDescription desc) {
		msg = String.format("unable to access field: %s in object of %s.", desc.getFieldName(), item.getClass());
	}
	
	public ReflectionException(Object item, String desc) {
		msg = String.format("unable to access field: %s in object of %s.", desc, item.getClass());
	}

	public ReflectionException(FieldDescription fieldDescription) {
		msg = String.format("unable to access field: %s in object of %s.", fieldDescription.getFieldName(), fieldDescription.getOwner());
	}

	@Override
	public String getMessage() {
		return msg;
	}
	
}
