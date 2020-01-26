package main.java.migration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import main.java.utils.MethodNameConverter;

public class ConfigurationFileReader {	
	
	/**
	 * Help function to prepare a file for XML-parsing 
	 * @param inputFile
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document prepareFile(File inputFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		
		doc.getDocumentElement().normalize();
		return doc;
	}
	
	
	
	
	
}
