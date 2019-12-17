package main.java.action.storing
;

import java.io.File;

import main.java.action.storing.exceptions.StoringException;
import main.java.database.Connector;
import main.java.migration.MappingController;
import main.java.migration.exceptions.ParsingException;
import user.classes.Laptop;
import user.classes.Student;

public class StoringTestApp {

	public static void main(String[] args) {
		Connector.establishConnection();
		MappingController mappingController = new MappingController();
		try {
			mappingController.readConfiguration(new File("./src/user/resource/input.xml"));
		} catch (ParsingException e) {
			System.err.println("Configurations files are wrong. Check other messages. ");
			e.getMessage();
			e.printStackTrace();
			return;
		}
		Student s = new Student();
		s.setBirthDate("1.1.1999");
		s.setFirstName("Andreas");
		s.setLastName("Endstein");
		
		StoringController sc = mappingController.getStoringController();
		
		
		Laptop l = new Laptop();
		l.setLaptopCategoryId(1);
		l.setModel("Dell Latitude E330");
		try {
			sc.store(l);
		} catch (StoringException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("L: " + l.getLaptopId());
		s.setLaptop(l);
		try {
			sc.store(s);
		} catch (StoringException e) {
			e.printStackTrace();
		}
		System.out.println("S: " + s.getStudentId());
	}

}
