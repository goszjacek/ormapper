package main.java.action.fetching;

import java.io.File;
import java.util.List;

import main.java.database.Connector;
import main.java.migration.MappingController;
import main.java.migration.exceptions.ParsingError;
import main.java.src_class.Student;

public class FetchingTestApp {

	public static void main(String[] args) {
		Connector.establishConnection();
		MappingController mappingController = new MappingController();
		Connector.establishConnection();
		try {
			mappingController.readConfiguration(new File("./input.xml"));
		} catch (ParsingError e) {
			System.err.println("Configurations files are wrong. Check other messages. ");
			e.printStackTrace();
			return;
		}
		FetchingController fetchingController = mappingController.getFetchingController();
		List<Student> students = fetchingController.selectAll(Student.class);
		for(Student s : students) {
			System.out.println(s);
		}
		List<Student> filteredStudents =  fetchingController.select(Student.class).where("first_name = 'Andrzej'");
		for(Student s : filteredStudents) {
			System.out.println(s);
		}
		
		
		
		
		
	}

}
