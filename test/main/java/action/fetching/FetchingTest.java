package main.java.action.fetching;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import main.java.database.Connector;
import main.java.migration.MappingController;
import main.java.migration.exceptions.ParsingException;
import user.classes.Laptop;
import user.classes.Student;

class FetchingTest {
	FetchingController fetchingController;
	Method createBasicQueryMethod;
	@BeforeEach
	void setUp() throws Exception {
		System.out.println("*** TEST ***");
		Connector.establishConnection();
		MappingController mappingController = new MappingController();
		mappingController.readConfiguration(new File("src/user/resource/input.xml"));
		fetchingController = mappingController.getFetchingController();
		createBasicQueryMethod = FetchingController.class.getDeclaredMethod("createBasicQueryForClass", Class.class);
		createBasicQueryMethod.setAccessible(true);	
	}
	
	
	
	@Test
	void test() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String answer = (String) createBasicQueryMethod.invoke(fetchingController, Student.class);
		assertEquals("SELECT student_id, first_name, last_name, laptop_id, birth_date, locker_id FROM Student", answer);
		
	}
	
	@Test
	void laptopSQL() throws Exception {
		assertEquals("SELECT laptop_category_id, laptop_id, model FROM Laptop",
				(String) createBasicQueryMethod.invoke(fetchingController, Laptop.class));	
		
	}
	
	
	@Test
	void getLaptopsTest() {
		List<Laptop> laptops = fetchingController.selectAll(Laptop.class);
		System.out.println(laptops);
	}
	
	@Test
	void getStudentsTest() {
		List<Student> students = fetchingController.selectAll(Student.class);
		System.out.println(students);
	}

}
