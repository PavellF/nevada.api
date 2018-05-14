package org.pavelf.nevada.api.logging;

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
