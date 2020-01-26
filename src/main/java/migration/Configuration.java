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
	 
	
	
	private static Class<?> readFieldClass(String fieldName, Class<?> cls) throws SecurityException, NoSuchFieldException {
		Field field = cls.getDeclaredField(fieldName);
		return field.getType();
		
		
	}
	
	private static void readClass(MappedClassDescription mcd) throws ClassNotFoundException {
		ClassLoader classLoader = Configuration.class.getClassLoader();
		Class<?> thisClass = classLoader.loadClass(mcd.getPath());
		mcd.setClassType(thisClass);
	
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
		// prepare mcd for class specific data
		MappedClassDescription mcd = new MappedClassDescription(descriptionLocation, path);
		
		//fill mcd with data
		try {
			Document doc = ConfigurationFileReader.prepareFile(new File(descriptionLocation));
			
			//inject name of the class -> TODO: extract 
			NodeList srcs = doc.getElementsByTagName("src-class");
	        Node src = srcs.item(0);
	        String srcTableNode = ((Element) src).getAttribute("table"), srcNameNode = ((Element) src).getAttribute("name");
	        if(srcTableNode=="" || srcNameNode=="")
	        	throw new NoAttributeException();
	        mcd.setTableName(srcTableNode);
	        mcd.setClassName(srcNameNode);
	        readClass(mcd);
			
	        // inject fields of the class -> TODO: extract
			SortedMap<String, FieldDescription> fieldDescriptions = new TreeMap<>();
			
			
			NodeList classnodeList = doc.getElementsByTagName("property");
			for (int i=0; i<classnodeList.getLength(); i++) {
				//inject one field -> TODO: extract
				Node property = classnodeList.item(i);
				if (property.getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) property;
					NamedNodeMap map = el.getAttributes();
					Node nameNode = map.getNamedItem("name"), columnNode = map.getNamedItem("column"), typeNode = map.getNamedItem("type"), featureNode = map.getNamedItem("feature");
					if(nameNode != null && columnNode != null && typeNode!=null) {
						String name = nameNode.getNodeValue(), column = columnNode.getNodeValue(), type=typeNode.getNodeValue();
						try {
							if(featureNode!=null) {
								// TODO: write graph what happens depending on the type, maybe use classes that inherit from FieldType instead of elseif logic
								if(featureNode.getNodeValue().equals("id")) {
									FieldDescription fd;
									fd = new FieldDescription(name, column, FieldDescription.getFieldType(type), readFieldClass(name, mcd.getClassType()),mcd.getClassType());
									mcd.setId(fd); 
									System.out.println(fd);
									fieldDescriptions.put(name, fd);
									
								
								}else if(featureNode.getNodeValue().toLowerCase().equals("unionetoone")) {
									FieldDescription fd;								
									fd = new FieldDescription(name, column, FieldType.ONETOONE, readFieldClass(name, mcd.getClassType()),mcd.getClassType());
									fieldDescriptions.put(name, fd);
								}else if(featureNode.getNodeValue().toLowerCase().equals("bionetoone")) {
									FieldDescription fd;								
									fd = new FieldDescription(name, column, FieldType.BIONETOONE, readFieldClass(name, mcd.getClassType()),mcd.getClassType());
									fieldDescriptions.put(name, fd);
								}
									
							}else {
								if(type.equals("text")) {
									fieldDescriptions.put(name, new FieldDescription(name, column, FieldType.STRING, String.class,mcd.getClassType()));
								}
								if(type.equals("int")) {
									fieldDescriptions.put(name, new FieldDescription(name, column, FieldType.INT, Integer.class,mcd.getClassType()));
								}
							}
							
						} catch (SecurityException | NoSuchFieldException e) {
							System.err.println("Check properties in a class description.");
							e.printStackTrace();
						}
						
					}else {
						throw new NoAttributeException();
					}
					
				}
			}
			mcd.setFields(fieldDescriptions);
			
			
			
	        return mcd;
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.err.println("The XML is wrong. Check the template documentation. ");
			e.printStackTrace();
			throw new WrongClassDescriptionException(e);
		} catch (NoAttributeException e) {
			System.err.println("You didn't provide some of the important attributes. ");
			e.printStackTrace();
			throw new WrongClassDescriptionException(e);
		} catch (ClassNotFoundException e) {
			System.err.println("No such class");
			throw new WrongClassDescriptionException(e);
		}
		
	}

	
	
	
	
	
}
