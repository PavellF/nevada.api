package org.pavelf.nevada.api.exception;

/**
 * Generic web application exception.
 * @since 1.0
 * @author Pavel F. 
 * */
public class WebApplicationException extends RuntimeException {

	private static final long serialVersionUID = -2172411545261180945L;
	private final ExceptionCase exceptionCase;
	
	public WebApplicationException(ExceptionCase exceptionCase) {
		super(exceptionCase.toString());
		this.exceptionCase = exceptionCase;
	}

	public ExceptionCase getExceptionCase() {
		return exceptionCase;
	}

	

	
}
