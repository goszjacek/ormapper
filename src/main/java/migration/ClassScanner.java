package main.java.migration;

import java.lang.reflect.Field;

public class ClassScanner {
	public static MappedClassDescription scanClass(Class inputClass) {
		
        String simpleName = inputClass.getSimpleName();
        Field[] fields = inputClass.getDeclaredFields();
        System.out.println("len:"+fields.length);
        for(Field field : fields ) {
      	  System.out.println(field.getName());
      	  String fieldName = field.getName();
      	  if (fieldName.contains("PId")) {
      		  System.out.println("Primary key is: "+ fieldName);
      	  }
//      	  System.out.println("Type is: " + TypesConverter.convertTypeNameJavaToSQLLite(field.getType().getTypeName()));
      	  
        }
		
		return null;
		
	}
}
