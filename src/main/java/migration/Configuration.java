package main.java.migration;

import java.util.List;
import java.util.Map;

public class Configuration {
	private Map<String, MappedClassDescription> mappedClassDescs;

	public MappedClassDescription getDescription(String cls) {
		return mappedClassDescs.get(cls);
	}
	
	public void setDescription(String className, MappedClassDescription desc) {
		mappedClassDescs.put(className, desc);
	}

	
	public void readDescription() {
		
	}
	
	
}
