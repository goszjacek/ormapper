package main.java.action.fetching;

import java.io.File;
import java.util.List;

import main.java.action.fetching.exceptions.WrongQueryException;
import main.java.action.fetching.query.QueryMode;
import main.java.database.Connector;
import main.java.migration.MappingController;
import main.java.migration.exceptions.ParsingException;
import user.classes.Student;

public class FetchingTestApp {

	public static void main(String[] args) {
		Connector.establishConnection();
		MappingController mappingController = new MappingController();
		try {
			mappingController.readConfiguration(new File("src/user/resource/input.xml"));
		} catch (ParsingException e) {
			System.err.println("Configurations files are wrong. Check other messages. ");
			e.getMessage();
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
		
		List<Student> withClassFieldsWheredStudents;
		try {
			withClassFieldsWheredStudents = fetchingController.select(Student.class).where("firstName", "=","'Andrzej'", QueryMode.CLASS);
			for(Student s : withClassFieldsWheredStudents) {
				System.out.println(s);
			}
		} catch (WrongQueryException e1) {
			e1.printStackTrace();
		}
		
		
		List<Student> student4;
		try {
			student4 = fetchingController.select(Student.class).where("studentId", "=","34", QueryMode.CLASS);
			for(Student s : student4) {
				System.out.println(s);
			}
		} catch (WrongQueryException e) {
			e.printStackTrace();
		}
		
		
		Student one;
		try {
			one = fetchingController.select(Student.class).id(5);
			System.out.println(one);
		} catch (WrongQueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
