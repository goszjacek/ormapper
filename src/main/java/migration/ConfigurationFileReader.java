package main.java.migration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
	
	
	
	
	
}
