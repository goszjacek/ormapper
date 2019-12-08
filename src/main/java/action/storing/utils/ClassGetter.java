package main.java.action.storing.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import main.java.migration.field.FieldDescription;
import main.java.migration.field.FieldType;
import main.java.utils.MethodNameConverter;

public class ClassGetter {
	public static String get(Object item, FieldDescription desc) {
		
		try {
			Method method = item.getClass().getDeclaredMethod(MethodNameConverter.getGetter(desc.getFieldName()));
			return method.invoke(item).toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	
}
