package main.java.migration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import main.java.migration.exceptions.NoAttributeException;

class ConfigurationFileReaderTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	NamedNodeMap attrs;
	
	@BeforeEach
	void setUp() throws Exception {
		File f = new File("./test/main/java/migration/basic_xml.xml");
		Document doc = ConfigurationFileReader.prepareFile(f);
		NodeList nodes = doc.getElementsByTagName("node");
		attrs = nodes.item(0).getAttributes();
	}

//	@Test
//	void getAttrTest() throws NoAttributeException {
//		assertEquals("value", ConfigurationFileReader.getSafeAttr(attrs, "attr"));
//	}
//	
//	@Test()
//	void getAttrExceptionTest() {
//		assertThrows(NoAttributeException.class, () -> {ConfigurationFileReader.getSafeAttr(attrs, "no_attr");});
//	}
//	
//	
//	@Test
//	void getAttrSafeTest() throws NoAttributeException {
//		assertEquals("default", ConfigurationFileReader.getSafeAttr(attrs, "no_attr", "default"));
//	}
	
	
	
	
	

}
