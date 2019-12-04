package main.java.action.storing;

import java.io.File;

import main.java.action.storing.exceptions.StoringException;
import main.java.database.Connector;
import main.java.migration.MappingController;
import main.java.migration.exceptions.ParsingException;
import main.java.src_class.Student;

public class StoringTestApp {

	public static void main(String[] args) {
		Connector.establishConnection();
		MappingController mappingController = new MappingController();
		try {
			mappingController.readConfiguration(new File("./input.xml"));
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
		try {
			sc.store(s);
		} catch (StoringException e) {
			e.printStackTrace();
		}
		

	}

}
