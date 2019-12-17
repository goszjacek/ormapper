package main.java.action.storing.exceptions;

import main.java.migration.exceptions.WrongClassDescriptionException;

public class StoringException extends Exception {
	Throwable e;
	public StoringException(Throwable e) {
		this.e = e;
	}

	@Override
	public String getMessage() {
		String msg = "The storing operation failed";
		if(e != null) {
			msg += " due to: " + e.getMessage(); 
		}
		return msg;
	}

	
}
