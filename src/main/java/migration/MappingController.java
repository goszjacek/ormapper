package main.java.migration;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.action.fetching.FetchingController;
import main.java.action.storing.StoringController;
import main.java.migration.exceptions.ParsingException;


public class MappingController {
	private Configuration configuration = new Configuration(); 
	

	public void readConfiguration(File file) throws ParsingException {
		Configuration.parseMainFile(file);
		
	}
	
	public int migrateClasses(File file) {
//		scanClasses(configuration.getPaths());
//		for(String className : configuration.getClasses());
		return 0;
	}
	
	private void scanClasses(List<String> paths) {
		Map<String, MappedClassDescription> descriptions = new HashMap<>();
		for(String path : paths) {
			//get class
            ClassLoader classLoader = MappedClassDescription.class.getClassLoader();
			try {
				Class<?> classToScan = classLoader.loadClass(path);
				descriptions.put(path, ClassScanner.scanClass(classToScan));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	}
	
	public Configuration getConfiguration() {
		return this.configuration;
	}

	public FetchingController getFetchingController() {
		return new FetchingController(this.getConfiguration());
		
	}
	
	public StoringController getStoringController() {
		return new StoringController(this.getConfiguration());
	}
}
