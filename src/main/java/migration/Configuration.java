package main.java.migration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import main.java.migration.exceptions.NoAttributeException;
import main.java.migration.exceptions.NoConfigurationException;
import main.java.migration.exceptions.ParsingException;
import main.java.migration.exceptions.WrongClassDescriptionException;
import main.java.migration.field.FieldDescription;
import main.java.migration.field.FieldType;

public class Configuration {
	private static Map<String, MappedClassDescription> mappedClassDescs = new HashMap<String, MappedClassDescription>();

	public MappedClassDescription getDescription(String cls) {
		return mappedClassDescs.get(cls);
	}
	
	public MappedClassDescription getSafeDescription(String cls) throws NoConfigurationException {
		MappedClassDescription mcd = mappedClassDescs.get(cls);
		if(mcd == null)
			throw new NoConfigurationException(cls);
		return mcd;
	}
	 
	
	
	
	

	public static void setDescription(String className, MappedClassDescription desc) {
		mappedClassDescs.put(className, desc);
	}

	/**
	 * Main parsing function where parameters are extracted.
	 * You just need to specify where is configuration file, and in configuration file class nodes with format:
	 * <class path="" description="" name="">
	 * @param inputFile
	 * @throws ParsingException
	 */
	public static void parseMainFile(File inputFile) throws ParsingException{
		
	    
		try {
			Document doc = ConfigurationFileReader.prepareFile(inputFile);
	        NodeList classNodeList = doc.getElementsByTagName("class");
	        for (int i = 0; i < classNodeList.getLength(); i++) {
	        	Node tmpClassNode = classNodeList.item(i);
	        	parseSingleClass(tmpClassNode);
	        }     
	       
		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.err.println("Parsing error");
			e.printStackTrace();
			throw new ParsingException(e);
		}
	}
	
	/**
	 * Parse single node describing a class
	 * @throws ParsingException 
	 */
	private static void parseSingleClass(Node tmpClassNode) throws ParsingException {
		           
        if (tmpClassNode.getNodeType() == Node.ELEMENT_NODE) {
           Element cls = (Element) tmpClassNode;
           NamedNodeMap attrs = cls.getAttributes();
           if(attrs == null) {
         	  throw new ParsingException(new NoAttributeException());	            	  
           }
           //get path, description and name of the class
           Node pathNode = attrs.getNamedItem("path");
           Node descNode = attrs.getNamedItem("description");
           Node clsNameNode = attrs.getNamedItem("name");
           if(pathNode == null || descNode == null || clsNameNode == null) {
         	  System.err.println("There are not enough attributes for the class");
         	  throw new ParsingException(); 
           }
           String desc = descNode.getNodeValue();
           String clsName = clsNameNode.getNodeValue();
           String clsPath = pathNode.getNodeValue().replace('/','.');
           try {
         	  //parse description file
         	  MappedClassDescription mcd = readDescriptionFile(desc, clsPath);
         	  setDescription(clsName, mcd);
           } catch(WrongClassDescriptionException e) {
         	  System.err.println("Description file wrong");
         	  throw new ParsingException(e);
           }           
        }	
	}
	
	
	/**
	 * Read a description of specific class. 
	 * @param descriptionLocation
	 * @param path
	 * @return
	 * @throws WrongClassDescriptionException
	 */
	static MappedClassDescription readDescriptionFile(String descriptionLocation, String path) throws WrongClassDescriptionException {
		try {
			// prepare mcd for class specific data
			MappedClassDescription mcd = new MappedClassDescription(descriptionLocation, path);
	        return mcd;
		} catch (ClassNotFoundException e) {
			System.err.println("No such class");
			throw new WrongClassDescriptionException(e);
		}
		
	}

	
	
	
	
	
}
