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

import main.java.migration.exceptions.NamesNotMatchingException;
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
					FieldDescription fd = new FieldDescription(el.getAttribute("name"), el.getAttribute("column"), FieldDescription.getFieldType(el.getAttribute("type")));
					fieldDescriptions.put(el.getAttribute("name"), fd);
					System.out.println("new description " + el.getAttribute("name"));
				}
			}
			mcd.setFields(fieldDescriptions);
			NodeList srcs = doc.getElementsByTagName("src-class");
	        Node src = srcs.item(0);
	        mcd.setTableName(((Element) src).getAttribute("table"));
	        mcd.setClassName(((Element) src).getAttribute("name"));
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
}
