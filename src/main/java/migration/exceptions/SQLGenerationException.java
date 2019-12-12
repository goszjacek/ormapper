package main.java.migration.exceptions;

import main.java.default_exceptions.DueToException;

public class SQLGenerationException extends DueToException{
	
	public SQLGenerationException(Exception e) {
		super(e);
	}

	public SQLGenerationException(String sql) {
		super();
		msg += "Wrong statement: " + sql;
	}

	@Override
	protected String name() {
		return "SQLGenerationException";
	}

	
	
}
