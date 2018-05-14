package org.pavelf.nevada.api.logging;

public interface LoggerFactory {

	public Logger obtain(Class<?> forClass);	
	
	public Logger obtain(String name);
	
}
