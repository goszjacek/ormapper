package main.java.utils;

public class MethodNameConverter {
	public static String getGetter(String fieldName) {		
		return "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
	}

	public static String getSetter(String fieldName) {
		return "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
	}
}
