package test.java;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import main.java.migration.ConfigurationFileReader;

class ConfigurationFileReaderTest {
	
	@Test
	void test() {
		List<String> paths = ConfigurationFileReader.parse(new File("input.txt"));
		assertTrue(paths.get(0).equals("com.jgo.pojos.Stock"));
		assertTrue(paths.get(1).equals("com.jgo.pojos.Category"));
		
	}

}
