package main.java.migration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.migration.exceptions.ParsingException;
import user.classes.Student;

class MappedClassDescriptionTest {

	static String descriptionPath = "./test/user/resource/description/sample_file_description.xml";
	static String configurationPath = "./test/user/resource/input.xml";
	
	@BeforeAll
	static void assertResources() {
		assertTrue(new File(configurationPath).isFile());
		assertTrue(new File(descriptionPath).isFile());
	}
	
	@BeforeEach
	void prepareResources() {
		
		
	}
	
	@Test
	void parseMainFileTest() {
		Configuration conf = null;
		try {
			Configuration.parseMainFile(new File(configurationPath));
			
			// test content of configuration
			
		} catch (ParsingException e) {
			assertTrue(false);
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * It tests mapping of a class Student to do MappedClassDescription
	 */
//	@Test
//	void readDescriptionFileTest() {
//		
//		
//		try {
//			Method descriptionReader = ConfigurationFileReader.class.getDeclaredMethod("readDescriptionFile", MappedClassDescription.class);
//			descriptionReader.setAccessible(true);
//			descriptionReader.invoke(null, mcd);
//			assertSame(Student.class, mcd.getClassType());
//		} catch (NoSuchMethodException | SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ReflectiveOperationException | RuntimeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}

}
