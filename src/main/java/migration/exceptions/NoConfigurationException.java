package main.java.migration.exceptions;

public class NoConfigurationException extends Exception {
	String msg;
	public NoConfigurationException(String cls) {
		this.msg = "no configuration for " + cls;
	}
	@Override
	public String getMessage() {
		return msg; 
	}

}
