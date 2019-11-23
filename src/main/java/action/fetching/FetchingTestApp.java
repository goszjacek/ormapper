package main.java.action.fetching;

import java.io.File;
import java.util.List;

import main.java.database.Connector;
import main.java.migration.MappingController;
import main.java.src_class.Student;

public class FetchingTestApp {

	public static void main(String[] args) {
		Connector.establishConnection();
		MappingController mappingController = new MappingController();
		
		Connector.establishConnection();
		mappingController.readConfiguration(new File("./input.xml"));
		
		FetchingController fetchingController = mappingController.getFetchingController();
		List<Student> students = fetchingController.selectAll(Student.class);
		for(Student s : students) {
			System.out.println(s);
		}
		
		
	}

}
