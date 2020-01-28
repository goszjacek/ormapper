package main.java.migration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import main.java.migration.exceptions.ParsingException;
import main.java.migration.exceptions.WrongClassDescriptionException;
import main.java.migration.field.FieldFeature;
import main.java.migration.field.FieldType;
import user.Const;

class ConfigurationTest {
	Configuration c;
	
	@BeforeEach
	void setUp() throws Exception {
		System.out.println("***TEST***");
		c = new Configuration();		 
	}

	@Test
	void test() throws NoSuchMethodException, SecurityException, ClassNotFoundException, WrongClassDescriptionException {
		//get laptop 
		String desc = Const.USER_DESCRIPTIONS + "/laptop.xml";
		String path = Const.USER_CLASSES + ".Laptop";

		MappedClassDescription mcd = new MappedClassDescription(desc,path);
		assertLaptop(mcd);
	}

	/**
	 * @param mcd
	 */
	private void assertLaptop(MappedClassDescription mcd) {
		assertEquals(FieldType.INT, mcd.getField("laptopId").getFieldType());
		assertEquals("laptop_category_id", mcd.getField("laptopCategoryId").getColumnName());
		assertEquals(FieldType.INT, mcd.getField("laptopId").getFieldType());
		assertEquals(FieldType.INT, mcd.getField("laptopCategoryId").getFieldType());
		assertEquals(FieldType.STRING, mcd.getField("model").getFieldType());
		
	}
	
	
	@Test
	void parseSingleClassTest() throws ParsingException {
		c.parseMainFile(new File(Const.USER_RESOURCE + "/input.xml"));
		MappedClassDescription mcdLaptop = c.getDescription("Laptop");
		assertLaptop(mcdLaptop);
		System.out.println(c.getDescription("Student"));
		System.out.println(c.getDescription("Locker"));
	}
}
