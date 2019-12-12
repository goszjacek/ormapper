package main.java.migration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.migration.exceptions.NoConfigurationException;

public class Configuration {
	private Map<String, MappedClassDescription> mappedClassDescs = new HashMap<String, MappedClassDescription>();

	public MappedClassDescription getDescription(String cls) {
		return mappedClassDescs.get(cls);
	}
	
	public MappedClassDescription getSafeDescription(String cls) throws NoConfigurationException {
		MappedClassDescription mcd = mappedClassDescs.get(cls);
		if(mcd == null)
			throw new NoConfigurationException(cls);
		return mcd;
	}
	 
	
	public void setDescription(String className, MappedClassDescription desc) {
		mappedClassDescs.put(className, desc);
	}

	
	
	
	
}
