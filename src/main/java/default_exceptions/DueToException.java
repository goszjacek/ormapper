package main.java.default_exceptions;

public abstract class DueToException extends Exception {
	protected String msg;
	public DueToException(Exception e) {
		msg = String.format("%s due to: %s", name(), e.getMessage());
	}
	public DueToException() {
		// TODO Auto-generated constructor stub
	}
	protected abstract String name();
}
