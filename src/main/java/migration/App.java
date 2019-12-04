package main.java.migration;

import java.io.File;

import main.java.database.Connector;
import main.java.migration.exceptions.ParsingException;

public class App {

	public static void main(String[] args) {
		MappingController mappingController = new MappingController();
		
		Connector.establishConnection();
		try {
			mappingController.readConfiguration(new File("./input.xml"));
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
