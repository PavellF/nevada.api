package org.pavelf.nevada.api.logging;

/**
 * Basic slf4j backed implementation for {@code Logger} that writes logs into 
 * file.
 * @author Pavel F.
 * @since 1.0
 * */
public class GenericLoggerImpl implements Logger {

	private org.slf4j.Logger logger;
	
	public GenericLoggerImpl(Class<?> forClass) {
		logger = org.slf4j.LoggerFactory.getLogger(forClass);
	}
	
	public GenericLoggerImpl(String name) {
		logger = org.slf4j.LoggerFactory.getLogger(name);
	}
	
	@Override
	public void info(String message) {
		logger.info(message);
	}

	@Override
	public void debug(String message) {
		logger.debug(message);
	}

	@Override
	public void error(String message) {
		logger.error(message);
		
	}

	@Override
	public void trace(String message) {
		logger.trace(message);
		
	}

	@Override
	public void warn(String message) {
		logger.warn(message);
		
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

}
