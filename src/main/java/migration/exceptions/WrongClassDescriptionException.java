package main.java.migration.exceptions;

import main.java.action.storing.exceptions.ReflectionException;

public class WrongClassDescriptionException extends Exception{
	String msg; 
	Class wrongDescribedClass;
	Exception e;
	public WrongClassDescriptionException(Exception e) {
	}

	public WrongClassDescriptionException(Class<?> cls) {
		this.wrongDescribedClass = cls;
	}

	public WrongClassDescriptionException(Class<? extends Object> class1, ReflectionException e) {
		this.wrongDescribedClass = class1;
		this.e = e;
		
	}

	@Override
	public String getMessage() {
		if(this.wrongDescribedClass != null)
			msg = "Class <" + wrongDescribedClass.getSimpleName() + "> is wrong described. ";
		else
			msg = "Some class is wrong described";
		if(e != null)
			msg += "Exception due to: " + e.getMessage(); 
		return msg;
	}
	
	
}
