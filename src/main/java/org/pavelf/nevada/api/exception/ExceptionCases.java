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
	
	public static final ExceptionCase UNRECOGNIZED_USER =
			ExceptionCase.of(6, "Could not recognize bounded user.", 
					HttpStatus.FORBIDDEN);
	
	public static final ExceptionCase UNRECOGNIZED_APPLICATION =
			ExceptionCase.of(7, "Could not recognize applicaion.", 
					HttpStatus.FORBIDDEN);
	
	public static final ExceptionCase BANNED_APPLICATION =
			ExceptionCase.of(8, "Specified application can not perform requests due suspend.", 
					HttpStatus.FORBIDDEN);
	
	public static final ExceptionCase INVALID_BODY_PROPERTY =
			ExceptionCase.of(9, "Some of body property is invalid(e.g. invalid time).", 
					HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase ACCESS_DENIED =
			ExceptionCase.of(10, "Have no permission to access this entity.", 
					HttpStatus.FORBIDDEN);
	
	public static final ExceptionCase OUT_OF_BOUND_VALUE =
			ExceptionCase.of(11, "Some value exeeds its limit.", 
					HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase BODY_REQUIRED =
			ExceptionCase.of(12, "Request body is required to perform this action.", 
					HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase FAILED_UPDATE =
			ExceptionCase.of(13, "Could not update entity.", 
					HttpStatus.INTERNAL_SERVER_ERROR);
	
	public static final ExceptionCase NO_PREVIOUS_PASSWORD =
			ExceptionCase.of(14, "Need current password in order to update existing one.", 
					HttpStatus.FORBIDDEN);
	
	public static final ExceptionCase BANNED_PROFILE =
			ExceptionCase.of(15, "This profile cn not perform requests.", 
					HttpStatus.FORBIDDEN);
}
