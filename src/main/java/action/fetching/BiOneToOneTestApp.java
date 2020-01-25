package main.java.action.fetching;

import java.io.File;
import java.util.List;

import main.java.action.fetching.exceptions.WrongQueryException;
import main.java.action.fetching.query.QueryMode;
import main.java.database.Connector;
import main.java.migration.MappingController;
import main.java.migration.exceptions.ParsingException;
import user.classes.Student;

public class BiOneToOneTestApp {

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
		FetchingController fc = mappingController.getFetchingController();
		try {
			List<Student> s = fc.select(Student.class).where("studentId","=", "1", QueryMode.CLASS);
			System.out.println(s.get(0).getFirstName() + "  "+			s.get(0).getLocker());
		} catch (WrongQueryException e) {
			e.printStackTrace();
		}
		

	}

}
