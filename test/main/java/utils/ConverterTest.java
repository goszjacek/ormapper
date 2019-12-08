package main.java.utils;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import main.java.utils.MethodNameConverter;

class ConverterTest {

	@Test
	void test() {
		String fieldName = "biggerDog";		
		assertTrue(MethodNameConverter.getGetter(fieldName).equals("getBiggerDog"));
	}
		
	@Test
	void testSetter() {
		String fieldName = "biggerDog";		
		assertTrue(MethodNameConverter.getSetter(fieldName).equals("setBiggerDog")); 
	}

}
