package main.java.migration;

import java.io.File;

import main.java.database.Connector;

public class App {

	public static void main(String[] args) {
		MappingController mappingController = new MappingController();
		
		Connector.establishConnection();
		mappingController.readConfiguration(new File("./input.xml"));
		
		
	}

}
