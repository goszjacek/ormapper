package main.java.action.storing.utils;

import java.lang.reflect.Method;

import main.java.action.storing.exceptions.ReflectionException;
import main.java.migration.Configuration;
import main.java.migration.MappedClassDescription;
import main.java.migration.exceptions.WrongClassDescriptionException;
import main.java.migration.field.FieldDescription;
import main.java.utils.MethodNameConverter;

public class ClassGetter {
	public static String get(Object item, FieldDescription desc) throws ReflectionException  {
		
		try {
			Method method = item.getClass().getDeclaredMethod(MethodNameConverter.getGetter(desc.getFieldName()));
			return method.invoke(item).toString();	
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new ReflectionException(e);
		}
	}

	public static Integer getId(Object item, FieldDescription desc, Configuration configuration) throws ReflectionException, WrongClassDescriptionException {
		
		try {
			Method method = item.getClass().getDeclaredMethod(MethodNameConverter.getGetter(desc.getFieldName()));
			Object itemToGetId = method.invoke(item);
			
			var itemToGetIdDesc = configuration.getDescription(itemToGetId.getClass().getSimpleName());
			if(itemToGetIdDesc == null) {
				throw new WrongClassDescriptionException(itemToGetId.getClass());
			}
			System.out.println("Found description for : " + itemToGetIdDesc.getClassName());
			try {
				Method getIdMethod = itemToGetIdDesc.getClassType().getMethod(MethodNameConverter.getGetter(itemToGetIdDesc.getId().getFieldName()));
				Integer id = (Integer) getIdMethod.invoke(itemToGetId);				
				System.out.println("Returning id: " + id);
				return id;
			} catch (Exception e) {
				e.printStackTrace();
				throw new ReflectionException(Class.forName(itemToGetIdDesc.getClassType().getPackageName()),itemToGetIdDesc.getId());
			}
			
			
		}catch (WrongClassDescriptionException e) {
			throw e;
		} catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ReflectionException(item,desc);
		}
		
	}
	
	
}
