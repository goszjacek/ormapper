package main.java.action.fetching;

import main.java.database.Connector;
import main.java.src_class.Student;

public class FetchingTestApp {

	public static void main(String[] args) {
		Connector.establishConnection();
		FetchingController.selectAll();
		FetchingController.selectAll(Student.class);
	}

}
