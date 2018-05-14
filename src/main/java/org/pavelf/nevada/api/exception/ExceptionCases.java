package org.pavelf.nevada.api.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionCases {

	public static final ExceptionCase MULTIPLE_IP_REQUESTS =
			ExceptionCase.of(1, "Too many requests that have the same token "
					+ "from defferent IP addresses has come in short period of time.", 
					HttpStatus.FORBIDDEN);
	
	public final ExceptionCase HEADER_SHOULD_BE_SPECIFIED = 
			ExceptionCase.of(2, "Accept header should be specified.", HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase MALFORMED_ACCEPT_HEADER =
			ExceptionCase.of(3, "Could not recognize incomig request's Accept header version parameter. ", 
					HttpStatus.BAD_REQUEST);
	
	
}
