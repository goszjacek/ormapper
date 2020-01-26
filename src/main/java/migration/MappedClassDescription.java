package main.java.migration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.SortedMap;
import java.util.TreeMap;

import main.java.migration.exceptions.NoAttributeException;
import main.java.migration.exceptions.WrongClassDescriptionException;
import main.java.migration.field.FieldDescription;
import main.java.migration.field.FieldType;
import main.java.migration.onetoone.OneToOneDescription;

public class MappedClassDescription {
	private Class classType;
	private String className, tableName;
	private Map<String,FieldDescription> fields;
	private Map<String,OneToOneDescription> oneToOnes;
	private String path, desc;
	FieldDescription id;
	
	public MappedClassDescription(String descriptionLocation, String path) throws ClassNotFoundException, WrongClassDescriptionException {
		this.desc = descriptionLocation;
		this.path = path;
		ClassLoader classLoader = Configuration.class.getClassLoader();
		Class<?> thisClass = classLoader.loadClass(this.path);
		setClassType(thisClass);
		
		parseDescription();
	}
	public FieldDescription getId() {
		return id;
	}
	public void setId(FieldDescription id) {
		this.id = id;
	}
	public Class getClassType() {
		return classType;
	}
	public void setClassType(Class<?> classType) {
		this.classType = classType;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String src) {
		this.className = src;
	}
	public Map<String, FieldDescription> getFields() {
		return fields;
	}
	public void setFields(Map<String, FieldDescription> fields) {
		this.fields = fields;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public FieldDescription getField(String fieldName) {
		return fields.get(fieldName);
	}
	
	@Override
	public String toString(){
		String s = className + "\n";
		for (Entry<String, FieldDescription> entry : fields.entrySet())  
		{
			s += entry.getValue().getColumnName() + "\n" + entry.getValue().getFieldName() + "\n";
		}
		return s;		
		
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Map<String, OneToOneDescription> getOneToOnes() {
		return oneToOnes;
	}
	public void setOneToOnes(Map<String, OneToOneDescription> oneToOnes) {
		this.oneToOnes = oneToOnes;
	}
	
	
	private void parseDescription() throws WrongClassDescriptionException{
		try {
			Document doc = ConfigurationFileReader.prepareFile(new File(desc));
			//fill mcd with data			
			//inject name of the class -> TODO: extract 
			NodeList srcs = doc.getElementsByTagName("src-class");
	        Node src = srcs.item(0);
	        String srcTableNode = ((Element) src).getAttribute("table"), srcNameNode = ((Element) src).getAttribute("name");
	        if(srcTableNode=="" || srcNameNode=="")
	        	throw new NoAttributeException();
	        setTableName(srcTableNode);
	        setClassName(srcNameNode);
			
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
									fd = new FieldDescription(name, column, FieldDescription.getFieldType(type), readFieldClass(name, getClassType()),getClassType());
									setId(fd); 
									System.out.println(fd);
									fieldDescriptions.put(name, fd);
									
								
								}else if(featureNode.getNodeValue().toLowerCase().equals("unionetoone")) {
									FieldDescription fd;								
									fd = new FieldDescription(name, column, FieldType.ONETOONE, readFieldClass(name, getClassType()),getClassType());
									fieldDescriptions.put(name, fd);
								}else if(featureNode.getNodeValue().toLowerCase().equals("bionetoone")) {
									FieldDescription fd;								
									fd = new FieldDescription(name, column, FieldType.BIONETOONE, readFieldClass(name, getClassType()),getClassType());
									fieldDescriptions.put(name, fd);
								}
									
							}else {
								if(type.equals("text")) {
									fieldDescriptions.put(name, new FieldDescription(name, column, FieldType.STRING, String.class, getClassType()));
								}
								if(type.equals("int")) {
									fieldDescriptions.put(name, new FieldDescription(name, column, FieldType.INT, Integer.class, getClassType()));
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
			setFields(fieldDescriptions);
		}
		catch (ParserConfigurationException | SAXException | IOException e) {
			System.err.println("The XML is wrong. Check the template documentation. ");
			e.printStackTrace();
			throw new WrongClassDescriptionException(e);
		}catch (NoAttributeException e) {
			System.err.println("You didn't provide some of the important attributes. ");
			e.printStackTrace();
			throw new WrongClassDescriptionException(e);
		}
		
		
	}
	
	private static Class<?> readFieldClass(String fieldName, Class<?> cls) throws SecurityException, NoSuchFieldException {
		Field field = cls.getDeclaredField(fieldName);
		return field.getType();	
		
	}
	
	
}
