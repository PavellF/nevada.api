package org.pavelf.nevada.api.logging;

/**
 * Defines loggers factory.
 * @author Pavel F.
 * @since 1.0
 * */
public interface LoggerFactory {

	public Logger obtain(Class<?> forClass);	
	
	public Logger obtain(String name);
	
}
