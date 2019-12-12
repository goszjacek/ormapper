package main.java.action.exception;

import main.java.default_exceptions.DueToException;

public class DatabaseException extends DueToException {

	public DatabaseException(Exception e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String name() {
		// TODO Auto-generated method stub
		return null;
	}

}
