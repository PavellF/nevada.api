package org.pavelf.nevada.api.logging;

/**
 * Defines general purpose application logger.
 * @author Pavel F.
 * @since 1.0
 * */
public interface Logger {

	public void info(String message);
	
	public void debug(String message);
	
	public void error(String message);
	
	public void trace(String message);
	
	public void warn(String message);
	
	public boolean isInfoEnabled();
	
	public boolean isDebugEnabled();
	
	public boolean isErrorEnabled();
	
	public boolean isTraceEnabled();
	
	public boolean isWarnEnabled();
}
