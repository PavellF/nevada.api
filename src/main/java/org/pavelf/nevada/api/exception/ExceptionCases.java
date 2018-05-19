package org.pavelf.nevada.api.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionCases {

	public static final ExceptionCase MULTIPLE_IP_REQUESTS =
			ExceptionCase.of(1, "Too many requests that have the same token "
					+ "from defferent IP addresses has come in short period of time.", 
					HttpStatus.FORBIDDEN);
	
	public final ExceptionCase HEADER_SHOULD_BE_SPECIFIED = 
			ExceptionCase.of(2, "Accept or Content-Type header should be specified.", 
					HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase MALFORMED_ACCEPT_HEADER =
			ExceptionCase.of(3, "Could not recognize incomig request's header version parameter. ", 
					HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase MALFORMED_VERSION =
			ExceptionCase.of(4, "Can not parse requested object version.", 
					HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase REQUIRED_BODY_PROPERTY =
			ExceptionCase.of(5, "One of required properties in the body was not set.", 
					HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase USER_NOT_FOUND =
			ExceptionCase.of(6, "Could not recognize bounded user.", 
					HttpStatus.FORBIDDEN);
}
