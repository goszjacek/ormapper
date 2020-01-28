package main.java.migration.exceptions;

import main.java.default_exceptions.DueToException;

public class WrongFieldException extends DueToException {

	public WrongFieldException(Exception e) {
	}

	public WrongFieldException() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String name() {
		return "No field exception";
	}

}
