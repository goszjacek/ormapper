package main.java.migration;

import java.io.File;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import main.java.migration.exceptions.NoAttributeException;
import main.java.migration.field.FieldDescription;

public class ConfigurationFileReader {	
	
	private static Document prepareFile(File inputFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		
		doc.getDocumentElement().normalize();
		return doc;
	}
	
	public static void parseMainFile(File inputFile, Configuration configuration){
		
        
		try {
			Document doc = prepareFile(inputFile);
	        NodeList classNodeList = doc.getElementsByTagName("class");
	        
	        for (int i = 0; i < classNodeList.getLength(); i++) {
	           Node tmpClassNode = classNodeList.item(i);               
	           if (tmpClassNode.getNodeType() == Node.ELEMENT_NODE) {
	              Element cls = (Element) tmpClassNode;
	              String path = cls.getAttribute("path");
	              String desc = cls.getAttribute("description");
	              String clsName = cls.getAttribute("name");
	              String clsPath = path.replace('/','.');
	              MappedClassDescription mcd = new MappedClassDescription();
	              mcd.setPath(clsPath);
	              mcd.setDesc(desc);
	              readDescriptionFile(mcd);
	              readClass(mcd);
	              configuration.setDescription(clsName, mcd);
	           }	           
	        }
	        
	       
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
	}
	
	private static void readDescriptionFile(MappedClassDescription mcd) {
		try {
			Document doc = prepareFile(new File(mcd.getDesc()));
			
			SortedMap<String, FieldDescription> fieldDescriptions = new TreeMap<>();
			
			NodeList classnodeList = doc.getElementsByTagName("property");
			for (int i=0; i<classnodeList.getLength(); i++) {
				Node property = classnodeList.item(i);
				if (property.getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) property;
					String name = el.getAttribute("name"), column = el.getAttribute("column"), type=el.getAttribute("type");
					if(name=="" || column=="" || type=="" )
						throw new NoAttributeException();
					FieldDescription fd = new FieldDescription(name, column, FieldDescription.getFieldType(type));
					fieldDescriptions.put(name, fd);
				}
			}
			mcd.setFields(fieldDescriptions);
			
			
			NodeList srcs = doc.getElementsByTagName("src-class");
	        Node src = srcs.item(0);
	        String table = ((Element) src).getAttribute("table"), name = ((Element) src).getAttribute("name");
	        if(table=="" || name=="")
	        	throw new NoAttributeException();
	        mcd.setTableName(table);
	        mcd.setClassName(name);
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.err.println("The XML is wrong. Check the template documentation. ");
			e.printStackTrace();
		} catch (NoAttributeException e) {
			System.err.println("You didn't provide some of the important attributes. ");
			e.printStackTrace();
		}
		
	}
	
	
	private static void readClass(MappedClassDescription mcd) {
		ClassLoader classLoader = Configuration.class.getClassLoader();
		try {
			Class<?> thisClass = classLoader.loadClass(mcd.getPath());
			mcd.setClassType(thisClass);
		} catch (ClassNotFoundException e) {
			System.err.println("There is no such class. Define correct class path. Path provided: " + mcd.getPath());
			e.printStackTrace();
		}
	}
	
	
	
	
}
